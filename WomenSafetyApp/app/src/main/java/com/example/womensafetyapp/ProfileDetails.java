package com.example.womensafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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

public class ProfileDetails extends AppCompatActivity {
    SharedPreferenceClass sharedPreferenceClass;
    private TextView user_name;
    private TextView user_email;
    private CircleImageView userImage;

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

                    Picasso.with(getApplicationContext())
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
        SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(this);

        logout.setOnClickListener(view -> {
            sharedPreferenceClass.clear();
            Intent intent = new Intent(this, LoginActivity.class);
            //To clear the back stack and prevent the user from going back to the main activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}