package com.example.eventtra;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class addSubeventsAndHeads extends Fragment {


    private Button addSubEventBtn,addEventBtn;
    private TextInputLayout mainHeadLayout,mainSubEventLayout;
    private EditText mainHeadText,mainSubEventText;
    private LinearLayout dynamicContainer;
    private int subEventCount;
    private ArrayList<Map<String,String>> subEventsList=new ArrayList<Map<String,String>>();
//    private Map<String,String> subEvents=new HashMap<>();
    private Bundle eventData;

    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference eventCollection = database.collection("Event");
    final private CollectionReference subEventCollection = database.collection("SubEvent");
    final private CollectionReference userCollection = database.collection("User");

    private Map<String,String> heads =new HashMap<>();
    private Map<String,String> subEvent_userID=new HashMap<>();

    private MyEvent newMainEvent;

    private AlertDialog loadingDialog;

    private String[] emails = {"ms6742988@gmail.com","hello@gmail.com","abc@gmail.com"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_subeventandheads, container, false);
        eventData=this.getArguments();


        //getting all views
        mainHeadLayout = view.findViewById(R.id.mainHeadlayout);
        mainSubEventLayout = view.findViewById(R.id.mainSubEventlayout);

        mainHeadText = view.findViewById(R.id.mainHeadBox);
        mainSubEventText = view.findViewById(R.id.mainSubEventBox);
        mainSubEventText.setText(eventData.getString("name"));

        addSubEventBtn=view.findViewById(R.id.addSubEventBtn);
        addEventBtn=view.findViewById(R.id.addEventBtn);

        dynamicContainer=view.findViewById(R.id.dynamicContainer);

        addSubEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainSubEventText.getText().toString().isEmpty())
                {
                    mainSubEventLayout.setError("Sub Event Name Required");
                }
                else if (mainHeadText.getText().toString().isEmpty()){
                    mainHeadLayout.setError("Head Email Required");
                }
                else
                {
                    validOrganizer(mainSubEventText.getText().toString(),mainHeadText.getText().toString(),mainHeadLayout,-1);
                }

            }
        });
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subEventsList.size()==0)
                {
                    mainSubEventLayout.setError("At Least 1 Sub Event Required");
                }
                else
                {
                    postEvent();
                }

            }
        });
        subEventCount=0;
        return view;
    }

    private void generateSubevents() {

        for (int i = 0; i < subEventsList.size(); i++) {
            String subEventName = subEventsList.get(i).keySet().iterator().next();
            String headEmail = subEventsList.get(i).get(subEventName);
            String headId = heads.get(headEmail);
            int index = i;
            subEventsModel newSubEvent=new subEventsModel(subEventName,headId,newMainEvent.getEventId());
            subEventCollection.add(newSubEvent).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    newSubEvent.setSubEventId(documentReference.getId());
                    subEvent_userID.put(newSubEvent.getSubEventId(),headId);
                    Log.d("Sub Event Added", "onSuccess: "+newSubEvent);

                    if(index==subEventsList.size()-1)
                    {
                        Log.d("Updating Main Event", subEvent_userID.toString());
                        Map<String,Object> updateEvent = new HashMap<>();
                        updateEvent.put("subEvents",subEvent_userID);
                        eventCollection.document(newMainEvent.getEventId()).update(updateEvent);
                        updateOrganizersStatus();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Fail To add Sub_event: "+subEventName, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void updateOrganizersStatus() {

        for (String headEmail: heads.keySet()) {
            Map<String,Object> roleUpdate = new HashMap<>();
            roleUpdate.put("role","organizer");
            userCollection.document(heads.get(headEmail)).update(roleUpdate);
        }
        loadingDialog.dismiss();

        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new addEventdetails()).addToBackStack("addEventDetails").commit();
    }

    private void postEvent() {

        showLoading();
        String name=eventData.getString("name");
        String des=eventData.getString("des");
        String startDate=eventData.getString("startDate");
        String endDate=eventData.getString("endDate");
        Uri pictureUri = Uri.parse(eventData.getString("pictureUri"));


        //posting Event in database
        newMainEvent = new MyEvent(name,des,startDate,endDate,pictureUri);
        Log.d("eventData", "validateAndPostData: "+newMainEvent);
        eventCollection.add(newMainEvent).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("event add", "onSuccess: " +documentReference.getId());
                newMainEvent.setEventId(documentReference.getId());

                //Posting Event Picture in Cloud Storage
                Log.d("picture and id", "uploadToFirebase: "+newMainEvent.getEventId()+"  "+pictureUri);
                StorageReference file = storageReference.child("Event/"+newMainEvent.getEventId()+"/event.jpg");
                file.putFile(pictureUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("upload image", "onSuccess: done");
                        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if(uri!=null)
                                {
                                    Log.d("Image uri", "onSuccess: "+uri);

                                    //now posting all respective sub events
                                    generateSubevents();


                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("upload image", "onFailure: fail");
                        //do nothing will show default event pic
                        //still make subevents
                        generateSubevents();
                    }
                });



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("event add", "onFailure: ");
                //move back to event detail page previos page
            }
        });


////        Map<String,String> subEventsMap = new HashMap<>();
//        for (int i = 0; i < subEventsList.size(); i++) {
//            String key = subEventsList.get(i).keySet().iterator().next();
//            Log.d("key", "postEvent: "+key);
//            subEvents.put(key,subEventsList.get(i).get(key));
//        }
//        MyEvent newEvent = new MyEvent(eventData.getString("name"),eventData.getString("des"),eventData.getString("startDate"),
//                eventData.getString("endDate"), Uri.parse(eventData.getString("pictureUri")),subEvents);
//        Log.d("Event", "postEvent: "+newEvent);

    }


    private void validOrganizer(String subEventName, String head,TextInputLayout headLayout,int index) {
        //check if we already have that head ID
        if(heads.containsKey(head))
        {
            if(index==-1)
            {
                addFields(subEventName,head);
            }
            else{
                Log.d("update before", subEventsList.toString());
                Map<String,String> SEupdate=new HashMap<>();
                SEupdate.put(subEventName,head);
                subEventsList.set(index,SEupdate);
                Log.d("update after", subEventsList.toString());
            }

            Log.d("users local", "onSuccess: "+ heads);
        }
        else
        {
            showLoading();
            //getiing entered user from database
            userCollection.whereEqualTo("email",head).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    MyUser user=new MyUser();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        user = documentSnapshot.toObject(MyUser.class);
                        user.setUserId(documentSnapshot.getId());
                        Log.d("user", "onSuccess: "+user.toString());
                    }
                    if(queryDocumentSnapshots.size()==0)
                    {
                        headLayout.setError("User Not found");
                    }
                    else if(user.getRole().equals("admin"))
                    {
                        headLayout.setError("Admin Can't be head");
                    }
                    else
                    {
                        heads.put(head,user.getUserId());
                        if(index==-1)
                        {
                            addFields(subEventName,head);
                        }
                        else{
                            Log.d("update before", subEventsList.toString());
                            Map<String,String> SEupdate=new HashMap<>();
                            SEupdate.put(subEventName,head);
                            subEventsList.set(index,SEupdate);
                            Log.d("update after", subEventsList.toString());
                        }
                        Log.d("users", "onSuccess: "+ heads);
                    }
                    loadingDialog.dismiss();
                }
            });
        }

    }

    private void addFields(String subEventName, String head) {


            subEventCount++;
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.sub_event_portion, null);

            TextInputLayout headLayout = addView.findViewById(R.id.headInputlayout);
            TextInputLayout subEventLayout = addView.findViewById(R.id.subBoxInputlayout);
            EditText headText = addView.findViewById(R.id.headBox);
            EditText subEventText = addView.findViewById(R.id.subBox);
            ImageButton menuBtn = addView.findViewById(R.id.subEventMenuBtn);
            int indexLocal = subEventCount;

            ///setting menu of layouts
            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(getActivity(), v);
                    popup.inflate(R.menu.subevent_add_menu);
                    MenuItem saveItem = popup.getMenu().findItem(R.id.saveSubEvent);
                    MenuItem removeItem = popup.getMenu().findItem(R.id.removeSubEvent);
                    saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {

                            Toast.makeText(getActivity(), "save clicked"+indexLocal, Toast.LENGTH_SHORT).show();
                            Log.d("sub events before updated", subEventsList.toString());
                            if(subEventText.getText().toString().isEmpty())
                            {
                                subEventLayout.setError("Sub Event Name Required");
                            }
                            else if (headText.getText().toString().isEmpty()){
                                headLayout.setError("Head Email Required");
                            }
                            else
                            {
                                subEventLayout.setError(null);
                                headLayout.setError(null);
                                validOrganizer(subEventText.getText().toString(),headText.getText().toString(),headLayout,indexLocal-1);
                            }
                            Log.d("sub events updated", subEventsList.toString());
                            return true;
                        }
                    });
                    removeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {
                            Toast.makeText(getActivity(), "remove clicked ok"+subEventText.getText().toString(), Toast.LENGTH_SHORT).show();
                            subEventsList.remove(indexLocal-1);
                            ((LinearLayout)addView.getParent()).removeView(addView);
                            Log.d("sub events removesd", subEventsList.toString());
                            return true;
                        }
                    });
                    popup.show();
                }
            });


            Map<String,String> subEventTemp =new HashMap<>();
            subEventTemp.put(subEventName,head);
            subEventsList.add(subEventTemp);
            Log.d("sub events added", subEventsList.toString());

            headText.setText(head);
            subEventText.setText(subEventName);
            dynamicContainer.addView(addView);

            //setting botttom margin
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) addView.getLayoutParams();
            final float scale = getContext().getResources().getDisplayMetrics().density;
            int pixels = (int) (5 * scale + 0.5f);
            params.setMargins(0, 0, 0,pixels );

            mainHeadLayout.setError(null);
            mainSubEventLayout.setError(null);
            mainHeadText.setText("");
            mainSubEventText.setText("");
            mainHeadText.clearFocus();
            mainSubEventText.clearFocus();


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