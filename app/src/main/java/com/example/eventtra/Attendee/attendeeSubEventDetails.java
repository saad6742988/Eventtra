package com.example.eventtra.Attendee;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventtra.ChatRooms.chatScreen;
import com.example.eventtra.AllUsers.GlobalData;
import com.example.eventtra.R;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;


public class attendeeSubEventDetails extends Fragment {

    TextView subName,subDes,subDate,subPrice,subTime;
    ImageView subImg;
    EditText cnic;
    TextInputLayout cniclayout;
    GlobalData globalData;
    Button subEventEnrollBtn,joinChatRoomBtn;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_attendee_sub_event_details, container, false);
        globalData = (GlobalData) getContext().getApplicationContext();
        subName=view.findViewById(R.id.subDetailName);
        subDes=view.findViewById(R.id.subDetailDes);
        subDate=view.findViewById(R.id.subDetailDate);
        subTime=view.findViewById(R.id.subDetailTime);
        subPrice=view.findViewById(R.id.subDetailPrice);
        subEventEnrollBtn = view.findViewById(R.id.EnrollBtn);
        joinChatRoomBtn = view.findViewById(R.id.joinChatRoomBtn);
        subImg = view.findViewById(R.id.subDetailImg);


        if(globalData.globalSubEvent.getPic()!=null)
        {
            Picasso.get().load(globalData.globalSubEvent.getPic()).into(subImg);
        }
        else{
            subImg.setImageResource(R.drawable.logo1);
        }

        subName.setText(globalData.globalSubEvent.getName());
        subDes.setText(globalData.globalSubEvent.getDesc());
        subDes.setText(globalData.globalSubEvent.getDesc());
        subPrice.setText("Ticket : Rs."+globalData.globalSubEvent.getPrice()+" Per Participant");
        subDate.setText(globalData.globalSubEvent.getSubEventDate());
        subTime.setText(globalData.globalSubEvent.getSubEventTime());
        if(!globalData.globalSubEvent.isOpenRegistration())
        {
            subEventEnrollBtn.setEnabled(false);
            subEventEnrollBtn.setText("Enrollment Closed");
        }
        joinChatRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatScreen fragment = new chatScreen();
                Bundle args = new Bundle();
                args.putString("id", getString(R.string.GeneralRoomId));
                args.putString("name", "General Discussion");
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, fragment).addToBackStack("chatScreen").commit();
            }
        });

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