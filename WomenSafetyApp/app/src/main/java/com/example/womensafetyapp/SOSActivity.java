package com.example.womensafetyapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.womensafetyapp.UtilsService.SharedPreferenceClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SOSActivity extends AppCompatActivity {
    public int counter;
    TextView countdownTextView;
    String Latitude;
    String Longitude;
    // initializing
    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient mFusedLocationClient;
    String guardian_name;
    String guardian_phone;
    CountDownTimer countDownTimer;

    // Initializing other items
    // from layout file
    TextView latitudeTextView, longitTextView;
    private final LocationCallback mLocationCallback = new LocationCallback() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            Latitude = mLastLocation.getLatitude() + "";
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
            Longitude = mLastLocation.getLongitude() + "";
        }
    };
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosactivity);

        latitudeTextView = findViewById(R.id.latTextView);
        longitTextView = findViewById(R.id.lonTextView);
        countdownTextView = findViewById(R.id.countdownTextView);
        Button stop_btn = findViewById(R.id.stop_btn);
        stop_btn.setVisibility(View.INVISIBLE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get the location
        getLastLocation();
        Button send;
        send = findViewById(R.id.sosButton);
        stop_btn.setOnClickListener(v -> {
            Intent intent = new Intent(SOSActivity.this, MainActivity.class);
            startActivity(intent);
            countDownTimer.cancel();
            finish();
        });
        send.setOnClickListener(view -> {
                    String message = "SOS! I am in danger. My location is: http://www.google.com/maps/place/" + Latitude + "," + Longitude;
                    CountDownTimer countDownTimer1;
                    countDownTimer1 = new CountDownTimer(20000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            countdownTextView.setText(String.valueOf(counter));
                            counter++;
                        }

                        @SuppressLint("SetTextI18n")
                        public void onFinish() {
                            countdownTextView.setText("FINISH!!");
                            getGuardianDetails();
                        }
                    };
                    countDownTimer1.start();
                    sendSMS(message);
                }
        );
    }

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        latitudeTextView.setText(location.getLatitude() + "");
                        Latitude = location.getLatitude() + "";
                        longitTextView.setText(location.getLongitude() + "");
                        Longitude = location.getLongitude() + "";
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    private void sendSMS(String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        } else {
            try {   // send sms
                countDownTimer = new CountDownTimer(180000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        countdownTextView.setText(String.valueOf(counter));
                        counter++;
                    }

                    @SuppressLint("SetTextI18n")
                    public void onFinish() {
                        countdownTextView.setText("FINISH!!");
                        sendSMS(message);
                    }
                };
                countDownTimer.start();
                String guardianNumber = guardian_phone;
                Button stop_btn = findViewById(R.id.stop_btn);
                stop_btn.setVisibility(View.VISIBLE);
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(guardianNumber, null, message, null, null);
                    Toast.makeText(this, "Trying to send SOS message...", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                } catch (Exception ignored) {
                }
            } catch (Exception ignored) {
            }
        }

    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
// if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    private void getGuardianDetails() {
        String url = " https://add-guardians.herokuapp.com/api/womenSafety/auth";
        SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(getApplicationContext());
        final String guardian_token = sharedPreferenceClass.getValue_string("guardian_token");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                if (response.getBoolean("success")) {
                    JSONObject userObj = response.getJSONObject("user");
                    guardian_name = userObj.getString("name");
                    guardian_phone = userObj.getString("phone");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", guardian_token);
                return params;
            }
        };
        int socketTimeout = 30000;
        new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
