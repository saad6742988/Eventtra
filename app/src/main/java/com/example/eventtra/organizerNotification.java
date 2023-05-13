package com.example.eventtra;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class organizerNotification extends Fragment {


    Spinner eventsDropDown;
    TextInputLayout notificationTitleLayout,notificationDesLayout;
    EditText notificationTitle,notificationDes;
    Button sendNotificationBtn;
    private String spinnerSelectedTopic="All";



    GlobalData globalData;
    //pass user data here. this is the array
    ArrayList<subEventsModel> subEventList = new ArrayList<>();
    ArrayList<String> subEventNames = new ArrayList<>();
    Context context;


    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference subEventCollection = database.collection("SubEvent");
    int counterEvent = 0;
    private AlertDialog loadingDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_organizer_notification, container, false);
        globalData = (GlobalData) getActivity().getApplicationContext();
        context=getContext();
        eventsDropDown=view.findViewById(R.id.eventsDropDown);
        notificationTitleLayout = view.findViewById(R.id.notificationTitleLayout);
        notificationTitle = view.findViewById(R.id.notificationTitle);
        notificationDesLayout = view.findViewById(R.id.notificationDesLayout);
        notificationDes = view.findViewById(R.id.notificationDes);
        subEventNames.add("All");
        getSubEvents();

        sendNotificationBtn=view.findViewById(R.id.sendNotification);
        sendNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        eventsDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i-1>=0)
                {
                    Log.d("event DropDown", ""+i+" "+subEventList.get(i-1).getName());
                    spinnerSelectedTopic = subEventList.get(i-1).getName()+"_"+subEventList.get(i-1).getSubEventId();
                }
                else
                {
                    Log.d("event DropDown", "All");
                    spinnerSelectedTopic="All";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    private void sendNotification() {
        String title = notificationTitle.getText().toString();
        String des = notificationDes.getText().toString();
        if(title.equals(""))
        {
            notificationTitleLayout.setError("Title Required");
            notificationTitleLayout.requestFocus();
        }
        else if(des.equals(""))
        {
            notificationDesLayout.setError("Message Required");
            notificationDesLayout.requestFocus();

        }
        else
        {
            FCMSend.pushNotification(context,
                    "/topics/"+spinnerSelectedTopic,
                    title,des,"MainActivity","Custom notification");
            notificationTitle.setText("");
            notificationDes.setText("");
        }
    }

    private void getSubEvents() {
        showLoading();
        subEventCollection.whereEqualTo("head",globalData.globalUser.getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0)
                {
                    for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                        subEventsModel event = documentSnapshot.toObject(subEventsModel.class);
                        event.setSubEventId(documentSnapshot.getId());
                        subEventList.add(event);
                        subEventNames.add(event.getName());
                        counterEvent++;
                        if(counterEvent==queryDocumentSnapshots.size())
                        {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,subEventNames);
                            eventsDropDown.setAdapter(adapter);
                            loadingDialog.dismiss();
                        }
                    }
                }
                else
                {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,subEventNames);
                    eventsDropDown.setAdapter(adapter);
                    loadingDialog.dismiss();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private void showLoading() {
        // adding ALERT Dialog builder object and passing activity as parameter
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.CustomAlertDialog);

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