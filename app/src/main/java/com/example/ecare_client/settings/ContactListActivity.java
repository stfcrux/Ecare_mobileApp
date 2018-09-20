package com.example.ecare_client.settings;

import android.content.Intent;
import android.os.Bundle;

import com.example.ecare_client.R;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.example.ecare_client.registration.ResetPasswordActivity;
import com.example.ecare_client.registration.SignupActivity;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class ContactListActivity extends AppCompatActivity {

    private EditText inputContact;
    private Button btnAddContact;
    private ProgressBar progressBar;

    private ListView contactListView;
    private FirebaseAuth auth;

    private ArrayAdapter<String> listAdapter;

    private FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        btnAddContact = (Button) findViewById(R.id.add_contact_button);
        inputContact = (EditText) findViewById(R.id.contact_email);
        contactListView = (ListView) findViewById(R.id.contact_list_view);


        //-----------------------------------------------------------------------------
        final ArrayList<String> TEST_ARRAY = new ArrayList<>();

        listAdapter =
                new ArrayAdapter<String>(
                        this,
                        R.layout.contact_list_text_view,
                        TEST_ARRAY);

        contactListView.setAdapter(listAdapter);
        //-----------------------------------------------------------------------------
        FirebaseUser currentUser = auth.getCurrentUser();

        final DatabaseReference userRef =
                database.getReference().child("Users").child(currentUser.getUid());

        Query queryContacts = userRef.child("Contacts").orderByKey();

        queryContacts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TEST_ARRAY.clear();
                    // dataSnapshot is the "Users" node with all children with contactEmail.
                    for (DataSnapshot matchingContact : dataSnapshot.getChildren()) {

                        // Very bad way of doing this perhaps.
                        if ( !(matchingContact.getKey().equals("Null")) ) {

                            TEST_ARRAY.add(matchingContact.getKey());

                        }
                        // UPDATE TEST_ARRAY HERE!!!


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing.
            }
        });


        btnAddContact.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String contactEmail = inputContact.getText().toString().trim();


                    DatabaseReference queryRef =
                            database.getReference().child("Users");

                    // Check if contactEmail is in the database!!!
                    Query queryNew = queryRef.orderByChild("Email").equalTo(contactEmail);


                    queryNew.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // dataSnapshot is the "Users" node with all children with contactEmail.
                                for (DataSnapshot matchingContact : dataSnapshot.getChildren()) {
                                    // do something with the individual "contact"
                                    // since we're in this loop, we know the contact email exists.
                                    String contactUid = matchingContact.getKey();
                                    userRef.child("Contacts").child(contactUid).setValue("Null");

                                    // UPDATE TEST_ARRAY HERE!!!


                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Do nothing.
                        }
                    });

                    // NEED TO REFRESH THE LIST VIEW!!!!
                    listAdapter.notifyDataSetChanged();



                }
            }

        );

    }




    @Override
    protected void onResume() {
        super.onResume();
    }
}
