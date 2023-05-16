package com.example.eventtra.Attendee;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventtra.Admin.adminEventRequests;
import com.example.eventtra.AllUsers.FCMSend;
import com.example.eventtra.AllUsers.GlobalData;
import com.example.eventtra.Models.EventRequestModel;
import com.example.eventtra.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class EventRequestAdapter extends RecyclerView.Adapter<EventRequestAdapter.viewHolder>{

    ArrayList<EventRequestModel> eventRequestModelArrayList;
    Context context;
    GlobalData globalData;
    HashMap<String,String> userData = new HashMap<>();
    HashMap<String,String> userDeviceTokens = new HashMap<>();
    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference eventRequestsCollection = database.collection("EventRequests");

    public EventRequestAdapter(ArrayList<EventRequestModel> eventRequestModelArrayList, Context context, HashMap<String, String> userData, HashMap<String, String> userDeviceTokens) {
        this.eventRequestModelArrayList = eventRequestModelArrayList;
        this.context = context;
        this.globalData = (GlobalData) context.getApplicationContext();
        this.userData = userData;
        this.userDeviceTokens=userDeviceTokens;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.event_request_list_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        EventRequestModel request = eventRequestModelArrayList.get(position);
        Log.d("Payment data", "onBindViewHolder: "+request);

        holder.requestEventName.setText(request.getRequestName());
        holder.requestByName.setText(userData.get(request.getUserID()));
        if(request.getRequestStatus().equals("Pending")) {
            holder.requestStatus.setText("Pending");
            holder.requestStatus.setTextColor(Color.BLUE);
        }
        else if(request.getRequestStatus().equals("Rejected")){
            holder.requestStatus.setText("Rejected");
            holder.requestStatus.setTextColor(Color.RED);
        }
        else if(request.getRequestStatus().equals("Canceled")){
            holder.requestStatus.setText("Canceled");
            holder.requestStatus.setTextColor(Color.RED);
        }
        else
        {
            holder.requestStatus.setText("Accepted");
            holder.requestStatus.setTextColor(Color.GREEN);
        }

        holder.requestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetails(request);
            }
        });
    }

    private void showDetails(EventRequestModel request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.CustomAlertDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.event_request_detail_card, null);
        builder.setView(view);
        final TextView requestDetailName = view.findViewById(R.id.requestDetailName);
        requestDetailName.setText(request.getRequestName());

        final TextView requestDetailDes = view.findViewById(R.id.requestDetailDes);
        requestDetailDes.setText(request.getRequestDes());

        final TextView requestDetailByName = view.findViewById(R.id.requestDetailByName);
        requestDetailByName.setText(userData.get(request.getUserID()));
        Button requestAcceptBtn=view.findViewById(R.id.requestAcceptBtn);
        Button requestRejectBtn=view.findViewById(R.id.requestRejectBtn);
        if(globalData.globalUser.getRole().equals("attendee")){
            requestRejectBtn.setVisibility(View.GONE);
            requestAcceptBtn.setVisibility(View.GONE);
        }
        Button requestCancelBtn=view.findViewById(R.id.requestCancelBtn);
        if(globalData.globalUser.getRole().equals("admin")){
            requestCancelBtn.setVisibility(View.GONE);
        }
        final TextView requestDetailStatus = view.findViewById(R.id.requestDetailStatus);
        if(request.getRequestStatus().equals("Pending")) {
            requestDetailStatus.setText("Pending");
            requestDetailStatus.setTextColor(Color.BLUE);
        }
        else if(request.getRequestStatus().equals("Rejected")){
            requestDetailStatus.setText("Rejected");
            requestDetailStatus.setTextColor(Color.RED);
            requestRejectBtn.setEnabled(false);
            requestCancelBtn.setEnabled(false);
        }
        else if(request.getRequestStatus().equals("Canceled")){
            requestDetailStatus.setText("Canceled");
            requestDetailStatus.setTextColor(Color.RED);
            requestCancelBtn.setEnabled(false);
        }
        else
        {
            requestDetailStatus.setText("Accepted");
            requestDetailStatus.setTextColor(Color.GREEN);
            requestAcceptBtn.setEnabled(false);
            requestCancelBtn.setEnabled(false);
        }
        AlertDialog alertDialog = builder.create();
        requestCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> updateRequest = new HashMap<>();
                updateRequest.put("requestStatus","Canceled");
                eventRequestsCollection.document(request.getRequestId()).update(updateRequest);
                alertDialog.dismiss();
                AppCompatActivity act = (AppCompatActivity)v.getContext();
                act.getSupportFragmentManager().popBackStack();
                act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new attendeeEventRequest()).addToBackStack("attendeeEventRequest").commit();
            }
        });
        requestAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> updateRequest = new HashMap<>();
                updateRequest.put("requestStatus","Accepted");
                eventRequestsCollection.document(request.getRequestId()).update(updateRequest);
                //sending Accept Notification
                FCMSend.pushNotification(
                        context,
                        userDeviceTokens.get(request.getUserID()),
                        "Event Request",
                        "Your Request for "+request.getRequestName()+" has Accepted",
                        "MianActivity","Event Request"
                );

                alertDialog.dismiss();
                AppCompatActivity act = (AppCompatActivity)v.getContext();
                act.getSupportFragmentManager().popBackStack();
                act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new adminEventRequests()).addToBackStack("adminEventRequests").commit();
            }
        });
        requestRejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> updateRequest = new HashMap<>();
                updateRequest.put("requestStatus","Rejected");
                eventRequestsCollection.document(request.getRequestId()).update(updateRequest);
                //sending Reject Notification
                FCMSend.pushNotification(
                        context,
                        userDeviceTokens.get(request.getUserID()),
                        "Event Request",
                        "Your Request for "+request.getRequestName()+" has Rejected",
                        "MianActivity","Event Request"
                );

                alertDialog.dismiss();
                AppCompatActivity act = (AppCompatActivity)v.getContext();
                act.getSupportFragmentManager().popBackStack();
                act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new adminEventRequests()).addToBackStack("adminEventRequests").commit();
            }
        });




        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public int getItemCount() {
        return eventRequestModelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView requestEventName;
        TextView requestByName;
        TextView requestStatus;
        CardView requestCard;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            requestEventName= itemView.findViewById(R.id.requestEventName);
            requestByName= itemView.findViewById(R.id.requestByName);
            requestStatus= itemView.findViewById(R.id.requestStatus);
            requestCard=itemView.findViewById(R.id.requestCard);


        }
    }
}
