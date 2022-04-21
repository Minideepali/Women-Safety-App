package com.example.womensafetyapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.womensafetyapp.UtilsService.SharedPreferenceClass;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetails extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 9544;
    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    SharedPreferenceClass sharedPreferenceClass;
    Uri selectedImage;
    String part_image;
    private TextView user_name;
    private TextView user_email;
    private CircleImageView userImage;

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        user_name = findViewById(R.id.username);
        user_email = findViewById(R.id.user_email);
        userImage = findViewById(R.id.avatar);
        Button logout = findViewById(R.id.logout);
        sharedPreferenceClass = new SharedPreferenceClass(this);
        //getUserProfile
        String url = " https://women-safety-app-api.herokuapp.com/api/womenSafety/auth";
        final String token = sharedPreferenceClass.getValue_string("token");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                if (response.getBoolean("success")) {
                    JSONObject userObj = response.getJSONObject("user");
                    user_name.setText(userObj.getString("username"));
                    user_email.setText(userObj.getString("email"));

                    Picasso.get()
                            .load(userObj.getString("avatar"))
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                            .error(R.drawable.ic_baseline_account_circle_24)
                            .into(userImage);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(ProfileDetails.this, "Error " + error, Toast.LENGTH_SHORT).show();
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
        getGuardianDetails();
        SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(this);

        logout.setOnClickListener(view -> {
            sharedPreferenceClass.clear();
            Intent intent = new Intent(this, LoginActivity.class);
            //To clear the back stack and prevent the user from going back to the main activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        //Clicking User Avatar to run a function called UploadImage
        userImage.setOnClickListener(view -> {
            UploadImage();
        });
    }

    private void getGuardianDetails() {
        TextView guardian_name = findViewById(R.id.guardian_name);
        TextView guardian_phone = findViewById(R.id.guardian_phone);
        String url = " https://add-guardians.herokuapp.com/api/womenSafety/auth";
        final String guardian_token = sharedPreferenceClass.getValue_string("guardian_token");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                if (response.getBoolean("success")) {
                    JSONObject userObj = response.getJSONObject("user");
                    guardian_name.setText(userObj.getString("name"));
                    guardian_phone.setText(userObj.getString("phone"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(ProfileDetails.this, "Error " + error, Toast.LENGTH_SHORT).show();
        }) {
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

    private void UploadImage() {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        verifyStoragePermissions(ProfileDetails.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Open Gallery"), PICK_IMAGE_REQUEST);
    }

    // Method to get the absolute path of the selected image from its URI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();                                                         // Get the image file URI
                String[] imageProjection = {MediaStore.Images.Media.DATA};
                try (Cursor cursor = getContentResolver().query(selectedImage, imageProjection, null, null, null)) {
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int indexImage = cursor.getColumnIndex(imageProjection[0]);
                        part_image = cursor.getString(indexImage);                                          // Get the image file absolute path
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        userImage.setImageBitmap(bitmap);

                        ByteArrayOutputStream baOS = new ByteArrayOutputStream();
                        if (bitmap != null) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baOS); // bm is the bitmap object
                        }

                    }
                }
            }
        }
    }
}