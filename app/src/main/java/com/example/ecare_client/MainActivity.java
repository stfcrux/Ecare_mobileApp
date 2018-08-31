package com.example.ecare_client;

import com.example.ecare_client.mainpageview.*;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ecare_client.videochat.BaseActivity;
import com.sinch.android.rtc.SinchError;

public class MainActivity extends BaseActivity implements OnClickListener, SinchService.StartFailedListener {

    private ProgressDialog mSpinner;
    private static final int JUMP_FLAG1 = 1;
    private static final int JUMP_FLAG2 = 2;
    private static final int JUMP_FLAG3 = 3;
    private static final int JUMP_FLAG4 = 4;
    private static int requestid;

    public static Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case JUMP_FLAG1:
                    Jump_page(1);
                    break;
                case JUMP_FLAG2:
                    Jump_page(2);
                    break;
                case JUMP_FLAG3:
                    Jump_page(3);
                    break;
                case JUMP_FLAG4:
                    Jump_page(4);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        //asking for permissions here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE},100);
        }

        /*
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)


        //SexangleViewGroup sexangleViewGroup = (SexangleViewGroup) findViewById(R.id.sexangleView1);
        //RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(sexangleViewGroup.getLayoutParams());

        //lp.setMargins(0, screenHeight/2, 0, 0);
        //sexangleViewGroup.setLayoutParams(lp);
        */
        SexangleImageView sexangleImageView = (SexangleImageView) findViewById(R.id.sexanglepic);
        sexangleImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        loginClicked();
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
        String userName = "Jerry";

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

