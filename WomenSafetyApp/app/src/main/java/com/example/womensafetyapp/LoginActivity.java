package com.example.womensafetyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.womensafetyapp.UtilsService.UtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ProgressBar progressBar;
    UtilService utilService;
    SharedPreferenceClass sharedPreferenceClass;
    private EditText email_ET, password_ET;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        Button loginBtn = findViewById(R.id.loginBtn);
        email_ET = findViewById(R.id.email_ET);
        password_ET = findViewById(R.id.password_ET);
        progressBar = findViewById(R.id.progress_bar);
        utilService = new UtilService();

        sharedPreferenceClass = new SharedPreferenceClass(this);

        loginBtn.setOnClickListener(view -> {
            utilService.hideKeyboard(view, LoginActivity.this);
            email = email_ET.getText().toString();
            password = password_ET.getText().toString();
            if (validate(view)) {
                loginUser();
                getGuardianDetails();
            }
        });
    }

    private void loginUser() {
        progressBar.setVisibility(View.VISIBLE);

        final HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        String apiKey = "https://women-safety-app-api.herokuapp.com/api/womenSafety/auth/login";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), response -> {
            try {
                if (response.getBoolean("success")) {
                    String token = response.getString("token");
                    sharedPreferenceClass.setValue_string("token", token);
                    Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                    Toast.makeText(LoginActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
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

    private boolean validate(View view) {
        boolean isValid;

        if (!TextUtils.isEmpty(email)) {
            if (!TextUtils.isEmpty(password)) {
                isValid = true;
            } else {
                utilService.showSnackBar(view, "Enter a password.");
                isValid = false;
            }
        } else {
            utilService.showSnackBar(view, "Enter an email");
            isValid = false;
        }

        return isValid;
    }

    private void getGuardianDetails() {
        progressBar.setVisibility(View.VISIBLE);

        final HashMap<String, String> params = new HashMap<>();
        params.put("email", email);

        String apiKey = "https://add-guardians.herokuapp.com/api/womenSafety/auth/retrieve";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                apiKey, new JSONObject(params), response -> {
            try {
                if (response.getBoolean("success")) {
                    String token = response.getString("guardian_token");
                    sharedPreferenceClass.setValue_string("guardian_token", token);
                    Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                    Toast.makeText(LoginActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences user_women = getSharedPreferences("user_women", MODE_PRIVATE);
        if (user_women.contains("token")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}