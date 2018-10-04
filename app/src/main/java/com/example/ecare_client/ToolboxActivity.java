package com.example.ecare_client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ToolboxActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_list);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.tool_box_title);
        titleLayout.setTitleText("ToolBox");


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
    }
    private void openPersonalInfo() {
            Intent personalInfo = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(personalInfo);
        }
    private void openToDOActivity() {
        Intent ChecklistActivity = new Intent(getApplicationContext(), com.example.ecare_client.checklist.MainActivity.class);
        startActivity(ChecklistActivity);
    }


}