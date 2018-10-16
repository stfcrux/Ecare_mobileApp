package com.example.ecare_client.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ecare_client.R;
import com.example.ecare_client.TitleLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PersonalInfoActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private RecyclerView infoList;
    private Button btnSaveInfo;
    private EditText inputPhone;
    private EditText inputEmail;

    private  UserInfo getUserForm(){
        return new UserInfo(inputPhone.getText().toString().trim(),inputEmail.getText().toString().trim());

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.personalinfo_title);
        titleLayout.setTitleText("Personal Info");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        inputPhone = (EditText) findViewById(R.id.phone_input_et);
        inputEmail = (EditText) findViewById(R.id.full_name_et);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final FirebaseUser currentUser = auth.getCurrentUser();

        final DatabaseReference userRef = database.getReference().child("Users").child(currentUser.getUid());

        DatabaseReference contactIDRef = database.getReference().child("Info");

        ValueEventListener infoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    String contactEmail = "Null";
                    Boolean contactOnline = false;

                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("Email")) {
                            contactEmail = child.getValue(String.class);
                            inputEmail.setText(contactEmail, TextView.BufferType.EDITABLE);

                        }

                        if (child.getKey().equals("Online")) {
                            contactOnline = Boolean.parseBoolean(
                                    child.getValue(String.class));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing.
            }
        };


        btnSaveInfo = (Button) findViewById(R.id.btn_save);
        btnSaveInfo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfo userinfo = getUserForm();

                        userRef.child("Info").setValue(userinfo);

                    }
                }
        );

    }

}