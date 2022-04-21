package com.example.womensafetyapp;

public class UserInfo {
    private static String email;

    public static String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        UserInfo.email = email;
    }
}
