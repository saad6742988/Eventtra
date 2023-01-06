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
        View view= LayoutInflater.from(context).inflate(R.layout.activity_sub_event_list, parent, false);

        return new viewHolder(view);
    }

    //will be used for setting values
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        subEventsModel model = list.get(position);

        holder.imageView.setImageResource(model.getPic());
        holder.textView.setText(model.getText());
        holder.textView2.setText(model.getDesc());
        holder.textView3.setText(model.getPrice());
    }

    //recylerView list size, to stop recycler view at max size
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        TextView textView2;
        TextView textView3;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.subImageView);
            textView= itemView.findViewById(R.id.subNameView);
            textView2= itemView.findViewById(R.id.subDescView);
            textView3= itemView.findViewById(R.id.subPriceView);
        }
    }


}
