package com.example.ecare_client.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecare_client.BaseActivity;
import com.example.ecare_client.ChatActivity;
import com.example.ecare_client.R;
import com.example.ecare_client.SinchService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.SinchError;

public class ContactProfileActivity extends BaseActivity implements SinchService.StartFailedListener {

    private ImageView contactPicture;
    private TextView contactEmail;
    private TextView contactNickname;
    private Button messageButton;
    private Button updateNicknameButton;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private ProgressDialog mSpinner;



    private String selectedContactName;
    private String selectedContactKey;
    private String selectedContactNickname;

    private ValueEventListener onlineListener;



    protected void onCreate(Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);

        selectedContactName = getIntent().getStringExtra("ContactName");
        selectedContactKey = getIntent().getStringExtra("ContactKey");
        selectedContactNickname = getIntent().getStringExtra("ContactNickname");

        DatabaseReference contactIDRef =
                database.getReference().child("Users").child(selectedContactKey);

        setContentView(R.layout.activity_contact_profile);

        contactPicture = (ImageView) findViewById(R.id.contact_picture);
        contactEmail = (TextView) findViewById(R.id.contact_email);
        contactNickname = (TextView) findViewById(R.id.contact_nickname);
        messageButton = (Button) findViewById(R.id.message_button);
        updateNicknameButton = (Button) findViewById(R.id.update_nickname);

        contactEmail.setText(selectedContactName);

        contactNickname.setText(selectedContactNickname);

        updateNicknameButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                String newText = contactNickname.getText().toString().trim();

                String userID = auth.getCurrentUser().getUid();

                
                database.getReference().child("Users").child(userID).
                        child("Contacts").child(selectedContactKey).setValue(newText);

                Toast.makeText(getApplicationContext(),
                        "Nickname updated.",
                        Toast.LENGTH_SHORT).show();

                // FINISH THIS


            }

        });


        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                beginChat(selectedContactName);


            }
        });


        onlineListener = new ValueEventListener() {
            @Override
            // THE DATA SNAPSHOT IS AT THE CHILD!! NOT THE ROOT NODE!!!!
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean contactOnline = false;

                for(DataSnapshot child : dataSnapshot.getChildren()) {

                    if (child.getKey().equals("Online")) {
                        contactOnline = Boolean.parseBoolean(
                                child.getValue(String.class));
                    }

                }

                messageButton.setEnabled(contactOnline);
                messageButton.setText(contactOnline ? "Message" : "Offline");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing.
            }
        };

        contactIDRef.addValueEventListener(onlineListener);

    }




    public void beginChat(String contactName) {

        selectedContactName = contactName;

        auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(email);
            showSpinner();

        } else {

            Intent chatActivity = new Intent(this, ChatActivity.class);

            Bundle options = new Bundle();
            options.putString("ContactName", contactName);

            chatActivity.putExtras(options);

            startActivity(chatActivity);
        }

    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Connecting");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }




    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }


    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {

        Intent chatActivity = new Intent(this, ChatActivity.class);

        Bundle options = new Bundle();
        options.putString("ContactName", selectedContactName);

        chatActivity.putExtras(options);

        startActivity(chatActivity);
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

}
