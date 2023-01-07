package com.example.eventtra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class subEventAdapter extends RecyclerView.Adapter<subEventAdapter.viewHolder>{


    ArrayList<subEventsModel> list;
    Context context;
    //Constructors of ArrayList and Context

    public subEventAdapter(ArrayList<subEventsModel> list, Context context) {
        this.list = list;
        this.context = context;
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
        subEventsModel model = list.get(position);

        holder.subImageView.setImageResource(model.getP());
        holder.subNameView.setText(model.getName());
        holder.subDescView.setText(model.getDesc());
        holder.subPriceView.setText(model.getPrice());
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
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            subImageView= itemView.findViewById(R.id.subImageView);
            subNameView= itemView.findViewById(R.id.subNameView);
            subDescView= itemView.findViewById(R.id.subDescView);
            subPriceView= itemView.findViewById(R.id.subPriceView);
        }
    }


}
