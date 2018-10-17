package com.example.ecare_client.settings;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInfo {

    public String phone, email, picPath;

    public UserInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserInfo(String phone, String email, String pic_path) {
        this.phone = phone;
        this.email = email;
        this.picPath = pic_path;
    }

}