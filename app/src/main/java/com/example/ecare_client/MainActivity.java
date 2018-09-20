package com.example.ecare_client;

import com.example.ecare_client.mainpageview.*;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sinch.android.rtc.SinchError;

public class MainActivity extends BaseActivity implements OnClickListener, SinchService.StartFailedListener {

    private ProgressDialog mSpinner;
    private String email = "null";
    private static final int JUMP_FLAG1 = 1;
    private static final int JUMP_FLAG2 = 2;
    private static final int JUMP_FLAG3 = 3;
    private static final int JUMP_FLAG4 = 4;
    private static int requestid;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        //asking for permissions here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE},100);
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            email = user.getEmail();
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
        }

        SexangleImageView sexangleImageView = (SexangleImageView) findViewById(R.id.chat_activity);
        sexangleImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        loginClicked();
                        Log.d(TAG, email);
                }
                return false;
            }
        });


        SexangleImageView MapsView = (SexangleImageView) findViewById(R.id.btnOpenMaps);
        MapsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        openMapsActivity();
                }
                return false;
            }
        });

        SexangleImageView ChecklistView = (SexangleImageView) findViewById(R.id.btnOpenChecklist);
        ChecklistView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        openChecklistActivity();
                }
                return false;
            }
        });


        SexangleImageView ContactListView = (SexangleImageView) findViewById(R.id.btnOpenContactList);
        ContactListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        openContactListActivity();
                }
                return false;
            }
        });

    }
    @Override
    public void onClick(View v) {
    }
    private static void Jump_page(int t){
        switch(t){
            case 1:
                Log.d("TEST","跳往图片");
                //openChatActivity();
                break;
            case 2:
                Log.d("TEST","跳往邮件");
                break;
            case 3:
                Log.d("TEST","跳往视频");
                break;
            case 4:
                Log.d("TEST","跳往文件");
                //openMapsActivity();
                break;
            default:
                break;

        }
    }
    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
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
        openChatActivity();
    }

    private void loginClicked() {
        String userName = email;

        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            openChatActivity();
        }
    }

    private void openChatActivity() {
        Intent chatActivity = new Intent(this, ChatActivity.class);
        startActivity(chatActivity);
    }

    private void openMapsActivity() {
        Intent MapsActivity = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(MapsActivity);
    }

    private void openChecklistActivity() {
        Intent ChecklistActivity = new Intent(getApplicationContext(), com.example.ecare_client.checklist.MainActivity.class);
        startActivity(ChecklistActivity);
    }

    private void openContactListActivity() {
        Intent ContactListActivity = new Intent(getApplicationContext(), com.example.ecare_client.settings.ContactListActivity.class);
        startActivity(ContactListActivity);

    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }

    public static void setRequestid(int request) {
        Jump_page(request);
    }
}

