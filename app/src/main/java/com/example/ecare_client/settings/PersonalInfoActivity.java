package com.example.ecare_client.settings;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
    private TextInputEditText inputPhone;
    private TextInputEditText inputName;

    private  UserInfo getUserForm(){
        return new UserInfo(inputPhone.getText().toString().trim(),inputName.getText().toString().trim());

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

        inputPhone = (TextInputEditText) findViewById(R.id.phone_input_et);
        inputName = (TextInputEditText) findViewById(R.id.full_name_et);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final FirebaseUser currentUser = auth.getCurrentUser();

        final DatabaseReference userRef = database.getReference().child("Users").child(currentUser.getUid());

        DatabaseReference infoRef = userRef.child("Info");
        // Attach a listener to read the data at our posts reference
        infoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String contactEmail = "Null";
                    String phoneNo = "Null";

                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("email")) {
                            contactEmail = child.getValue(String.class);
                            inputName.setText(contactEmail);

                        }else if (child.getKey().equals("phone")) {
                            phoneNo = child.getValue(String.class);
                            inputPhone.setText(phoneNo);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing.
            }
        });


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