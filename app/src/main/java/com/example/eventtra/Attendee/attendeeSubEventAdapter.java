package com.example.eventtra.Attendee;

import android.content.Context;
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

import com.example.eventtra.AllUsers.GlobalData;
import com.example.eventtra.Models.subEventsModel;
import com.example.eventtra.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class attendeeSubEventAdapter extends RecyclerView.Adapter<attendeeSubEventAdapter.viewHolder> {

    ArrayList<subEventsModel> list;
    Context context;
    GlobalData globalData;

    public attendeeSubEventAdapter(ArrayList<subEventsModel> list, Context context) {
        this.list = list;
        this.context = context;
        globalData = (GlobalData) context.getApplicationContext();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(subEvent.getEventTime().toDate());
        holder.subEventdateView.setText(startDate.get(Calendar.DATE)+"-"+startDate.get(Calendar.MONTH)+"-"+startDate.get(Calendar.YEAR)
                +" At "+startDate.get(Calendar.HOUR_OF_DAY)+":"+startDate.get(Calendar.MINUTE));

        holder.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked event", "onClick: "+subEvent);
                globalData.globalSubEvent=subEvent;
                AppCompatActivity act = (AppCompatActivity)v.getContext();
                act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new attendeeSubEventDetails()).addToBackStack("attendeeSubEventDetails").commit();
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
