package com.example.eventtra;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class attendee_sub_events extends Fragment {

    TextView mainName,mainDes,mainDate;
    RecyclerView recyclerView;
    GlobalData globalData;
    //pass user data here. this is the array
    ArrayList <subEventsModel> subEventList = new ArrayList<>();


    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference subEventCollection = database.collection("SubEvent");

    private AlertDialog loadingDialog;
    int counterEvent = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_sub_events, container, false);
        mainName = view.findViewById(R.id.mainEventName);
        mainDes = view.findViewById(R.id.mainEventDes);
        mainDate = view.findViewById(R.id.mainEventDate);
        globalData = (GlobalData) getContext().getApplicationContext();
        mainName.setText(globalData.globalEvent.getEventName());
        mainDes.setText(globalData.globalEvent.getEventDes());
        mainDate.setText(globalData.globalEvent.getStartDate()+" to "+globalData.globalEvent.getEndDate());
        recyclerView=view.findViewById(R.id.attendeeSubRecyclerView);
        subEventList.clear();
        getEventsData();

        return view;
    }

    private void getEventsData() {
        showLoading();

        subEventCollection.whereEqualTo("mainEventId",globalData.globalEvent.getEventId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                Log.d("Count events", "onSuccess: "+queryDocumentSnapshots.size());
                counterEvent = 0;
                if(queryDocumentSnapshots.size()>0)
                {
                    for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                        subEventsModel event = documentSnapshot.toObject(subEventsModel.class);
                        event.setSubEventId(documentSnapshot.getId());


                        if(!event.getDesc().equals("")) {
                            //get event Picture
                            StorageReference file = storageReference.child("SubEvent/" + documentSnapshot.getId() + "/subevent.jpg");
                            file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("main get image", "onSuccess: fetch success");
                                    if (uri != null) {
                                        Log.d("Image uri", "onSuccess: " + uri);
                                        event.setPic(uri);


                                        subEventList.add(event);

                                        counterEvent++;
                                        if (counterEvent == queryDocumentSnapshots.size()) {
                                            populateList();
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    event.setPic(null);
                                    Log.d("Error", "onFailure: " + e.getMessage());
                                    Log.d("null event", "onFailure: " + event);
                                    subEventList.add(event);
                                    counterEvent++;
                                    if (counterEvent == queryDocumentSnapshots.size()) {
                                        populateList();
                                    }
                                }
                            });
                        }
                        else {
                            counterEvent++;
                            loadingDialog.dismiss();
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

    private void populateList() {
        ArrayList <subEventsModel> subEventListSorted = new ArrayList<>();
        subEventListSorted = getSortedList();

        Log.d("all Events", "populateList: "+subEventList);
        attendeeSubEventAdapter adapter= new attendeeSubEventAdapter(subEventListSorted,getContext() );
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        loadingDialog.dismiss();
    }

    private ArrayList<subEventsModel> getSortedList() {
        ArrayList <subEventsModel> temp = new ArrayList<>();
        String[] sortedCategories=globalData.eventCategories;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Arrays.sort(sortedCategories, Comparator.comparing(globalData.globalUser.getCategoryHistory()::get, Comparator.nullsLast(Comparator.reverseOrder())));
        }
        for (int i = 0; i < sortedCategories.length; i++) {
            for (int j = 0; j < subEventList.size(); j++) {
                if(subEventList.get(j).getCategory().equals(sortedCategories[i]))
                {
                    temp.add(subEventList.get(j));
                }
            }
        }

        return temp;
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
                    ArrayList<subEventsModel> filtered = new ArrayList<>();
                    for (int i = 0; i < subEventList.size(); i++) {
                        if(subEventList.get(i).getName().toLowerCase().contains(newText.toLowerCase()))
                        {
                            Toast.makeText(getContext(), subEventList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            filtered.add(subEventList.get(i));
                        }
                    }
                    attendeeSubEventAdapter adapter= new attendeeSubEventAdapter(filtered, getContext());
                    recyclerView.setAdapter(adapter);

                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

}