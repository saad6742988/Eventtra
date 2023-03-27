package com.example.eventtra.ChatRooms;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventtra.GlobalData;
import com.example.eventtra.MyEvent;
import com.example.eventtra.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    ArrayList<MessageModel> messages;
    Context context;
    GlobalData globalData;

    public MessageAdapter(ArrayList<MessageModel> messages, Context context, GlobalData globalData) {
        this.messages = messages;
        this.context = context;
        this.globalData = globalData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.message_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModel message = messages.get(position);
        holder.messageText.setText(message.getMessage());
        holder.emailText.setText(message.getEmail());

        Date date=message.getTimeStamp().toDate();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.US);
        String time = format.format(date);
        holder.timeText.setText(time);

        if(message.getEmail().equals(globalData.globalUser.getEmail()))
        {
            holder.messageLayout.setGravity(Gravity.RIGHT);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout messageLayout;
        TextView messageText,emailText,timeText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageLayout =itemView.findViewById(R.id.messageLayout);
            messageText=itemView.findViewById(R.id.messageText);
            emailText=itemView.findViewById(R.id.emailText);
            timeText=itemView.findViewById(R.id.timeText);
        }
    }
}
