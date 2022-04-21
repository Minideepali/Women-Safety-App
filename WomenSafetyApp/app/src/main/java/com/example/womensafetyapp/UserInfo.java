package com.example.womensafetyapp;

public class UserInfo {
    private static String email;
    private static String name;

    private static String guardian_phone;
    private static String guardian_name;

    public static String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        UserInfo.email = email;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        UserInfo.name = name;
    }

    public static String getGuardian_name() {
        return guardian_name;
    }

    public static void setGuardian_name(String guardian_name) {
        UserInfo.guardian_name = guardian_name;
    }

    public static String getGuardian_phone() {
        return guardian_phone;
    }

    public static void setGuardian_phone(String guardian_phone) {
        UserInfo.guardian_phone = guardian_phone;
    }
}
