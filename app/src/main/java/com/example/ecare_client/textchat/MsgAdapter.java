package com.example.ecare_client.textchat;



import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sinch.android.rtc.messaging.Message;
import com.example.ecare_client.R;

import java.util.ArrayList;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<Pair<Message, Integer>> mMessages;
    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;


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

    public MsgAdapter(){
        mMessages = new ArrayList<Pair<Message, Integer>>();
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
        String content = mMessages.get(position).first.getTextBody();
        if (type == DIRECTION_INCOMING){
            //如果是收到消息，显示左边布局，隐藏右边布局
            holder.lefrLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(content);
        } else if (type == DIRECTION_OUTGOING){
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.lefrLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(content);
        }
    }

    private int getMsgType(int i){
        return mMessages.get(i).second;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
