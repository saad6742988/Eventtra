package com.example.eventtra.ChatRooms;

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
import android.widget.Toast;

import com.example.eventtra.AllUsers.GlobalData;
import com.example.eventtra.Models.ChatRoomModel;
import com.example.eventtra.Models.MessageModel;
import com.example.eventtra.Models.PaymentInfo;
import com.example.eventtra.R;
import com.example.eventtra.Models.subEventsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;


public class chatRoomsList extends Fragment {


    RecyclerView chatsListRecyclerView;
    ArrayList<ChatRoomModel> chatRoomsList = new ArrayList<>();
    HashMap<String,String> attendeeTemp = new HashMap<>();

    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference subEventCollection = database.collection("SubEvent");
    final private CollectionReference paymentsCollection = database.collection("Payments");
    final private CollectionReference chatRoomsCollection = database.collection("ChatRooms");
    GlobalData globalData;
    private AlertDialog loadingDialog;
    ChatRoomsListAdapter adapter;
    int counter = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_rooms_list, container, false);
        globalData = (GlobalData) getActivity().getApplicationContext();
        chatsListRecyclerView=view.findViewById(R.id.chatsListRecyclerView);
        // Inflate the layout for this fragment
        adapter= new ChatRoomsListAdapter(chatRoomsList, getContext());
        chatsListRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatsListRecyclerView.setLayoutManager(layoutManager);
        chatRoomsList.clear();

        if(globalData.globalUser.getRole().equals(getString(R.string.ADMIN)))
        {
            setManagementListener();
//            getAdminRooms();
        }
        else if(globalData.globalUser.getRole().equals(getString(R.string.ORGANIZER)))
        {
            setManagementListener();
//            getOrganizerRooms();
        }
        else
        {
            setAttendeeListener();
        }

        return view;
    }

    private void setManagementListener() {
        ChatRoomModel room = new ChatRoomModel(getString(R.string.ManagementRoomId),"Management Discussion");
        chatRoomsCollection.document(getString(R.string.ManagementRoomId)).collection("messages").
                orderBy("timeStamp", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()>0)
                        {
                            for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                MessageModel message = document.toObject(MessageModel.class);
                                room.setMessage(message.getMessage());
                                room.setTimeStamp(message.getTimeStamp());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
        chatRoomsList.add(room);
        adapter.notifyItemInserted(chatRoomsList.size()-1);
        ChatRoomModel GeneralRoom = new ChatRoomModel(getString(R.string.GeneralRoomId),"General Discussion");
        chatRoomsCollection.document(getString(R.string.GeneralRoomId)).collection("messages").
                orderBy("timeStamp", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()>0)
                        {
                            for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                MessageModel message = document.toObject(MessageModel.class);
                                GeneralRoom.setMessage(message.getMessage());
                                GeneralRoom.setTimeStamp(message.getTimeStamp());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
        chatRoomsList.add(GeneralRoom);
        adapter.notifyItemInserted(chatRoomsList.size()-1);
        subEventCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Handle document changes (added, modified, or removed)
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            subEventsModel subEventsModel= dc.getDocument().toObject(subEventsModel.class);
                            subEventsModel.setSubEventId(dc.getDocument().getId());
                            ChatRoomModel room = new ChatRoomModel(dc.getDocument().getId(),subEventsModel.getName());
                            //get first document
                            chatRoomsCollection.document(dc.getDocument().getId()).collection("messages").
                                    orderBy("timeStamp", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if(queryDocumentSnapshots.size()>0)
                                            {
                                                for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                                    MessageModel message = document.toObject(MessageModel.class);
                                                    room.setMessage(message.getMessage());
                                                    room.setTimeStamp(message.getTimeStamp());
                                                    adapter.notifyDataSetChanged();

                                                }
                                            }
                                        }
                                    });

                            Log.d("check rooms", "onSuccess: "+room);
                            if(globalData.globalUser.getRole().equals(getString(R.string.ADMIN)))
                            {
                                chatRoomsList.add(room);
                                adapter.notifyItemInserted(chatRoomsList.size()-1);
                            }
                            else
                            {
                                if(globalData.globalUser.getUserId().equals(subEventsModel.getHead()))
                                {
                                    chatRoomsList.add(room);
                                    adapter.notifyItemInserted(chatRoomsList.size()-1);
                                }
                            }
                            break;
                        case MODIFIED:
                            // Handle modified document
                            break;
                        case REMOVED:
                            // Handle removed document
                            break;
                    }
                }
            }
        });
    }

//    private void getOrganizerRooms() {
//        Toast.makeText(getContext(), "Organizers Room", Toast.LENGTH_SHORT).show();
//        ChatRoomModel room = new ChatRoomModel(getString(R.string.ManagementRoomId),"Management Room");
//        chatRoomsList.add(room);
//        adapter.notifyItemInserted(chatRoomsList.size()-1);
//
//        subEventCollection.whereEqualTo("head",globalData.globalUser.getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if(queryDocumentSnapshots.size()>0)
//                {
//                    for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
//                    {
//                        subEventsModel subEventsModel = documentSnapshot.toObject(subEventsModel.class);
//                        subEventsModel.setSubEventId(documentSnapshot.getId());
//                        ChatRoomModel room = new ChatRoomModel(documentSnapshot.getId(),subEventsModel.getName());
//                        Log.d("check rooms", "onSuccess: "+room);
//                        chatRoomsList.add(room);
//                        adapter.notifyItemInserted(chatRoomsList.size()-1);
////                        counter++;
//                    }
//                }
//                else
//                {
//                    loadingDialog.dismiss();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//    }

    private void setAttendeeListener() {
        Toast.makeText(getContext(), "attendee Room", Toast.LENGTH_SHORT).show();
        ChatRoomModel GeneralRoom = new ChatRoomModel(getString(R.string.GeneralRoomId),"General Discussion");
        chatRoomsCollection.document(getString(R.string.GeneralRoomId)).collection("messages").
                orderBy("timeStamp", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()>0)
                        {
                            for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                MessageModel message = document.toObject(MessageModel.class);
                                GeneralRoom.setMessage(message.getMessage());
                                GeneralRoom.setTimeStamp(message.getTimeStamp());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
        chatRoomsList.add(GeneralRoom);
        adapter.notifyItemInserted(chatRoomsList.size()-1);
        paymentsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Handle document changes (added, modified, or removed)
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            PaymentInfo paymentInfo= dc.getDocument().toObject(PaymentInfo.class);
                            paymentInfo.setId(dc.getDocument().getId());
                            if(!attendeeTemp.containsKey(paymentInfo.getSubEventID()))
                            {
                                attendeeTemp.put(paymentInfo.getSubEventID(),paymentInfo.getSubEventName());
                                ChatRoomModel room = new ChatRoomModel(paymentInfo.getSubEventID(),paymentInfo.getSubEventName());
                                chatRoomsList.add(room);
                                adapter.notifyItemInserted(chatRoomsList.size()-1);
                            }
                            break;
                        case MODIFIED:
                            // Handle modified document
                            break;
                        case REMOVED:
                            // Handle removed document
                            break;
                    }
                }
            }
        });
    }

    private void getAdminRooms() {
        Toast.makeText(getContext(), "admin Room", Toast.LENGTH_SHORT).show();
        ChatRoomModel room = new ChatRoomModel(getString(R.string.ManagementRoomId),"Management Room");
        chatRoomsList.add(room);
        adapter.notifyItemInserted(chatRoomsList.size()-1);

        subEventCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0)
                {
                    for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                    {
                        subEventsModel subEventsModel = documentSnapshot.toObject(subEventsModel.class);
                        subEventsModel.setSubEventId(documentSnapshot.getId());
                        ChatRoomModel room = new ChatRoomModel(documentSnapshot.getId(),subEventsModel.getName());
                        Log.d("check rooms", "onSuccess: "+room);
                        chatRoomsList.add(room);
                        adapter.notifyItemInserted(chatRoomsList.size()-1);
//                        counter++;
                    }
                }
                else
                {
                    loadingDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
}