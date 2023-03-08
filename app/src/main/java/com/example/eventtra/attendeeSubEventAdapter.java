package com.example.eventtra;

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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

        holder.subEventNameView.setText(subEvent.getName());
        holder.subEventdateView.setText(subEvent.getSubEventDate()+" At "+subEvent.getSubEventTime());

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
        CardView mainCardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            subEventNameView= itemView.findViewById(R.id.attendeeSubName);
            subEventdateView= itemView.findViewById(R.id.attendeeSubDate);
            mainCardView=itemView.findViewById(R.id.attendeeSubCard);


        }
    }
}
