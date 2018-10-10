package com.example.ecare_client;

import com.example.ecare_client.mainpageview.*;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;
    private ProgressDialog mSpinner;
    private String email = "null";
    private static final String TAG = "MainActivity";
    private TextView t1_temp,t2_city,t3_description,t4_date;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;
    private double Lat;
    private double Lon;


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

        SexangleImageView chatActivity = (SexangleImageView) findViewById(R.id.chat_activity);
        chatActivity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        openContactListActivity();
                }
                return false;
            }
        });

        SexangleImageView helpActivity = (SexangleImageView) findViewById(R.id.help);
        helpActivity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        sendHelpSMS();
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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
         locationListener = new LocationListener() {
             @Override
             public void onLocationChanged(Location location) {
                 currentLocation = location;
                 Lat = location.getLatitude();
                 Lon = location.getLongitude();
                 find_weather(Lat, Lon);
             }

             @Override
             public void onStatusChanged(String provider, int status, Bundle extras) {

             }

             @Override
             public void onProviderEnabled(String provider) {

             }

             @Override
             public void onProviderDisabled(String provider) {

             }
         };

        t1_temp = (TextView)findViewById(R.id.textView);
        t2_city = (TextView)findViewById(R.id.textView3);
        t3_description = (TextView)findViewById(R.id.textView4);
        t4_date = (TextView)findViewById(R.id.textView2);

        //find_weather();

        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);



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


    private void find_weather(double lat, double lon) {
        String url ="http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=f4d6dace6e140800a81c7003610b7de2&units=Imperial";

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

                    t2_city.setText(city);
                    t3_description.setText(description);
                    ImageView image = (ImageView) findViewById(R.id.imageView);
                    changeImage(description,image);

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

    private void changeImage(String s, ImageView image){
        switch (s){
            case "broken clouds":
            case "overcast clouds":
                image.setImageResource(R.drawable.icon_brokenclouds);
                break;
            case "sky is clear":
                image.setImageResource(R.drawable.icon_clearsky);
                break;
            case "light rain":
            case "moderate rain":
            case "heavy intensity rain":
                image.setImageResource(R.drawable.icon_rain);
                break;
            case "snow":
            case "light snow":
            case "moderate snow":
                image.setImageResource(R.drawable.icon_snow);
                break;
            case "mist":
                image.setImageResource(R.drawable.icon_mist);
                break;
            case "few clouds":
                image.setImageResource(R.drawable.icon_fewclouds);
                break;
            case "thunder storm":
                image.setImageResource(R.drawable.icon_thunderstorm);
                break;
            default:
                image.setImageResource(R.drawable.icon_clearsky);
                break;
        }
    }

    private void sendHelpSMS(){

        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        String phoneNo = "+610426443229";
        String myCurrentlocation = "https://www.google.com.au/maps/place/"+Lat+"+"+Lon;
        String message = "I need Help, I am at this location:"+myCurrentlocation;
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}

