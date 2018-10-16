package com.example.ecare_client.settings;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInfo {

    public String phone;
    public String email;

    public UserInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserInfo(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

}