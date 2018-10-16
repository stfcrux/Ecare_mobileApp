package com.example.ecare_client.textchat;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecare_client.ChatActivity;
import com.example.ecare_client.MapsActivity;
import com.sinch.android.rtc.messaging.Message;
import com.example.ecare_client.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> implements Parcelable {

    private List<Pair<Message, Integer>> mMessages;
    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;
    private static final String TAG = "MsgAdapter";
    private Context context;
    private int mData;



    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout lefrLayout;
        LinearLayout rightLayout;

        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view){
            super(view);
            lefrLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);


        }
    }

    public MsgAdapter(Context context){
        mMessages = new ArrayList<Pair<Message, Integer>>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    public void addMessage(Message message, int direction) {
        mMessages.add(new Pair(message, direction));
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair msg = mMessages.get(position);
        int type = getMsgType(position);
        final String content = mMessages.get(position).first.getTextBody();
        if (content.equals("Click to see my current location")){
            Map<String,String> header = mMessages.get(position).first.getHeaders();
            final String lat = header.get("lat");
            final String lon = header.get("lon");
            holder.leftMsg.setFocusable(false);
            holder.rightMsg.setFocusable(false);
            holder.leftMsg.setClickable(true);
            holder.rightMsg.setClickable(true);
            holder.leftMsg.setContentDescription("location");
            holder.rightLayout.setContentDescription("location");
            holder.leftMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,MapsActivity.class);
                    Bundle options = new Bundle();
                    options.putString("lat", lat);
                    options.putString("lon",lon);
                    intent.putExtras(options);
                    context.startActivity(intent);
                }
            });
            holder.rightMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,MapsActivity.class);
                    Bundle options = new Bundle();
                    options.putString("lat", lat);
                    options.putString("lon",lon);
                    intent.putExtras(options);
                    context.startActivity(intent);
                }
            });
            Log.d(TAG, holder.leftMsg.getContentDescription().toString());
        }
        if (type == DIRECTION_INCOMING){
            //如果是收到消息，显示左边布局，隐藏右边布局
            holder.lefrLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(content);
            //holder.leftMsg.setClickable(false);
        } else if (type == DIRECTION_OUTGOING){
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.lefrLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(content);
            //holder.rightMsg.setClickable(false);
        }
    }

    private int getMsgType(int i){
        return mMessages.get(i).second;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_msg:
                case R.id.right_msg:
                    if(v.getContentDescription()!= null && v.getContentDescription().toString().equals("location")){
                        openMapActivity();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private View.OnTouchListener editTextTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_UP:
                    if(v.getContentDescription()!= null && v.getContentDescription().toString().equals("location")){
                        openMapActivity();
                    }
            }
            return false;
        }
    };

    private void openMapActivity(){
        Intent intent = new Intent(context,MapsActivity.class);
        context.startActivity(intent);
        Log.d(TAG, "openMapActivity: ");
    }

    public int describeContents() {
        return 0;
    }

    /** save object in parcel */
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<MsgAdapter> CREATOR
            = new Parcelable.Creator<MsgAdapter>() {
        public MsgAdapter createFromParcel(Parcel in) {
            return new MsgAdapter(in);
        }

        public MsgAdapter[] newArray(int size) {
            return new MsgAdapter[size];
        }
    };

    /** recreate object from parcel */
    private MsgAdapter(Parcel in) {
        mData = in.readInt();
    }
}


