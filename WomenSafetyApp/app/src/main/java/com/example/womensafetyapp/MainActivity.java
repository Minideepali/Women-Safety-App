package com.example.womensafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.womensafetyapp.UtilsService.SharedPreferenceClass;

public class MainActivity extends AppCompatActivity {
    Button Logout;
    Button sosButton;
    Button sirenButton;
    Button voiceRecordingButton;
    Button helplineButton;
    ImageView profile;

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
    }
}
