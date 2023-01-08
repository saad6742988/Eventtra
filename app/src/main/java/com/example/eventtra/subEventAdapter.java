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

public class subEventAdapter extends RecyclerView.Adapter<subEventAdapter.viewHolder>{


    ArrayList<subEventsModel> list;
    Context context;
    GlobalData globalData;
    //Constructors of ArrayList and Context

    public subEventAdapter(ArrayList<subEventsModel> list, Context context) {
        this.list = list;
        this.context = context;
        globalData = (GlobalData) context.getApplicationContext();
    }

    //by default 3 methods
    @NonNull
    @Override

    //will be used for layout only
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sub_event_card, parent, false);

        return new viewHolder(view);
    }

    //will be used for setting values
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        subEventsModel subEvent = list.get(position);

        if(subEvent.getPic()!=null)
        {
            Picasso.get().load(subEvent.getPic()).into(holder.subImageView);
        }
        else{
            holder.subImageView.setImageResource(R.drawable.logo1);
        }
        holder.subNameView.setText(subEvent.getName());
        if(subEvent.getDesc().equals(""))
        {
            holder.subDescView.setText("Add Description");
        }
        else
        {
            holder.subDescView.setText(subEvent.getDesc());
        }
        if(subEvent.getPrice().equals(""))
        {
            holder.subPriceView.setText("Add Price");
        }
        else
        {
            holder.subPriceView.setText(subEvent.getPrice());
        }


        holder.subCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked event", "onClick: "+subEvent);
                globalData.globalSubEvent=subEvent;
                AppCompatActivity act = (AppCompatActivity)v.getContext();
                act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_organizer, new org_subEvent_edit()).addToBackStack("addEventDetails").commit();
            }
        });
    }

    //recylerView list size, to stop recycler view at max size
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView subImageView;
        TextView subNameView;
        TextView subDescView;
        TextView subPriceView;
        CardView subCardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            subImageView= itemView.findViewById(R.id.subImageView);
            subNameView= itemView.findViewById(R.id.subNameView);
            subDescView= itemView.findViewById(R.id.subDescView);
            subPriceView= itemView.findViewById(R.id.subPriceView);
            subCardView=itemView.findViewById(R.id.subRecyclerView);
        }
    }


}
