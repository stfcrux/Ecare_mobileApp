package com.example.ecare_client.settings;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInfo {

    public String phone, email, picPath, carerName, carerPhone;
    public String isCarer;

    public UserInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserInfo(String phone, String email, String pic_path, String carerName, String isCarer, String carerPhone) {
        this.phone = phone;
        this.email = email;
        this.picPath = pic_path;
        this.carerName = carerName;
        this.isCarer = isCarer;
        this.carerPhone = carerPhone;
    }

}