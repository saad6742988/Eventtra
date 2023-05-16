package com.example.eventtra.ChatRooms;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventtra.AllUsers.GlobalData;
import com.example.eventtra.Models.ChatRoomModel;
import com.example.eventtra.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoomsListAdapter extends RecyclerView.Adapter<ChatRoomsListAdapter.ViewHolder>{

    ArrayList<ChatRoomModel> chatRoomsList = new ArrayList<>();
    Context context;
    GlobalData globalData;

    public ChatRoomsListAdapter(ArrayList<ChatRoomModel> chatRoomsList, Context context) {
        this.chatRoomsList = chatRoomsList;
        this.context = context;
        this.globalData = (GlobalData) context.getApplicationContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.chatroom_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoomModel room = chatRoomsList.get(position);
        holder.chatRoomName.setText(room.getRoomName());
        if(!room.getMessage().isEmpty())
        {
            holder.chatRoomMessage.setText(room.getMessage());
            Date date=room.getTimeStamp().toDate();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.US);
            String time = format.format(date);
            holder.chatRoomTime.setText(time);
        }
        else
        {
            holder.chatRoomMessage.setText("No Messages Yet!");
            holder.chatRoomTime.setText("");
        }
        holder.chatRoomCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatScreen fragment = new chatScreen();
                Bundle args = new Bundle();
                args.putString("id", room.getRoomId());
                args.putString("name", room.getRoomName());
                fragment.setArguments(args);
                AppCompatActivity act = (AppCompatActivity)view.getContext();
                if(globalData.globalUser.getRole().equals(context.getString(R.string.ADMIN)))
                {
                    act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("chatScreen").commit();
                }
                else if(globalData.globalUser.getRole().equals(context.getString(R.string.ORGANIZER)))
                {
                    act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_organizer, fragment).addToBackStack("chatScreen").commit();
                }
                else
                {
                    act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, fragment).addToBackStack("chatScreen").commit();

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return chatRoomsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView chatRoomName,chatRoomMessage,chatRoomTime;
        CardView chatRoomCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatRoomName = itemView.findViewById(R.id.chatRoomName);
            chatRoomMessage = itemView.findViewById(R.id.chatRoomMessage);
            chatRoomTime = itemView.findViewById(R.id.chatRoomTime);
            chatRoomCard = itemView.findViewById(R.id.chatRoomCard);
        }
    }
}
