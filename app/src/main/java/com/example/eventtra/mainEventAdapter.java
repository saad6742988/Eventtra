package com.example.eventtra;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class mainEventAdapter extends RecyclerView.Adapter<mainEventAdapter.viewHolder> {

    ArrayList<MyEvent> list;
    Context context;
    //Constructors of ArrayList and Context

    public mainEventAdapter(ArrayList<MyEvent> list, Context context) {
        this.list = list;
        this.context = context;
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
        holder.mainEventdateView.setText(mainEvent.getStartDate()+"to"+mainEvent.getEndDate());

        holder.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked event", "onClick: "+mainEvent);
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