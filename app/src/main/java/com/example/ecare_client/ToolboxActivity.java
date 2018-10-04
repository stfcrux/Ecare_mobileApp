package com.example.ecare_client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ToolboxActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_list);
        Intent intent = getIntent();


        ImageView personalInfo = (ImageView) findViewById(R.id.btnPersonalInfo);
        personalInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        openPersonalInfo();
                }
                return false;

            }
        });


        ImageView checkList = (ImageView) findViewById(R.id.btnOpenChecklist);
        checkList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        openToDOActivity();
                }
                return false;

            }
        });

        ImageView alarmClock = (ImageView) findViewById(R.id.btnOpenAlarmClock);
        alarmClock.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        openAlarmClock();
                }
                return false;

            }
        });
    }
    private void openPersonalInfo() {
            Intent personalInfo = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(personalInfo);
        }
    private void openToDOActivity() {
        Intent ChecklistActivity = new Intent(getApplicationContext(), com.example.ecare_client.checklist.MainActivity.class);
        startActivity(ChecklistActivity);
    }
    private void openAlarmClock() {
        Intent AlarmClockActivity = new Intent(getApplicationContext(), com.example.ecare_client.alarmclock.MainActivity.class);
        startActivity(AlarmClockActivity);
    }


}