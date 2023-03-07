package com.example.eventtra;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class attendee_sub_events extends Fragment {

    TextView mainName,mainDes,mainDate;
    GlobalData globalData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_sub_events, container, false);
        mainName = view.findViewById(R.id.mainEventName);
        mainDes = view.findViewById(R.id.mainEventDes);
        mainDate = view.findViewById(R.id.mainEventDate);
        globalData = (GlobalData) getContext().getApplicationContext();
        mainName.setText(globalData.globalEvent.getEventName());
        mainDes.setText(globalData.globalEvent.getEventDes());
        mainDate.setText(globalData.globalEvent.getStartDate()+" to "+globalData.globalEvent.getEndDate());

        return view;
    }
}