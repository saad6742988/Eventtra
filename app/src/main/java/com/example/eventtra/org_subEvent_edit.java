package com.example.eventtra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class org_subEvent_edit extends Fragment {


    private Button saveBtn;
    private EditText eventName,eventDes,eventPrice,eventNoOfPar,streamLinkBox;
    private TextInputLayout eventNameLayout,eventDesLayout,
            subDatePickLayout,eventPicLayout,eventPriceLayout,eventNoOfParlayout,streamLinkBoxLayout;
    Switch openEnrollmentSwitch,liveStreamSwitch;
    private TextView addEventPic;
    private ImageView eventPic;
    private Uri pictureUri;
    private TextView header;
    TimePicker subEventTime;

    Spinner categoryDropDown;
    private String spinnerSelectedCategory;


    private DatePicker subDatePick;
    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference subEventCollection = database.collection("SubEvent");



    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();

    private AlertDialog loadingDialog;
    GlobalData globalData;
    private Context context;

    private boolean picChangeCheck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_org_sub_event_edit, container, false);
        globalData = (GlobalData) getActivity().getApplicationContext();
        context=getContext();

        saveBtn=view.findViewById(R.id.saveBtn);

        eventName=view.findViewById(R.id.eventNameBox);
        eventDes=view.findViewById(R.id.eventDesBox);
        eventPrice=view.findViewById(R.id.eventPriceBox);
        eventNoOfPar = view.findViewById(R.id.eventNoParticipantsBox);
        streamLinkBox=view.findViewById(R.id.streamLinkBox);

        eventNoOfParlayout=view.findViewById(R.id.eventNoParticipantsBoxLayout);
        eventNameLayout=view.findViewById(R.id.eventNameBoxLayout);
        eventDesLayout=view.findViewById(R.id.eventDesBoxLayout);
        eventPriceLayout=view.findViewById(R.id.eventPriceBoxLayout);
        streamLinkBoxLayout=view.findViewById(R.id.streamLinkBoxLayout);

        openEnrollmentSwitch=view.findViewById(R.id.openEnrollmentSwitch);
        liveStreamSwitch=view.findViewById(R.id.liveStreamSwitch);

        categoryDropDown=view.findViewById(R.id.categoryDropDown);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,globalData.eventCategories);
        categoryDropDown.setAdapter(adapter);

        subDatePick=view.findViewById(R.id.subEventDatePick);
        subDatePickLayout=view.findViewById(R.id.subEventDatePickLayout);
        subDatePick.setMinDate(System.currentTimeMillis()-1000);

        eventPic=view.findViewById(R.id.eventPicture);
        addEventPic=view.findViewById(R.id.addEventPicBtn);
        header=view.findViewById(R.id.headSubEventEdittv);
        subEventTime = view.findViewById(R.id.sub_event_time_picker);
        subEventTime.setIs24HourView(true);
        subEventTime.setHour(subEventTime.getHour()+1);


        setData();

        eventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                header.setText(eventName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        eventPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePic();
            }
        });
        addEventPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePic();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveSubEvent();
//                convertToTimeStamp(subDatePick,subEventTime);
            }
        });

        categoryDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedCategory = globalData.eventCategories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoryDropDown.setSelection(0);
                spinnerSelectedCategory = globalData.eventCategories[0];
            }
        });
        return view;
    }

    private void saveSubEvent() {
        String subName = eventName.getText().toString();
        String subDes = eventDes.getText().toString();
        String subPrice = eventPrice.getText().toString();
        String noOfPar=eventNoOfPar.getText().toString();
        String streamLink=streamLinkBox.getText().toString();
        Log.d("check stream link", streamLink);


        if(subName.isEmpty())
        {
            eventNameLayout.setError("Sub-Event Name Required");
            eventNameLayout.requestFocus();
        }
        else if(subDes.isEmpty()){
            eventDesLayout.setError("Sub-Event Description Required");
            eventDesLayout.requestFocus();
        }
        else if(subPrice.isEmpty())
        {
            eventPriceLayout.setError("Sub-Event Price Required");
            eventPriceLayout.requestFocus();
        }
        else if(noOfPar.isEmpty() || Integer.parseInt(noOfPar)<1)
        {
            eventNoOfParlayout.setError("Minimum No. of Participants Must be Greater than 1");
            eventNoOfParlayout.requestFocus();
        }
        else if (liveStreamSwitch.isChecked() && streamLink.isEmpty())
        {
            streamLinkBoxLayout.setError("Stream Link Required");
            streamLinkBoxLayout.requestFocus();
        }
        else{
            showLoading();
            globalData.globalSubEvent.setName(subName);
            globalData.globalSubEvent.setDesc(subDes);
            globalData.globalSubEvent.setPrice(subPrice);
            globalData.globalSubEvent.setSubEventDate(getDateString(subDatePick));
            globalData.globalSubEvent.setSubEventTime(subEventTime.getHour()+":"+subEventTime.getMinute());
            globalData.globalSubEvent.setMinParticipants(Integer.parseInt(noOfPar));
            globalData.globalSubEvent.setStreamLink(streamLink);
            globalData.globalSubEvent.setCategory(spinnerSelectedCategory);
            globalData.globalSubEvent.setEventTime(convertToTimeStamp(subDatePick,subEventTime));
            String registrationStatus="Registrations are closed Now!";
            if(openEnrollmentSwitch.isChecked()) {
                globalData.globalSubEvent.setOpenRegistration(true);
                registrationStatus = "Registrations are open Now!";
            }
            else
                globalData.globalSubEvent.setOpenRegistration(false);
            if(liveStreamSwitch.isChecked()) {
                globalData.globalSubEvent.setStreamStatus(true);
            }
            else
                globalData.globalSubEvent.setStreamStatus(false);
            Map<String,Object> updateSubEvent = new HashMap<>();
            updateSubEvent.put("name",globalData.globalSubEvent.getName());
            updateSubEvent.put("desc",globalData.globalSubEvent.getDesc());
            updateSubEvent.put("price",globalData.globalSubEvent.getPrice());
            updateSubEvent.put("subEventDate",globalData.globalSubEvent.getSubEventDate());
            updateSubEvent.put("subEventTime",globalData.globalSubEvent.getSubEventTime());
            updateSubEvent.put("minParticipants",globalData.globalSubEvent.getMinParticipants());
            updateSubEvent.put("openRegistration",globalData.globalSubEvent.isOpenRegistration());
            updateSubEvent.put("streamStatus",globalData.globalSubEvent.isStreamStatus());
            updateSubEvent.put("streamLink",globalData.globalSubEvent.getStreamLink());
            updateSubEvent.put("category",globalData.globalSubEvent.getCategory());
            updateSubEvent.put("eventTime",globalData.globalSubEvent.getEventTime());
            subEventCollection.document(globalData.globalSubEvent.getSubEventId()).update(updateSubEvent);

            //send registration open/close notification
            FCMSend.pushNotification(context,"/topics/All",globalData.globalSubEvent.getName(),
                    registrationStatus,"MianActivity","Registration Status");
            if(picChangeCheck)
            {
                StorageReference file = storageReference.child("SubEvent/"+globalData.globalSubEvent.getSubEventId()+"/subevent.jpg");
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
                                    globalData.globalSubEvent.setPic(uri);

                                    goBackToSubEventList();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("upload image", "onFailure: fail");
                        //do nothing will show default event pic
                        globalData.globalSubEvent.setPic(null);
                        goBackToSubEventList();
                    }
                });
            }
            else
            {
                goBackToSubEventList();
            }

        }
    }

    private void goBackToSubEventList() {
        loadingDialog.dismiss();
        getActivity().getSupportFragmentManager().popBackStack();
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_organizer, new subEvent_list()).commit();
    }

    private String getDateString(DatePicker startDatePick) {
        int date = startDatePick.getDayOfMonth();
        int month = startDatePick.getMonth()+1 ;
        int year = startDatePick.getYear();
        String dateString="";
        if(date<10)
        {
            dateString+="0"+date;
        }
        else
        {
            dateString+=date;
        }
        dateString+="/";
        if(month<10)
        {
            dateString+="0"+month;
        }
        else
        {
            dateString+=month;
        }
        dateString+="/"+year;

        return dateString;
    }
    private void setData() {

        eventName.setText(globalData.globalSubEvent.getName());
        header.setText(globalData.globalSubEvent.getName());
        eventNoOfPar.setText(String.valueOf(globalData.globalSubEvent.getMinParticipants()));
        if(!globalData.globalSubEvent.getDesc().equals(""))
            eventDes.setText(globalData.globalSubEvent.getDesc());
        if(!globalData.globalSubEvent.getPrice().equals(""))
            eventPrice.setText(globalData.globalSubEvent.getPrice());
        if(!globalData.globalSubEvent.getStreamLink().equals(""))
            streamLinkBox.setText(globalData.globalSubEvent.getStreamLink());
        if(!globalData.globalSubEvent.getSubEventDate().equals(""))
        {
            int[] temp = getDateInt(globalData.globalSubEvent.getSubEventDate());
            subDatePick.updateDate(temp[2],temp[1],temp[0]);
        }
        if(!globalData.globalSubEvent.getSubEventTime().equals(""))
        {
            String[] temp = globalData.globalSubEvent.getSubEventTime().split(":");
            subEventTime.setHour(Integer.parseInt(temp[0]));
            subEventTime.setMinute(Integer.parseInt(temp[1]));
        }

        Calendar calendar = Calendar.getInstance();
        Date subEventDateTime = globalData.globalSubEvent.getEventTime().toDate();
        calendar.setTime(subEventDateTime);
        Log.d("subEventDateTime", "setData: "+calendar.getTime());
        subDatePick.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
        subEventTime.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        subEventTime.setMinute(calendar.get(Calendar.MINUTE));


        if(globalData.globalSubEvent.isOpenRegistration())
            openEnrollmentSwitch.setChecked(true);
        else
            openEnrollmentSwitch.setChecked(false);
        if(globalData.globalSubEvent.isStreamStatus())
            liveStreamSwitch.setChecked(true);
        else
            liveStreamSwitch.setChecked(false);


        categoryDropDown.setSelection(Arrays.asList(globalData.eventCategories).indexOf(globalData.globalSubEvent.getCategory()));




        pictureUri=globalData.globalSubEvent.getPic();
        if(globalData.globalSubEvent.getPic()==null)
        {
            eventPic.setVisibility(View.GONE);
            addEventPic.setVisibility(View.VISIBLE);
        }
        else
        {
            eventPic.setVisibility(View.VISIBLE);
            addEventPic.setVisibility(View.GONE);
            Picasso.get().load(globalData.globalSubEvent.getPic()).into(eventPic);
        }
    }
    private int[] getDateInt(String startDate) {
        String[] temp = startDate.split("/");
        return new int[]{Integer.parseInt(temp[0]),Integer.parseInt(temp[1])-1,Integer.parseInt(temp[2])};
    }


    private void changePic() {
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGallery,1000);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                pictureUri = data.getData();
                Log.d("picture URi", "onActivityResult: "+pictureUri.toString());
                eventPic.setImageURI(pictureUri);
                picChangeCheck=true;
                globalData.globalSubEvent.setPic(pictureUri);
                saveBtn.setEnabled(true);
                //profilePic.setImageURI(pictureUri);
//                showLoading();
//                uploadToFirebase(pictureUri);
                addEventPic.setVisibility(View.GONE);
                eventPic.setVisibility(View.VISIBLE);

            }
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
    public Timestamp convertToTimeStamp(DatePicker datePicker, TimePicker subEventTime)
    {

        //need changes
        int date = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,date,subEventTime.getHour(),subEventTime.getMinute(),0);
        Log.d("Calender", calendar.getTime().toString());

        Date dateObj = calendar.getTime();
        Log.d("Date", ""+dateObj.toString());
        Timestamp timestamp = new Timestamp(dateObj);
        Log.d("Timestamp", ""+timestamp.toString());



        return timestamp;

    }

}