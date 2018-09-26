package com.example.ecare_client.settings;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import com.example.ecare_client.R;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ItemAnimator;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.example.ecare_client.registration.ResetPasswordActivity;
import com.example.ecare_client.registration.SignupActivity;
import com.example.ecare_client.settings.widgets.ContactAdapter;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

    private FirebaseAuth auth;

    private RecyclerView contactListView;
    private ArrayList<Contact> contactArrayList;

    private FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        btnAddContact = (Button) findViewById(R.id.add_contact_button);
        inputContact = (EditText) findViewById(R.id.contact_email);

        contactListView = (RecyclerView) findViewById(R.id.contact_list_view);

        // NEED A DELETE BUTTON IN THE LIST!!!!!!
        final ArrayList<Contact> contacts = new ArrayList<>();

        // contactIDs is just used to pass the contacts onto another method.
        final ArrayList<String> contactIDs = new ArrayList<>();

        final ContactAdapter adapter = new ContactAdapter(contacts);

        contactListView.setAdapter(adapter);
        // Set layout manager to position the items
        contactListView.setLayoutManager(new LinearLayoutManager(this));

        //-----------------------------------------------------------------------------
        FirebaseUser currentUser = auth.getCurrentUser();

        final DatabaseReference userRef =
                database.getReference().child("Users").child(currentUser.getUid());

        Query queryContacts = userRef.child("Contacts").orderByKey();


        queryContacts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // dataSnapshot is the "Users" node with all children with contactEmail.
                    for (DataSnapshot matchingContact : dataSnapshot.getChildren()) {

                        // Very bad way of doing this perhaps.
                        if ( !(matchingContact.getKey().equals("Null")) ) {

                            contactIDs.add(matchingContact.getKey());


                        }

                    }

                    setContactListeners(contactIDs, contacts, adapter);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing.
            }
        });




        //-----------------------------------------------------------------------------




        // Initialize contacts

        // Create adapter passing in the sample user data

        // Attach the adapter to the recyclerview to populate items


        // Refresh list after doing the query, even before pressing AddContact.



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


                                    setContactListener(contactUid, contacts, adapter);


                                }
                                // NEED TO REFRESH THE LIST VIEW!!!!

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Do nothing.
                        }
                    });


                }
            }

        );




    }




    @Override
    protected void onResume() {
        super.onResume();
    }


    protected void setContactListeners(ArrayList<String> contactIDs,
                               final ArrayList<Contact> contacts,
                               final ContactAdapter adapter) {


        for (String contactID : contactIDs) {
            setContactListener(contactID, contacts, adapter);


        }


    }

    protected void setContactListener(String contactID,
                                       final ArrayList<Contact> contacts,
                                       final ContactAdapter adapter) {

        database = FirebaseDatabase.getInstance();

        DatabaseReference userRef =
                database.getReference().child("Users");

        DatabaseReference contactIDRef = userRef.child(contactID);



        contactIDRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // Need the email and the online status.
                String contactEmail = dataSnapshot.child("Email").getValue(String.class);
                Log.d("contactEmail", contactEmail);

                Boolean contactOnline =
                        Boolean.parseBoolean(
                                dataSnapshot.child("Online").getValue(String.class));

                Contact contactObject = new Contact(contactEmail, contactOnline);

                int currentIndex = contacts.indexOf(contactObject);

                // In case the contact wasn't previously in the list.


                if (currentIndex != -1) {
                    if (contactOnline) {
                        contacts.remove(currentIndex);
                        adapter.notifyItemRemoved(currentIndex);

                        contacts.add(0, contactObject);
                        adapter.notifyItemInserted(0);
                    }

                    else {
                        contacts.remove(currentIndex);
                        contacts.add(currentIndex, contactObject);
                        adapter.notifyItemChanged(currentIndex);

                    }
                }

                else {
                    if (contactOnline) {
                        contacts.add(0, contactObject);
                        adapter.notifyItemInserted(0);
                    }

                    else {
                        contacts.add(contactObject);
                        int lastPosition = contacts.size() - 1;
                        adapter.notifyItemInserted(lastPosition);

                    }
                }


            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {


            }

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {


            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing.
            }
        });



    }
}
