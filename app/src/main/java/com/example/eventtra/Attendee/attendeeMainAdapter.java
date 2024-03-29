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
import com.example.eventtra.Models.MyEvent;
import com.example.eventtra.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class attendeeMainAdapter extends RecyclerView.Adapter<attendeeMainAdapter.viewHolder>{

    ArrayList<MyEvent> list;
    Context context;
    GlobalData globalData;

    public attendeeMainAdapter(ArrayList<MyEvent> list, Context context) {
        this.list = list;
        this.context = context;
        this.globalData = (GlobalData) context.getApplicationContext();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.attendee_main_card, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        MyEvent mainEvent = list.get(position);
        if(mainEvent.getEventPic()!=null)
        {
            Picasso.get().load(mainEvent.getEventPic()).into(holder.mainEventImageView);
        }
        else{
            holder.mainEventImageView.setImageResource(R.drawable.logo1);
        }
        holder.mainEventNameView.setText(mainEvent.getEventName());
        holder.mainEventDescView.setText(mainEvent.getEventDes());

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(mainEvent.getStartDate().toDate());
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(mainEvent.getEndDate().toDate());
        holder.mainEventdateView.setText(startDate.get(Calendar.DATE)+"-"+startDate.get(Calendar.MONTH)+"-"+startDate.get(Calendar.YEAR)+
                " to "
                +endDate.get(Calendar.DATE)+"-"+endDate.get(Calendar.MONTH)+"-"+endDate.get(Calendar.YEAR));

        holder.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked event", "onClick: "+mainEvent);
                globalData.globalEvent=mainEvent;
                AppCompatActivity act = (AppCompatActivity)v.getContext();
                act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new attendee_sub_events()).addToBackStack("attendeeSubEventList").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView mainEventImageView;
        TextView mainEventNameView;
        TextView mainEventDescView;
        TextView mainEventdateView;
        CardView mainCardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mainEventImageView= itemView.findViewById(R.id.attendeeMainImg);
            mainEventNameView= itemView.findViewById(R.id.attendeeMainName);
            mainEventDescView= itemView.findViewById(R.id.attendeeMainDes);
            mainEventdateView= itemView.findViewById(R.id.attendeeMainDate);
            mainCardView=itemView.findViewById(R.id.attendeeMainCard);


        }
    }
}
