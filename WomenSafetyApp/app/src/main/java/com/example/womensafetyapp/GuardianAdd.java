package com.example.womensafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.womensafetyapp.UtilsService.SharedPreferenceClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GuardianAdd extends AppCompatActivity {
    ProgressBar progressBar;
    private String name;
    private String email;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_add);
        Toast.makeText(this, UserInfo.getEmail(), Toast.LENGTH_SHORT).show();
        Button add_button = findViewById(R.id.add_button);
        EditText name_ET = findViewById(R.id.name);
        EditText phone_ET = findViewById(R.id.phone);

        progressBar = findViewById(R.id.progress_bar);
        add_button.setOnClickListener(v -> {
            name = name_ET.getText().toString();
            email = UserInfo.getEmail();
            phone = phone_ET.getText().toString();
            AddGuardianOne();
        });
    }

    void AddGuardianOne() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("phone", phone);
        UserInfo.setGuardian_name(name);
        UserInfo.setGuardian_phone(phone);
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, phone, Toast.LENGTH_SHORT).show();
        String apiKey = "https://add-guardians.herokuapp.com/api/womenSafety/auth/add";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), response -> {
            try {
                if (response.getBoolean("success")) {
                    String token = response.getString("token");
                    SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(this);
                    sharedPreferenceClass.setValue_string("guardian_token", token);
                    Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                }
                progressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        }, error -> {

            NetworkResponse response = error.networkResponse;
            if (error instanceof ServerError && response != null) {
                try {
                    String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                    JSONObject obj = new JSONObject(res);
                    Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException | UnsupportedEncodingException je) {
                    je.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return params;
            }
        };

        // set retry policy
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // request add
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}