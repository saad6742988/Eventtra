package com.example.eventtra;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class mainEventList extends Fragment {


    RecyclerView recyclerView;
    GlobalData globalData;
    //pass user data here. this is the array
    ArrayList<MyEvent> mainEventLists = new ArrayList<>();

    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference eventCollection = database.collection("Event");

    private AlertDialog loadingDialog;
    int counterEvent = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_event_list, container, false);
        recyclerView = view.findViewById(R.id.mainEventRecycleView);
        globalData = (GlobalData) getActivity().getApplicationContext();
        mainEventLists.clear();
        getEventsData();


        //setting subEventsAdapter
        mainEventAdapter adapter= new mainEventAdapter(mainEventLists, getContext());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    private void getEventsData() {
        showLoading();

        eventCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                Log.d("Count events", "onSuccess: "+queryDocumentSnapshots.size());
                counterEvent = 0;
               if(queryDocumentSnapshots.size()>0)
               {
                   for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                       MyEvent event = documentSnapshot.toObject(MyEvent.class);
                       event.setEventId(documentSnapshot.getId());


                       //get event Picture
                       StorageReference file = storageReference.child("Event/"+documentSnapshot.getId()+"/event.jpg");
                       file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               Log.d("main get image", "onSuccess: fetch success");
                               if(uri!=null)
                               {
                                   Log.d("Image uri", "onSuccess: "+uri);
                                   event.setEventPic(uri);
                                   mainEventLists.add(event);
                                   counterEvent++;
                                   if(counterEvent==queryDocumentSnapshots.size())
                                   {
                                       populateList();
                                   }
                               }
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                               event.setEventPic(null);
                               Log.d("Error", "onFailure: "+e.getMessage());
                               Log.d("null event", "onFailure: "+event);
                               mainEventLists.add(event);
                               counterEvent++;
                               if(counterEvent==queryDocumentSnapshots.size())
                               {
                                   populateList();
                               }
                           }
                       });

                   }
               }
               else
               {
                   loadingDialog.dismiss();
               }

                Log.d("TAG", "onSuccess: ");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: ");
            }
        });
    }

    private void populateList() {

        Log.d("all Events", "populateList: "+mainEventLists);
        mainEventAdapter adapter= new mainEventAdapter(mainEventLists, getContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        loadingDialog.dismiss();
    }

    private void showLoading() {
        // adding ALERT Dialog builder object and passing activity as parameter
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(true);
        loadingDialog = builder.create();

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int width = (int) (200 * scale + 0.5f);
        int height = (int) (200 * scale + 0.5f);
        loadingDialog.show();
        loadingDialog.getWindow().setLayout(width,height);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
    }
}