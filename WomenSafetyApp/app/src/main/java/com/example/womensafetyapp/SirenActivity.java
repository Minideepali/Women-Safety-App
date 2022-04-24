package com.example.womensafetyapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SirenActivity extends AppCompatActivity {
    Button start_btn, stop_btn;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siren);
        start_btn = findViewById(R.id.start_btn);
        stop_btn = findViewById(R.id.stop_btn);

    }

    public void start_btn(View v) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.beep1);
            mediaPlayer.setOnCompletionListener(mp -> stopPlayer());
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void stop_btn(View v) {
        stopPlayer();
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(this, "Siren Stopped", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}