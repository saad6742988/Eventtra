package com.example.eventtra;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class addEventdetails extends Fragment {



    private Button nextBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_eventdetails, container, false);
//        nextBtn=view.findViewById(R.id.nextBtn);
//
//
//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new addSubeventsAndHeads()).addToBackStack(null).commit();
//            }
//        });
        return view;
    }
}