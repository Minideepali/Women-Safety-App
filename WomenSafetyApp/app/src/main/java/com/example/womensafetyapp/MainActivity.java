package com.example.womensafetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.womensafetyapp.UtilsService.SharedPreferenceClass;

public class MainActivity extends AppCompatActivity {
    Button Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logout = findViewById(R.id.logout);
        SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(this);
        Logout.setOnClickListener(view -> {
            sharedPreferenceClass.clear();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}