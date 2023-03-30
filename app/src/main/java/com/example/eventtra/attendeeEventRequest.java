package com.example.eventtra;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class attendeeEventRequest extends Fragment {

    RecyclerView recyclerView;
    //pass user data here. this is the array
    ArrayList<EventRequestModel> eventRequestModelArrayList = new ArrayList<>();

    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference eventRequestsCollection = database.collection("EventRequests");
    final private CollectionReference userCollection = database.collection("User");
    HashMap<String,String> userData = new HashMap<>();
    HashMap<String,String> userDeviceTokens = new HashMap<>();
    private AlertDialog loadingDialog;
    GlobalData globalData;
    int counter=0;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_event_request, container, false);
        globalData = (GlobalData) getContext().getApplicationContext();
        context= getContext();
        recyclerView = view.findViewById(R.id.attendeeRequestRecyclerView);
        eventRequestModelArrayList.clear();
        getRequestsData();
        // Inflate the layout for this fragment
        return view;
    }

    private void getRequestsData() {
        showLoading();

        eventRequestsCollection.whereEqualTo("userID",globalData.globalUser.getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                Log.d("Count Requests", "onSuccess: "+queryDocumentSnapshots.size());
                counter = 0;
                if(queryDocumentSnapshots.size()>0)
                {
                    for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                        EventRequestModel request = documentSnapshot.toObject(EventRequestModel.class);
                        request.setRequestId(documentSnapshot.getId());
                        eventRequestModelArrayList.add(request);
                        counter++;
                        if(counter==queryDocumentSnapshots.size())
                        {
                            getUsersName();
                        }
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
    private void getUsersName() {
        counter = 0;
        userCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0)
                {
                    for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                        userData.put(documentSnapshot.getId(),documentSnapshot.get("fname")+" "+documentSnapshot.get("lname"));
                        userDeviceTokens.put(documentSnapshot.getId(),documentSnapshot.get("deviceToken").toString());
                        counter++;
                        if(counter==queryDocumentSnapshots.size())
                        {
                            populateList();
                        }


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
                Toast.makeText(getContext(), "Error while Getting Info", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void populateList() {
        Log.d("all Events", "populateList: "+eventRequestModelArrayList);
        EventRequestAdapter adapter= new EventRequestAdapter(eventRequestModelArrayList, getContext(),userData,userDeviceTokens);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        loadingDialog.dismiss();
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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.attendee_add_event_request, menu);
        MenuItem addViewItem = menu.findItem(R.id.app_bar_add);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(addViewItem);
        addViewItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                addRequest();
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    void addRequest()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.attendee_event_request_dialog, null);
        builder.setView(view);
        final TextInputLayout requestNameLayout = view.findViewById(R.id.requestNameInputlayout);
        final TextInputLayout requestDesLayout = view.findViewById(R.id.requestDesInputlayout);
        final EditText editName = view.findViewById(R.id.requestNameInput);
        final EditText editDes = view.findViewById(R.id.requestDesInput);
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        AlertDialog alertDialog = builder.create();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rName = editName.getText().toString();
                String rDes = editDes.getText().toString();
                if(rName.isEmpty())
                {
                    requestNameLayout.setError("Please Enter Name");
                    requestNameLayout.requestFocus();
                }
                else if(rDes.isEmpty())
                {
                    requestDesLayout.setError("Please Enter Description");
                    requestDesLayout.requestFocus();
                }
                else
                {
                    alertDialog.dismiss();
                    showLoading();
                    EventRequestModel request = new EventRequestModel(rName,rDes,"Pending",globalData.globalUser.getUserId());
                    eventRequestsCollection.add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            userCollection.whereEqualTo("role","admin").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if(queryDocumentSnapshots.size()>0) {
                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            FCMSend.pushNotification(
                                                    context,
                                                    documentSnapshot.get("deviceToken").toString(),
                                                    "Event Request",
                                                    globalData.globalUser.getFname()+" "+globalData.globalUser.getLname()+
                                                            " has requested to arrange "+request.getRequestName(),
                                                    "MianActivity","Event Request"
                                            );
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                            requireActivity().getSupportFragmentManager().popBackStack();
                            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new attendeeEventRequest()).addToBackStack("attendeeEventRequest").commit();
                            loadingDialog.dismiss();
                        }
                    });

                }
            }
        });

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }
}