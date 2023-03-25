package com.example.eventtra.ChatRooms;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventtra.GlobalData;
import com.example.eventtra.R;

import java.util.HashMap;


public class chatRoomsList extends Fragment {

    HashMap<String,String> chatRoomsList = new HashMap<>();
    GlobalData globalData;
    private AlertDialog loadingDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_rooms_list, container, false);
        globalData = (GlobalData) getActivity().getApplicationContext();

        // Inflate the layout for this fragment


        if(globalData.globalUser.getRole().equals(getString(R.string.ADMIN)))
        {
            getAdminRooms();
        }
        else if(globalData.globalUser.getRole().equals(getString(R.string.ORGANIZER)))
        {
            getOrganizerRooms();
        }
        else
        {
            getAttendeeRooms();
        }

        return view;
    }

    private void getOrganizerRooms() {
        Toast.makeText(getContext(), "Organizers Room", Toast.LENGTH_SHORT).show();
    }

    private void getAttendeeRooms() {
        Toast.makeText(getContext(), "attendee Room", Toast.LENGTH_SHORT).show();

    }

    private void getAdminRooms() {
        Toast.makeText(getContext(), "admin Room", Toast.LENGTH_SHORT).show();

    }
}