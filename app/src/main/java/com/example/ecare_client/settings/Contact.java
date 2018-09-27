package com.example.ecare_client.settings;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Contact {
    private String mName;
    private boolean mOnline;
    private boolean mChecked;
    private String mKey;

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

    private static int lastContactId = 0;

    /*
    public static ArrayList<Contact> createContactsList(int numContacts) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(
                    new Contact(
                            "Person " + ++lastContactId,
                            ,
                            i <= numContacts / 2));
        }

        return contacts;
    }
     */
}
