package com.example.ecare_client.settings;

import java.util.ArrayList;

public class Contact {
    private String mName;
    private boolean mOnline;
    private boolean mChecked;

    public Contact(String name, boolean online) {
        mName = name;
        mOnline = online;

        mChecked = false;
    }

    public String getName() {
        return mName;
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



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Contact) {
            Contact otherContact = (Contact) obj;

            return (otherContact.getName().
                    equals(
                            getName())
            );

        }

        else {
            return false;
        }
    }

    private static int lastContactId = 0;

    public static ArrayList<Contact> createContactsList(int numContacts) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(
                    new Contact(
                            "Person " + ++lastContactId,
                            i <= numContacts / 2));
        }

        return contacts;
    }
}
