package com.example.eventtra.ChatRooms;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.eventtra.GlobalData;
import com.example.eventtra.MyEvent;
import com.example.eventtra.R;
import com.example.eventtra.attendeeMainAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class chatScreen extends Fragment {

    RecyclerView messageRecycleView;
    EditText newMessageBox;
    ImageButton messageSendBtn;
    GlobalData globalData;
    ArrayList<MessageModel> messageList = new ArrayList<>();


    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference chatRoomsCollection = database.collection("ChatRooms");
    private CollectionReference chatMessagesCollection = chatRoomsCollection.
            document("thEg7DGkDfOiufL0619H").
            collection("messages");

    private AlertDialog loadingDialog;
    int counterMessages = 0;
    MessageAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_screen, container, false);
        globalData = (GlobalData) getActivity().getApplicationContext();
        adapter= new MessageAdapter(messageList, getContext(),globalData);
        messageRecycleView=view.findViewById(R.id.messageRecycleView);
        newMessageBox=view.findViewById(R.id.newMessageBox);
        messageSendBtn=view.findViewById(R.id.messageSendBtn);
        messageList.clear();
//        getMessages();

        messageRecycleView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        messageRecycleView.setLayoutManager(layoutManager);



        messageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMessage();
            }
        });

        chatMessagesCollection.orderBy("timeStamp", Query.Direction.ASCENDING).addSnapshotListener(MetadataChanges.INCLUDE,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {


                if (error!=null)
                    return;

                // Handle document changes (added, modified, or removed)
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            MessageModel message= dc.getDocument().toObject(MessageModel.class);
                            if(message.getTimeStamp()!=null) {
                                message.setMessageId(dc.getDocument().getId());
                                Log.d("message from db", "onEvent: " + message);
                                messageList.add(message);
                                adapter.notifyItemInserted(messageList.size()-1);
                                messageRecycleView.scrollToPosition(messageList.size() - 1);
                            }
                            break;
                        case MODIFIED:
                            // Handle modified document
                            message= dc.getDocument().toObject(MessageModel.class);
                            message.setMessageId(dc.getDocument().getId());
                            Log.d("message from db", "onEvent: "+message);
                            messageList.add(message);
                            adapter.notifyItemInserted(messageList.size()-1);
                            messageRecycleView.scrollToPosition(messageList.size()-1);
                            break;
                        case REMOVED:
                            // Handle removed document
                            break;
                    }
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void postMessage() {
        if(!newMessageBox.getText().toString().equals("")){

            HashMap<String,Object> addMessage = new HashMap<>();
            addMessage.put("message", newMessageBox.getText().toString());
            addMessage.put("email", globalData.globalUser.getEmail());
            addMessage.put("timeStamp", FieldValue.serverTimestamp());
//            MessageModel message = new MessageModel(newMessageBox.getText().toString(),globalData.globalUser.getEmail());
            chatMessagesCollection.add(addMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getContext(), "message sent", Toast.LENGTH_SHORT).show();
                    newMessageBox.setText("");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "message not sent ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getMessages()
    {
        showLoading();
        chatMessagesCollection.orderBy("timeStamp", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("Count messages", "onSuccess: "+queryDocumentSnapshots.size());
                counterMessages = 0;
                if(queryDocumentSnapshots.size()>0)
                {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                    {
                        MessageModel message = documentSnapshot.toObject(MessageModel.class);
                        message.setMessageId(documentSnapshot.getId());


                        messageList.add(message);
                        counterMessages++;
                        if (counterMessages == queryDocumentSnapshots.size()) {
                            populateList();
                        }
                    }
                }
                else {
                    loadingDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismiss();
            }
        });
    }
    private void populateList() {

        Log.d("all messsages", "populateList: "+messageList);

        adapter= new MessageAdapter(messageList, getContext(),globalData);

        messageRecycleView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        messageRecycleView.setLayoutManager(layoutManager);
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