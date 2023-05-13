package com.example.eventtra.Attendee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventtra.AttendeePage;
import com.example.eventtra.GlobalData;
import com.example.eventtra.R;
import com.example.eventtra.attendeeSubEventAdapter;
import com.example.eventtra.attendeeSubEventDetails;
import com.example.eventtra.subEventsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LiveStreamListAdapter extends RecyclerView.Adapter<LiveStreamListAdapter.viewHolder> {
    ArrayList<subEventsModel> list;
    Context context;
    GlobalData globalData;

    public LiveStreamListAdapter(ArrayList<subEventsModel> list, Context context) {
        this.list = list;
        this.context = context;
        globalData = (GlobalData) context.getApplicationContext();
    }

    @NonNull
    @Override
    public LiveStreamListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.attendee_seb_event_card, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        subEventsModel subEvent = list.get(position);

        if(subEvent.getPic()!=null)
        {
            Picasso.get().load(subEvent.getPic()).into(holder.attendeeSubImg);
        }
        else{
            holder.attendeeSubImg.setImageResource(R.drawable.logo1);
        }
        holder.subEventNameView.setText(subEvent.getName());
        holder.subEventdateView.setText(subEvent.getSubEventDate()+" At "+subEvent.getSubEventTime());

        holder.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open live stream screen
                Intent intent = new Intent(v.getContext(),LiveStreamView.class);
                intent.putExtra("streamLink", "https://www.youtube.com/embed/ahCq355hpaY");
                AppCompatActivity act = (AppCompatActivity)v.getContext();
                act.startActivity(intent);
//                act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, fragment).addToBackStack("attendeeSubEventDetails").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView subEventNameView;
        TextView subEventdateView;
        ImageView attendeeSubImg;
        CardView mainCardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            subEventNameView= itemView.findViewById(R.id.attendeeSubName);
            subEventdateView= itemView.findViewById(R.id.attendeeSubDate);
            mainCardView=itemView.findViewById(R.id.attendeeSubCard);
            attendeeSubImg=itemView.findViewById(R.id.attendeeSubimageView);


        }
    }
}
