package com.example.womensafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.womensafetyapp.UtilsService.SharedPreferenceClass;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    Button Logout;
    Button sosButton;
    Button sirenButton;
    Button voiceRecordingButton;
    Button helplineButton;
    CircleImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logout = findViewById(R.id.logout);
        sosButton = findViewById(R.id.sosButton);
        sirenButton = findViewById(R.id.sirenButton);
        voiceRecordingButton = findViewById(R.id.voiceRecordingButton);
        helplineButton = findViewById(R.id.helplineButton);

        SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(this);

        Logout.setOnClickListener(view -> {
            sharedPreferenceClass.clear();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        sosButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SOSActivity.class));
        });

        sirenButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SirenActivity.class));
        });

        voiceRecordingButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, VoiceRecordingActivity.class));
        });

        helplineButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, HelplineActivity.class));
        });

        //Click on Imageview profile to go to SOS activity
        profile = findViewById(R.id.profile_image);
        profile.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ProfileDetails.class));
        });
        setProfileImage();
    }

    private void setProfileImage() {
        SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(this);
        String url = " https://women-safety-app-api.herokuapp.com/api/womenSafety/auth";
        final String token = sharedPreferenceClass.getValue_string("token");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                if (response.getBoolean("success")) {
                    JSONObject userObj = response.getJSONObject("user");

                    Picasso.get()
                            .load(userObj.getString("avatar"))
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                            .error(R.drawable.ic_baseline_account_circle_24)
                            .into(profile);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(MainActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", token);
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
