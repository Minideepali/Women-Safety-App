package com.example.womensafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GuardianAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_add);
        Toast.makeText(this, UserInfo.getEmail(), Toast.LENGTH_SHORT).show();
        Button add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(v -> {
            Toast.makeText(this, "Guardian added", Toast.LENGTH_SHORT).show();
            //Go to MainActivity
            Intent intent = new Intent(GuardianAdd.this, MainActivity.class);
            startActivity(intent);
        });
    }
}