package com.example.eventtra.Attendee;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventtra.AllUsers.GlobalData;
import com.example.eventtra.Models.PaymentInfo;
import com.example.eventtra.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.eventtra.Models.subEventsModel;


public class liveStreamList extends Fragment {


    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference paymentCollection = database.collection("Payments");
    final private CollectionReference subEventCollection = database.collection("SubEvent");
    HashMap<String,String> userRegisteredEvents = new HashMap<String,String>();
    ArrayList<subEventsModel> subEventsModelArrayList =new ArrayList<subEventsModel>();
    RecyclerView attendeeLiveStreamRecyclerView;
    GlobalData globalData;
    AlertDialog.Builder builder;
    private AlertDialog loadingDialog;
    Context context;
    LiveStreamListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_stream_list, container, false);
        globalData = (GlobalData) getActivity().getApplicationContext();
        context = getActivity().getApplicationContext();
        attendeeLiveStreamRecyclerView = view.findViewById(R.id.attendeeLiveStreamRecyclerView);
//        subEventsModelArrayList.clear();
//        userRegisteredEvents.clear();

        // adding ALERT Dialog builder object and passing activity as parameter
         builder = new AlertDialog.Builder(getActivity(),R.style.CustomAlertDialog);


        adapter= new LiveStreamListAdapter(subEventsModelArrayList, getContext());
        attendeeLiveStreamRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        attendeeLiveStreamRecyclerView.setLayoutManager(layoutManager);

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater loadingInflater = getActivity().getLayoutInflater();
        builder.setView(loadingInflater.inflate(R.layout.loading, null));
        builder.setCancelable(true);


        getUserRegisteredEvents();
        Log.d("live stream subevents list", subEventsModelArrayList.toString());
        return view;
    }

    private void showLoading() {
        loadingDialog = builder.create();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int width = (int) (200 * scale + 0.5f);
        int height = (int) (200 * scale + 0.5f);
        loadingDialog.show();
        loadingDialog.getWindow().setLayout(width,height);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
    }
    private void getUserRegisteredEvents() {
//        showLoading();
        subEventsModelArrayList.clear();
        userRegisteredEvents.clear();
        paymentCollection.whereEqualTo("madeBy",globalData.globalUser.getUserId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (error!=null)
                    return;

                // Handle document changes (added, modified, or removed)
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            PaymentInfo paymentInfo= dc.getDocument().toObject(PaymentInfo.class);
                            userRegisteredEvents.put(paymentInfo.getSubEventID(),paymentInfo.getSubEventName());
                            break;
                        case MODIFIED:
                            // Handle modified document

                            break;
                        case REMOVED:
                            // Handle removed document
                            break;
                    }
                }
                int size = userRegisteredEvents.size();
                int eventCounter=0;
                for (Map.Entry<String, String> event : userRegisteredEvents.entrySet()) {
                    String key = event.getKey();
                    String value = event.getValue();
                    Log.d("user events", "Key=" + key + ", Value=" + value);
                    eventCounter++;
                    int finalEventCounter = eventCounter;
                    subEventCollection.document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult().exists())
                            {
                                subEventsModel subEvent = task.getResult().toObject(subEventsModel.class);
                                subEvent.setSubEventId(task.getResult().getId());
                                if(!eventPassed(subEvent.getEventTime())) {
                                    Log.d("testevent", "onComplete: "+subEvent.getName());
                                    StorageReference file = storageReference.child("SubEvent/" + subEvent.getSubEventId() + "/subevent.jpg");
                                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            if (uri != null) {
                                                subEvent.setPic(uri);
                                                subEventsModelArrayList.add(subEvent);
//                                            if (finalEventCounter == size) {
//                                                populateList();
//                                            }
                                                adapter.notifyItemInserted(subEventsModelArrayList.size() - 1);

                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            subEvent.setPic(null);
                                            subEventsModelArrayList.add(subEvent);
//                                        if (finalEventCounter == size) {
//                                            populateList();
//                                        }
                                            adapter.notifyItemInserted(subEventsModelArrayList.size() - 1);
                                        }
                                    });
                                }
                            }
                        }
                    });
                }

            }
        });
    }

    private void populateList() {
        LiveStreamListAdapter adapter= new LiveStreamListAdapter(subEventsModelArrayList, getContext());
        attendeeLiveStreamRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        attendeeLiveStreamRecyclerView.setLayoutManager(layoutManager);
        loadingDialog.dismiss();
    }

    private boolean eventPassed(Timestamp eventTime) {
        if (eventTime.compareTo(Timestamp.now()) < 0) {
            Log.d("testevent", "eventPassed: true "+eventTime.toDate().toString()+" : "+Timestamp.now().toDate().toString());
            return true;
        } else if (eventTime.compareTo(Timestamp.now()) > 0) {
            Log.d("testevent", "eventPassed: false "+eventTime.toDate().toString()+" : "+Timestamp.now().toDate().toString());
            return false;
        } else {
            Log.d("testevent", "eventPassed: false "+eventTime.toDate().toString()+" : "+Timestamp.now().toDate().toString());
            return false;
        }
    }
}