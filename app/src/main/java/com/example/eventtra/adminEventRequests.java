package com.example.eventtra;

import android.app.AlertDialog;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class adminEventRequests extends Fragment {


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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_admin_event_requests, container, false);
        globalData = (GlobalData) getContext().getApplicationContext();
        recyclerView = view.findViewById(R.id.adminRequestRecyclerView);
        eventRequestModelArrayList.clear();
        getRequestsData();
        // Inflate the layout for this fragment
        return view;
    }

    private void getRequestsData() {
        showLoading();

        eventRequestsCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                Log.d("Count Requests", "onSuccess: "+queryDocumentSnapshots.size());
                counter = 0;
                if(queryDocumentSnapshots.size()>0)
                {
                    for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                        EventRequestModel request = documentSnapshot.toObject(EventRequestModel.class);
                        request.setRequestId(documentSnapshot.getId());
                        if(!request.getRequestStatus().equals("Canceled"))
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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_bar, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty())
                {
                    populateList();
                }
                else
                {
                    ArrayList<EventRequestModel> filtered = new ArrayList<>();
                    for (int i = 0; i < eventRequestModelArrayList.size(); i++) {
                        if(eventRequestModelArrayList.get(i).getRequestName().toLowerCase().contains(newText.toLowerCase()))
                        {
                            filtered.add(eventRequestModelArrayList.get(i));
                        }
                    }
                    EventRequestAdapter adapter= new EventRequestAdapter(filtered, getContext(),userData,userDeviceTokens);
                    recyclerView.setAdapter(adapter);

                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}