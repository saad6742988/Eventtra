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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class editSubeventsAndHeads extends Fragment {


    private Button addSubEventBtn,saveEventBtn;
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
    private ArrayList<Map<String,String>> oldSubEvents =new ArrayList<Map<String,String>>();
    private Map<String,String> subEvent_userID=new HashMap<>();
    private Map<String,String> headsDeviceTokens=new HashMap<>();

    private AlertDialog loadingDialog;

    private boolean picChangeCheck = false;
    private boolean changeCheck = false;

    private GlobalData globalData;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_subevents_and_heads, container, false);
        eventData=this.getArguments();
        context=getContext();


        globalData = (GlobalData) getActivity().getApplicationContext();
        getSubEvents();
        //getting all views
        mainHeadLayout = view.findViewById(R.id.mainHeadlayout);
        mainSubEventLayout = view.findViewById(R.id.mainSubEventlayout);

        mainHeadText = view.findViewById(R.id.mainHeadBox);
        mainSubEventText = view.findViewById(R.id.mainSubEventBox);
        mainSubEventText.setText(eventData.getString("name"));

        addSubEventBtn=view.findViewById(R.id.addSubEventBtn);
        saveEventBtn=view.findViewById(R.id.saveEventBtn);
        String change=eventData.getString("change");
        String changepic=eventData.getString("changepic");
        if(change.equals("true")||changepic.equals("true"))
        {
            saveEventBtn.setEnabled(true);
        }
        else
        {
            saveEventBtn.setEnabled(false);
        }


        dynamicContainer=view.findViewById(R.id.dynamicContainer);

        addSubEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainSubEventLayout.setError(null);
                mainHeadLayout.setError(null);
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
        saveEventBtn.setOnClickListener(new View.OnClickListener() {
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

    private void postEvent() {

        showLoading();
        //updating Event
        Map<String,Object> eventUpdate = new HashMap<>();
        eventUpdate.put("eventName",globalData.globalEvent.getEventName());
        eventUpdate.put("eventDes",globalData.globalEvent.getEventDes());
        eventUpdate.put("startDate",globalData.globalEvent.getStartDate());
        eventUpdate.put("endDate",globalData.globalEvent.getEndDate());
        eventCollection.document(globalData.globalEvent.getEventId()).update(eventUpdate);

        //updateing Picture
        String changepic=eventData.getString("changepic");
        if(changepic.equals("true"))
        {
            //Posting Event Picture in Cloud Storage
            Log.d("picture and id", "uploadToFirebase: "+globalData.globalEvent.getEventId()+"  "+globalData.globalEvent.getEventPic());
            StorageReference file = storageReference.child("Event/"+globalData.globalEvent.getEventId()+"/event.jpg");
            file.putFile(globalData.globalEvent.getEventPic()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("upload image", "onSuccess: done");
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(uri!=null)
                            {
                                Log.d("Image uri", "onSuccess: "+uri);



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


        //now posting all respective sub events
        generateSubevents();



    }

    private void generateSubevents() {

        Log.d("subEvent_userID111111", subEvent_userID.toString());
        //converting aaray list to MAp
        Log.d("old list", oldSubEvents.toString());
        Map<String,String> oldSubEventsMap = new HashMap<>();
        for (int i = 0; i < oldSubEvents.size(); i++) {
            String oldsubEventName = oldSubEvents.get(i).keySet().iterator().next();
            String oldsubEventId = oldSubEvents.get(i).get(oldsubEventName);
            oldSubEventsMap.put(oldsubEventName,oldsubEventId);
        }

        Log.d("old map", oldSubEventsMap.toString());
        Log.d("subEventsList", subEventsList.toString());

        for (int i = 0; i < subEventsList.size(); i++) {
            String subEventName = subEventsList.get(i).keySet().iterator().next();
            String headEmail = subEventsList.get(i).get(subEventName);
            String headId = heads.get(headEmail);
            if (oldSubEventsMap.containsKey(subEventName)) {
                subEvent_userID.put(oldSubEventsMap.get(subEventName), headId);
                Map<String, Object> updateSub = new HashMap<>();
                updateSub.put("name", subEventName);
                updateSub.put("head", headId);
                Log.d("updating", oldSubEventsMap.get(subEventName));
                subEventCollection.document(oldSubEventsMap.get(subEventName)).update(updateSub);
                FCMSend.pushNotification(context,headsDeviceTokens.get(headEmail),
                        "Event Organizer","You have been Assigned a role of Organizer in "+subEventName
                        ,"MainActivity","Organizer");
                subEvent_userID.put(oldSubEventsMap.get(subEventName), headId);
                if (i == subEventsList.size() - 1) {
                    Log.d("Updating Main Event", subEvent_userID.toString());
                    Map<String, Object> updateEvent = new HashMap<>();
                    updateEvent.put("subEvents", subEvent_userID);
                    eventCollection.document(globalData.globalEvent.getEventId()).update(updateEvent);
                    updateOrganizersStatus();
                }
            } else {
                int index = i;
                subEventsModel newSubEvent = new subEventsModel(subEventName, headId, globalData.globalEvent.getEventId());
                subEventCollection.add(newSubEvent).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        newSubEvent.setSubEventId(documentReference.getId());
                        subEvent_userID.put(newSubEvent.getSubEventId(), headId);
                        Log.d("Sub Event Added", "onSuccess: " + newSubEvent);
                        //sending notification
                        FCMSend.pushNotification(context,headsDeviceTokens.get(headEmail),
                                "Event Organizer","You have been Assigned a role of Organizer in "+newSubEvent.getName()
                                ,"MainActivity","Organizer");
                        if (index == subEventsList.size() - 1) {
                            Log.d("Updating Main Event", subEvent_userID.toString());
                            Map<String, Object> updateEvent = new HashMap<>();
                            updateEvent.put("subEvents", subEvent_userID);
                            eventCollection.document(globalData.globalEvent.getEventId()).update(updateEvent);
                            updateOrganizersStatus();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Fail To add Sub_event: " + subEventName, Toast.LENGTH_SHORT).show();
                        if (index == subEventsList.size() - 1) {
                            Log.d("Updating Main Event", subEvent_userID.toString());
                            Map<String, Object> updateEvent = new HashMap<>();
                            updateEvent.put("subEvents", subEvent_userID);
                            eventCollection.document(globalData.globalEvent.getEventId()).update(updateEvent);
                            updateOrganizersStatus();
                        }
                    }
                });
            }


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
    }


    private void getSubEvents()
    {

        Map<String,String> subEventsPair = globalData.globalEvent.getSubEvents();
       if(subEventsPair!=null)
       {
           for (String subEventId:subEventsPair.keySet()) {
               subEventCollection.document(subEventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {
                       subEventsModel subEvent = documentSnapshot.toObject(subEventsModel.class);
                       subEvent.setSubEventId(documentSnapshot.getId());

                       Map<String,String> oldEvent =new HashMap<>();
                       oldEvent.put(subEvent.getName(),subEvent.getSubEventId());
                       oldSubEvents.add(oldEvent);

                       userCollection.document(subEventsPair.get(subEventId)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                           @Override
                           public void onSuccess(DocumentSnapshot documentSnapshot) {
                               MyUser user = documentSnapshot.toObject(MyUser.class);
                               user.setUserId(documentSnapshot.getId());
                               heads.put(user.getEmail(),user.getUserId());
                               headsDeviceTokens.put(user.getEmail(),user.getDeviceToken());
                               addFields(subEvent.getName(),user.getEmail());

                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                           }
                       });
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {

                   }
               });
           }
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

                        Log.d("indexLocal", indexLocal+" : "+oldSubEvents.size());
                        if(indexLocal-1<oldSubEvents.size())
                        {
                            String oldsubEventName = oldSubEvents.get(indexLocal-1).keySet().iterator().next();
                            String oldsubEventId = oldSubEvents.get(indexLocal-1).get(oldsubEventName);
                            Log.d("deleting", "onMenuItemClick: "+oldsubEventName);
                            subEventCollection.document(oldsubEventId).delete();
                        }
                        saveEventBtn.setEnabled(true);
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


                Log.d("Before Removing and updating old", "validOrganizer: "+oldSubEvents);
                String oldsubEventName = oldSubEvents.get(index).keySet().iterator().next();
                String oldsubEventId = oldSubEvents.get(index).get(oldsubEventName);
                Map<String,String> temp=new HashMap<>();
                temp.put(subEventName,oldsubEventId);
                oldSubEvents.set(index,temp);
                Log.d("After Removing and updating old", "validOrganizer: "+oldSubEvents);


                Log.d("update after", subEventsList.toString());
            }
            saveEventBtn.setEnabled(true);
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
                        headsDeviceTokens.put(head,user.getDeviceToken());
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
                        saveEventBtn.setEnabled(true);
                    }
                    loadingDialog.dismiss();
                }
            });
        }

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