package com.example.eventtra;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class attendeeSubEventDetails extends Fragment {

    TextView subName,subDes,subTime,subPrice;
    GlobalData globalData;
    Button subEventEnrollBtn;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_attendee_sub_event_details, container, false);
        globalData = (GlobalData) getContext().getApplicationContext();
        subName=view.findViewById(R.id.subDetailName);
        subDes=view.findViewById(R.id.subDetailDes);
        subTime=view.findViewById(R.id.subDetailDate);
        subPrice=view.findViewById(R.id.subDetailPrice);
        subEventEnrollBtn = view.findViewById(R.id.subEventEnrollBtn);

        subName.setText(globalData.globalSubEvent.getName());
        subDes.setText(globalData.globalSubEvent.getDesc());
        subDes.setText(globalData.globalSubEvent.getDesc());
        subPrice.setText("Ticket : Rs."+globalData.globalSubEvent.getPrice()+" Per Participant");
        subTime.setText("Date&Time : "+globalData.globalSubEvent.getSubEventDate()+" At "+globalData.globalSubEvent.getSubEventTime());

        subEventEnrollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new attendee_event_enrollment()).addToBackStack("attendee_event_enrollment").commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}