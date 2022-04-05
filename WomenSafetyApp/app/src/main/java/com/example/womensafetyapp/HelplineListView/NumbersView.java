package com.example.womensafetyapp.HelplineListView;

public class NumbersView {
    // Emergency Contact Name
    private String Emergency_contact_name;

    // Emergency Contact Number
    private String Emergency_contact_number;

    public NumbersView(String emergency_contact_name, String emergency_contact_number) {
        Emergency_contact_name = emergency_contact_name;
        Emergency_contact_number = emergency_contact_number;
    }

    public String getEmergency_contact_number() {
        return Emergency_contact_number;
    }

    public void setEmergency_contact_number(String emergency_contact_number) {
        Emergency_contact_number = emergency_contact_number;
    }

    public String getEmergency_contact_name() {
        return Emergency_contact_name;
    }

    public void setEmergency_contact_name(String emergency_contact_name) {
        Emergency_contact_name = emergency_contact_name;
    }
}