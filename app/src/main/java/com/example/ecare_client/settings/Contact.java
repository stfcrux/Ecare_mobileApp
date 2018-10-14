package com.example.ecare_client.settings;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class Contact implements Serializable {
    private String mName;
    private boolean mOnline;
    private boolean mChecked;
    private String mKey;

    private ContactListActivity mContext;

    private Button messageButton = null;

    private ValueEventListener contactEventListener;



    public Contact(String name, String key, boolean online) {
        mName = name;
        mOnline = online;
        mKey = key;

        mChecked = false;
    }



    public String getName() {
        return mName;
    }

    public void setName(String newName) {
        mName = newName;

    }

    public String getKey() {
        return mKey;

    }

    public boolean isOnline() {
        return mOnline;
    }

    public void setOnline(boolean value) {
        mOnline = value;

        if (messageButton != null) {
            messageButton.setEnabled(value);
        }

    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean value) {
        mChecked = value;

    }

    public ValueEventListener getContactEventListener() {
        return contactEventListener;
    }

    public void setContactEventListener(ValueEventListener listener) {
        contactEventListener = listener;
    }

    public ContactListActivity getContext() {
        return mContext;

    }

    public void setContext(ContactListActivity context) {
        mContext = context;

    }


    public void setMessageButton(Button newButton) {
        messageButton = newButton;

    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Contact) {
            Contact otherContact = (Contact) obj;

            return (otherContact.getKey().
                    equals(
                            getKey())
            );

        }

        else {
            return false;
        }
    }



    //private static int lastContactId = 0;


}
