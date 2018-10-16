package com.example.ecare_client;

import com.example.ecare_client.videochat.*;
import com.example.ecare_client.textchat.*;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity implements MessageClientListener{

    private EditText inputText;
    private Button send;
    private Button videochat;
    private Button sendCurrentLocation;
    private Button more;
    private RecyclerView msgRecyclerView;
    private String makeCallTo;
    private MsgAdapter adapter;
    private static final String TAG = "ChatActivity";
    private TextView leftMsg;
    private TextView rightMsg;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String Lat;
    private String Lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            adapter = savedInstanceState.getParcelable("adapter");
        }else {
            adapter = new MsgAdapter(getApplicationContext());
        }
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.chat_title);
        titleLayout.setTitleText("Chat");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Lat = String.valueOf(location.getLatitude());
                Lon = String.valueOf(location.getLongitude());
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

        makeCallTo = getIntent().getExtras().getString("ContactName");

        //makeCallTo = "testexample@example.com";

        rightMsg = (TextView) findViewById(R.id.right_msg);
        leftMsg = (TextView) findViewById(R.id.left_msg);
        sendCurrentLocation = (Button) findViewById(R.id.send_location);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        videochat = (Button) findViewById(R.id.video_call);
        more = (Button) findViewById(R.id.more);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);

        videochat.setOnClickListener(buttonClickListener);
        send.setOnClickListener(buttonClickListener);
        sendCurrentLocation.setOnClickListener(buttonClickListener);
        more.setOnClickListener(buttonClickListener);
        if(rightMsg != null && leftMsg !=null) {
            rightMsg.setOnClickListener(buttonClickListener);
            leftMsg.setOnClickListener(buttonClickListener);
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
    }


    private void sendMsg(){
        String content = inputText.getText().toString();
        if (!"".equals(content)) {

            getSinchServiceInterface().sendMessage(makeCallTo, content);
            //adapter.notifyItemInserted(adapter.getItemCount() - 1); 当有新消息时涮新RecyclerView 中的显示
            msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);//定位到最后一行;
            inputText.setText("");//清空输入栏；
        }

    }



    //to place the call to the entered name
    private void callButtonClicked() {

        Call call = getSinchServiceInterface().callUserVideo(makeCallTo);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.video_call:
                    callButtonClicked();
                    break;
                case R.id.send:
                    sendMsg();
                    break;
                case R.id.send_location:
                    sendCurrentLocation();
                    break;
                case R.id.more:

                        sendCurrentLocation.setVisibility(View.VISIBLE);
                        sendCurrentLocation.setClickable(true);
                        videochat.setVisibility(View.VISIBLE);
                        videochat.setClickable(true);
                        more.setContentDescription("back");

                    /*
                    if (more.getContentDescription().equals("back")){
                        sendCurrentLocation.setVisibility(View.INVISIBLE);
                        sendCurrentLocation.setClickable(false);
                        videochat.setVisibility(View.INVISIBLE);
                        videochat.setClickable(false);
                        more.setContentDescription(null);

                    }
                    */
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().removeMessageClientListener(this);
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }

    @Override
    public void onIncomingMessage(MessageClient client, Message message) {

        adapter.addMessage(message, adapter.DIRECTION_INCOMING);
        //leftMsg.setOnClickListener(buttonClickListener);
    }

    @Override
    public void onMessageSent(MessageClient client, Message message, String recipientId) {

        adapter.addMessage(message, adapter.DIRECTION_OUTGOING);
        //rightMsg.setOnClickListener(buttonClickListener);
        //Toast.makeText(this, "Sent", Toast.LENGTH_LONG);
    }

    @Override
    public void onServiceConnected() {
        getSinchServiceInterface().addMessageClientListener(this);
    }

    @Override
    public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {
        // Left blank intentionally
    }

    @Override
    public void onMessageFailed(MessageClient client, Message message,
                                MessageFailureInfo failureInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sending failed: ")
                .append(failureInfo.getSinchError().getMessage());

        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
        Log.d(TAG, sb.toString());
    }

    @Override
    public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {
        Log.d(TAG, "onDelivered");
    }

    private void sendCurrentLocation(){
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        String message = "Click to see my current location";
        if (!"".equals(message)) {

            getSinchServiceInterface().sendLocation(makeCallTo,message,Lat,Lon);
            //adapter.notifyItemInserted(adapter.getItemCount() - 1); 当有新消息时涮新RecyclerView 中的显示
            msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);//定位到最后一行;
            //inputText.setText("");//清空输入栏；
        }
    }

    private void openMapActivity(){
        Intent MapsActivity = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(MapsActivity);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("adapter",adapter);
        super.onSaveInstanceState(outState);
    }

}
