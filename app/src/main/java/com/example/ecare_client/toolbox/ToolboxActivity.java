package com.example.ecare_client.toolbox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecare_client.BaseActivity;
import com.example.ecare_client.ChatActivity;
import com.example.ecare_client.MainActivity;
import com.example.ecare_client.MapsActivity;
import com.example.ecare_client.R;
import com.example.ecare_client.SinchService;
import com.example.ecare_client.mainpageview.SexangleImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sinch.android.rtc.SinchError;

public class ToolboxActivity extends BaseActivity {



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main_page);
        Intent intent = getIntent();

    }


}