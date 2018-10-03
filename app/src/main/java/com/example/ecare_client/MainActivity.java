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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecare_client.settings.PersonalInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends BaseActivity implements OnClickListener {

    private ProgressDialog mSpinner;
    private String email = "null";
    private static final String TAG = "MainActivity";
    private TextView t1_temp,t2_city,t3_description,t4_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_layout);

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

        SexangleImageView ChecklistView = (SexangleImageView) findViewById(R.id.btnOpenSettings);
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


        SexangleImageView PersonalInfoView = (SexangleImageView) findViewById(R.id.btnOpenPersonalInfo);
        PersonalInfoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        openPersonalInfoActivity();
                }
                return false;
            }
        });

        t1_temp = (TextView)findViewById(R.id.textView);
        t2_city = (TextView)findViewById(R.id.textView3);
        t3_description = (TextView)findViewById(R.id.textView4);
        t4_date = (TextView)findViewById(R.id.textView2);

        find_weather();

    }

    @Override
    public void onClick(View v) {
    }

//    @Override
//    protected void onServiceConnected() {
//        getSinchServiceInterface().setStartListener(this);
//    }

    @Override
   protected void onPause() {
        if (mSpinner != null) {
           mSpinner.dismiss();
        }
        super.onPause();
    }

//    @Override
//    public void onStartFailed(SinchError error) {
//        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
//        if (mSpinner != null) {
//            mSpinner.dismiss();
//        }
//    }

//    @Override
//    public void onStarted() {
//        openContactListActivity();
//    }

    private void loginClicked() {
        String userName = email;
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        }
        openContactListActivity();
    }
    /*
    private void openChatActivity() {
        Intent chatActivity = new Intent(this, ChatActivity.class);
        startActivity(chatActivity);
    }
    */

    private void openMapsActivity() {
        Intent MapsActivity = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(MapsActivity);
    }

    private void openChecklistActivity() {
        Intent ChecklistActivity = new Intent(getApplicationContext(), ToolboxActivity.class);
        startActivity(ChecklistActivity);
    }

    private void openContactListActivity() {
        Intent ContactListActivity = new Intent(getApplicationContext(), com.example.ecare_client.settings.ContactListActivity.class);
        startActivity(ContactListActivity);

    }

    private void openPersonalInfoActivity() {
        Intent personalInfoActivity = new Intent(this, PersonalInfoActivity.class);
        startActivity(personalInfoActivity);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }


    private void find_weather() {
        String url ="http://api.openweathermap.org/data/2.5/weather?q=melbourne&appid=f4d6dace6e140800a81c7003610b7de2&units=Imperial";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String description = object.getString("description");
                    String city = response.getString("name");

                    //  t1_temp.setText(temp);
                    t2_city.setText(city);
                    t3_description.setText(description);
                    ImageView image = (ImageView) findViewById(R.id.imageView);
                    image.setImageResource(R.drawable.icon_fewclouds);

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                    String formatted_date = sdf.format(calendar.getTime());

                    t4_date.setText(formatted_date);

                    double temp_int = Double.parseDouble(temp);
                    double centi = (temp_int - 32) /1.8000;
                    centi = Math.round(centi);
                    int i = (int)centi;
                    t1_temp.setText(String.valueOf(i));



                }catch(JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);


    }
}

