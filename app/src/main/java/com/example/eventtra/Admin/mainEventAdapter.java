package com.example.eventtra.Admin;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventtra.AllUsers.GlobalData;
import com.example.eventtra.Models.MyEvent;
import com.example.eventtra.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;


public class mainEventAdapter extends RecyclerView.Adapter<mainEventAdapter.viewHolder> {

    ArrayList<MyEvent> list;
    Context context;
    GlobalData globalData;
    //Constructors of ArrayList and Context

    public mainEventAdapter(ArrayList<MyEvent> list, Context context) {
        this.list = list;
        this.context = context;
        globalData = (GlobalData) context.getApplicationContext();
    }

    //by default 3 methods
    @NonNull
    @Override

    //will be used for layout only
    public mainEventAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.main_event_card, parent, false);

        return new mainEventAdapter.viewHolder(view);
    }

    //will be used for setting values
    @Override
    public void onBindViewHolder(@NonNull mainEventAdapter.viewHolder holder, int position) {
        MyEvent mainEvent = list.get(position);
//
//        holder.mainEventImageView.setImageResource(model.getP());
//        holder.subNameView.setText(model.getName());
//        holder.subDescView.setText(model.getDesc());
//        holder.subPriceView.setText(model.getPrice());
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
                act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new editEventDetails()).addToBackStack("addEventDetails").commit();
            }
        });
    }

    //recylerView list size, to stop recycler view at max size
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
            mainEventImageView= itemView.findViewById(R.id.mainEventImageView);
            mainEventNameView= itemView.findViewById(R.id.mainEventNameView);
            mainEventDescView= itemView.findViewById(R.id.mainEventDescView);
            mainEventdateView= itemView.findViewById(R.id.mainEventdateView);
            mainCardView=itemView.findViewById(R.id.mainRecyclerViewCard);


        }
    }

}