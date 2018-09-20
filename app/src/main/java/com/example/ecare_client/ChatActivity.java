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
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity implements MessageClientListener{

    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private Button videochat;
    private RecyclerView msgRecyclerView;
    private String makeCallTo;
    private MsgAdapter adapter;
    private static final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        makeCallTo = "testexample@example.com";

        initMsg();
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        videochat = (Button) findViewById(R.id.video_call);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter();
        msgRecyclerView.setAdapter(adapter);

        videochat.setOnClickListener(buttonClickListener);
        send.setOnClickListener(buttonClickListener);
    }


    private void sendMsg(){
        String content = inputText.getText().toString();
        if (!"".equals(content)) {
            //Msg msg = new Msg(content, Msg.TYPE_SEND);
            //msgList.add(msg);
            getSinchServiceInterface().sendMessage(makeCallTo, content);
            //adapter.notifyItemInserted(adapter.getItemCount() - 1);//当有新消息时涮新RecyclerView 中的显示
           //msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);//定位到最后一行;
            inputText.setText("");//清空输入栏；
        }

    }

    private void initMsg() {
        Msg msg1 = new Msg("Hello guy", Msg.TYPR_RECEIVED);
        Msg msg2 = new Msg("Who is That?", Msg.TYPE_SEND);
        Msg msg3 = new Msg("This is Jerry. Nice talking to you", Msg.TYPR_RECEIVED);

        msgList.add(msg1);
        msgList.add(msg2);
        msgList.add(msg3);

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
    }

    @Override
    public void onMessageSent(MessageClient client, Message message, String recipientId) {
        adapter.addMessage(message, adapter.DIRECTION_OUTGOING);
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


}
