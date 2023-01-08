package com.example.eventtra;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class editEventDetails extends Fragment {

    private Button nextBtn;
    private EditText eventName,eventDes;
    private TextInputLayout eventNameLayout,eventDesLayout,startDatePickLayout,endDatePickLayout,eventPicLayout;
    private TextView addEventPic;
    private ImageView eventPic;
    private DatePicker startDatePick,endDatePick;
    private Uri pictureUri;

    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference eventCollection = database.collection("Event");

    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();

    private AlertDialog loadingDialog;

    private boolean picChangeCheck = false;
    private boolean changeCheck = false;
    GlobalData globalData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        globalData = (GlobalData) getActivity().getApplicationContext();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_event_details, container, false);
        nextBtn=view.findViewById(R.id.nextBtn);

        eventName=view.findViewById(R.id.eventNameBox);
        eventDes=view.findViewById(R.id.eventDesBox);
        eventNameLayout=view.findViewById(R.id.eventNameBoxLayout);
        eventDesLayout=view.findViewById(R.id.eventDesBoxLayout);

        startDatePick=view.findViewById(R.id.startDatePick);
        startDatePickLayout=view.findViewById(R.id.startDatePickLayout);
        endDatePick=view.findViewById(R.id.endDatePick);
        endDatePickLayout=view.findViewById(R.id.endDatePickLayout);


        eventPic=view.findViewById(R.id.eventPicture);
        addEventPic=view.findViewById(R.id.addEventPicBtn);

        eventName.setText(globalData.globalEvent.getEventName());
        eventDes.setText(globalData.globalEvent.getEventDes());
        eventDes.setText(globalData.globalEvent.getEventDes());

        pictureUri=globalData.globalEvent.getEventPic();
        if(globalData.globalEvent.getEventPic()==null)
        {
            eventPic.setVisibility(View.GONE);
            addEventPic.setVisibility(View.VISIBLE);
        }
        else
        {
            eventPic.setVisibility(View.VISIBLE);
            addEventPic.setVisibility(View.GONE);
            Picasso.get().load(globalData.globalEvent.getEventPic()).into(eventPic);
        }


        int[] startDate = getDateInt(globalData.globalEvent.getStartDate());
        Log.d("array int", "getDateInt: "+startDate[0]);
        int[] endDate = getDateInt(globalData.globalEvent.getEndDate());
        startDatePick.updateDate(startDate[2],startDate[1],startDate[0]);
        endDatePick.updateDate(endDate[2],endDate[1],endDate[0]);



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

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndPostData();
            }
        });



        return view;
    }


    private void validateAndPostData() {
        String name = eventName.getText().toString();
        String des = eventDes.getText().toString();
//        int[] date = getDateInt(startDate);
//        endDatePick.updateDate(date[2],date[1],date[0]);

        eventNameLayout.setError(null);
        eventDesLayout.setError(null);
        endDatePickLayout.setError(null);
        if(name.isEmpty())
        {
            eventNameLayout.setError("Event Name Can't Be Empty");
            eventNameLayout.requestFocus();
        }
        else if(des.isEmpty())
        {
            eventDesLayout.setError("Event Name Can't Be Empty");
            eventDesLayout.requestFocus();
        }
        else if(!validDate(startDatePick,endDatePick))
        {
            endDatePickLayout.setError("End Date can't Be Before Start Date");
        }
        else if(pictureUri==null)
        {
            addEventPic.setText("Please Upload A Picture");
            addEventPic.setTextColor(Color.parseColor("#ffff4444"));
            eventPic.setVisibility(View.GONE);
            addEventPic.setVisibility(View.VISIBLE);
        }
        else
        {
            if(!globalData.globalEvent.getEventName().equals(name) ||
                    !globalData.globalEvent.getEventDes().equals(des)||
                    !globalData.globalEvent.getStartDate().equals(getDateString(startDatePick))||
                    !globalData.globalEvent.getEndDate().equals(getDateString(endDatePick)))
            {
                changeCheck=true;
                globalData.globalEvent.setEventName(name);
                globalData.globalEvent.setEventDes(des);
                globalData.globalEvent.setStartDate(getDateString(startDatePick));
                globalData.globalEvent.setEndDate(getDateString(endDatePick));
            }



            Bundle eventData=new Bundle();
            if(changeCheck)
                eventData.putString("change","true");
            else
                eventData.putString("change","false");
            if(picChangeCheck)
                eventData.putString("changepic","true");
            else
                eventData.putString("changepic","false");

//
            editSubeventsAndHeads newfrag = new editSubeventsAndHeads();
            newfrag.setArguments(eventData);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newfrag).addToBackStack(null).commit();
        }


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
                globalData.globalEvent.setEventPic(pictureUri);
                //profilePic.setImageURI(pictureUri);
//                showLoading();
//                uploadToFirebase(pictureUri);
                addEventPic.setVisibility(View.GONE);
                eventPic.setVisibility(View.VISIBLE);

            }
        }
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

    private int[] getDateInt(String date) {
        String[] temp = date.split("/");
        Log.d("array string", "getDateInt: "+temp[0]);
        return new int[]{Integer.parseInt(temp[0]),Integer.parseInt(temp[1])-1,Integer.parseInt(temp[2])};
    }
    private boolean validDate(DatePicker start,DatePicker end)
    {

        if(start.getYear()>end.getYear())
            return false;
        if(start.getYear()==end.getYear())
        {
            if(start.getMonth()>end.getMonth())
                return false;
            if(start.getMonth()==end.getMonth())
            {
                if(start.getDayOfMonth()>end.getDayOfMonth())
                    return false;
            }
        }
        return true;
    }
}