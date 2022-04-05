package com.example.womensafetyapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
            String selectedItem = (String) parent.getItemAtPosition(position);
            String selectedNumber = arrayList.get(position).getEmergency_contact_number();
            Toast.makeText(this, selectedNumber, Toast.LENGTH_SHORT).show();
        });
    }
}