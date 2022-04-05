package com.example.womensafetyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.womensafetyapp.HelplineListView.NumbersView;
import com.example.womensafetyapp.HelplineListView.NumbersViewAdapter;

import java.util.ArrayList;

public class HelplineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline);
        final ArrayList<NumbersView> arrayList = new ArrayList<>();

        // Add Emergency Names and Numbers
        arrayList.add(new NumbersView("Ambulance", "911"));
        arrayList.add(new NumbersView("Police", "100"));
        arrayList.add(new NumbersView("Fire Brigade", "101"));
        arrayList.add(new NumbersView("Women Safety Department", "512"));

        // set the numbersViewAdapter for ListView
        NumbersViewAdapter numbersArrayAdapter = new NumbersViewAdapter(this, arrayList);
        ListView numbersListView = findViewById(R.id.listView);
        numbersListView.setAdapter(numbersArrayAdapter);
        numbersListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedNumber = arrayList.get(position).getEmergency_contact_number();
            //Intent for calling selected number
            Intent intent = new Intent(Intent.ACTION_CALL);
            Toast.makeText(this, selectedNumber, Toast.LENGTH_SHORT).show();
            intent.setData(android.net.Uri.parse("tel:" + selectedNumber));
            //startActivity(intent);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {

                try {
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}