package com.example.eventtra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class subEvent_list extends AppCompatActivity {

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_event_card);
        recyclerView = findViewById(R.id.subRecyclerView);



        //pass user data here. this is the array
        ArrayList <subEventsModel> list = new ArrayList<>();


        list.add(new subEventsModel(R.drawable.eventtemp1, "Barbque Dinner", "Description", "Price"));
        list.add(new subEventsModel(R.drawable.eventtemp1, "Barbque Dinner", "Description" , "Price"));

        //setting subEventsAdapter
        subEventAdapter adapter= new subEventAdapter(list, this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


}