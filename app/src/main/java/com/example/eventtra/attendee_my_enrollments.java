package com.example.eventtra;

import android.net.Uri;
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


public class attendee_my_enrollments extends Fragment {

    RecyclerView attendeeEnrollmentRecyclerView;
    //pass user data here. this is the array
    ArrayList<PaymentInfo> paymentInfoArrayList = new ArrayList<>();
    GlobalData globalData;

    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference paymentCollection = database.collection("Payments");
    private AlertDialog loadingDialog;
    int counter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_attendee_my_enrollments, container, false);
        attendeeEnrollmentRecyclerView=view.findViewById(R.id.attendeeEnrollmentRecyclerView);
        globalData = (GlobalData) getActivity().getApplicationContext();
        paymentInfoArrayList.clear();
        getPaymentsData();
        return view;
    }

    private void getPaymentsData() {
        showLoading();

        paymentCollection.whereEqualTo("madeBy",globalData.getGlobalUser().getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                Log.d("Count payments", "onSuccess: "+queryDocumentSnapshots.size());
                counter = 0;
                if(queryDocumentSnapshots.size()>0)
                {
                    for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                        PaymentInfo paymentInfo = documentSnapshot.toObject(PaymentInfo.class);
                        paymentInfo.setId(documentSnapshot.getId());
                        paymentInfoArrayList.add(paymentInfo);
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
        Log.d("all Payments", "populateList: "+paymentInfoArrayList);
        attendeeEnrollmentAdapter adapter= new attendeeEnrollmentAdapter(paymentInfoArrayList, getContext());
        attendeeEnrollmentRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        attendeeEnrollmentRecyclerView.setLayoutManager(layoutManager);
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
        searchViewItem.setTitle("Enter Cnic");
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
                    ArrayList<PaymentInfo> filtered = new ArrayList<>();
                    for (int i = 0; i < paymentInfoArrayList.size(); i++) {
                        if(paymentInfoArrayList.get(i).getParticipantCnic().contains(newText))
                        {
                            Toast.makeText(getContext(), paymentInfoArrayList.get(i).getParticipantCnic(), Toast.LENGTH_SHORT).show();
                            filtered.add(paymentInfoArrayList.get(i));
                        }
                    }
                    attendeeEnrollmentAdapter adapter= new attendeeEnrollmentAdapter(filtered, getContext());
                    attendeeEnrollmentRecyclerView.setAdapter(adapter);

                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}