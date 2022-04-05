package com.example.womensafetyapp.HelplineListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.womensafetyapp.R;

import java.util.ArrayList;

public class NumbersViewAdapter extends ArrayAdapter<NumbersView> {

    // invoke the suitable constructor of the ArrayAdapter class
    public NumbersViewAdapter(@NonNull Context context, ArrayList<NumbersView> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        NumbersView currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired Emergency_contact_name for the same
        TextView Emergency_contact_name = currentItemView.findViewById(R.id.Emergency_contact_name);
        Emergency_contact_name.setText(currentNumberPosition.getEmergency_contact_name());

        // then according to the position of the view assign the desired Emergency_contact_number for the same
        TextView Emergency_contact_number = currentItemView.findViewById(R.id.Emergency_contact_number);
        Emergency_contact_number.setText(currentNumberPosition.getEmergency_contact_number());

        // then return the recyclable view
        return currentItemView;
    }
}