package com.example.eventtra.Admin;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventtra.Admin.addSubeventsAndHeads;
import com.example.eventtra.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;


public class addEventdetails extends Fragment {


    private Button nextBtn;
    private EditText eventName,eventDes;
    private TextInputLayout eventNameLayout,eventDesLayout,startDatePickLayout,endDatePickLayout,eventPicLayout;
    private TextView addEventPic,header;
    private ImageView eventPic;
    private DatePicker startDatePick,endDatePick;
    private Uri pictureUri;


    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference eventCollection = database.collection("Event");

    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();

    private AlertDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_eventdetails, container, false);
        nextBtn=view.findViewById(R.id.nextBtn);
        eventName=view.findViewById(R.id.eventNameBox);
        eventDes=view.findViewById(R.id.eventDesBox);
        eventNameLayout=view.findViewById(R.id.eventNameBoxLayout);
        eventDesLayout=view.findViewById(R.id.eventDesBoxLayout);
        startDatePick=view.findViewById(R.id.startDatePick);
        startDatePickLayout=view.findViewById(R.id.startDatePickLayout);
        endDatePick=view.findViewById(R.id.endDatePick);
        endDatePickLayout=view.findViewById(R.id.endDatePickLayout);
        startDatePick.setMinDate(System.currentTimeMillis()-1000);
        endDatePick.setMinDate(System.currentTimeMillis()-1000);


        eventPic=view.findViewById(R.id.eventPicture);
        addEventPic=view.findViewById(R.id.addEventPicBtn);
        header=view.findViewById(R.id.mainEventHeader);

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

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndPostData();

                // to be removed
//                convertToTimeStamp(startDatePick);
            }
        });
        return view;
    }

    private void changePic() {
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGallery,1000);
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
//        else if(!validDate(startDatePick.getYear(),startDatePick.getMonth(),startDatePick.getDayOfMonth(),endDatePick.getYear(),endDatePick.getMonth(),endDatePick.getDayOfMonth()))
        else if(!validDateTimeStamp(convertToTimeStamp(startDatePick),convertToTimeStamp(endDatePick)))
        {
            endDatePickLayout.setError("End Date can't Be Before Start Date");
        }
        else if(pictureUri==null)
        {
            addEventPic.setText("Please Upload A Picture");
            addEventPic.setTextColor(Color.parseColor("#ffff4444"));
        }
        else
        {



            Bundle eventData=new Bundle();
            eventData.putString("name",name);
            eventData.putString("des",des);
            eventData.putSerializable("startDate",convertToTimeStamp(startDatePick).toDate());
            eventData.putSerializable("endDate",convertToTimeStamp(endDatePick).toDate());
            eventData.putString("pictureUri",pictureUri.toString());
//
            addSubeventsAndHeads newfrag = new addSubeventsAndHeads();
            newfrag.setArguments(eventData);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newfrag).addToBackStack(null).commit();
        }


    }

    public boolean validDate(int startYear, int startMonth,int startDay,int endYear, int endMonth, int endDay)
    {


        if(startYear>endYear)
            return false;
        if(startYear==endYear)
        {
            if(startMonth>endMonth)
                return false;
            if(startMonth==endMonth)
            {
                if(startDay>endDay)
                    return false;
            }
        }
        return true;
    }
    public boolean validDateTimeStamp(Timestamp startDate,Timestamp endDate)
    {
        int result = startDate.compareTo(endDate);

        if (result < 0) {
            return true;
        } else if (result > 0) {
            return false;
        } else {
            return true;
        }
    }

    private int[] getDateInt(String startDate) {
        String[] temp = startDate.split("/");
        return new int[]{Integer.parseInt(temp[0]),Integer.parseInt(temp[1])-1,Integer.parseInt(temp[2])};
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
                //profilePic.setImageURI(pictureUri);
//                showLoading();
//                uploadToFirebase(pictureUri);
                addEventPic.setVisibility(View.GONE);
                eventPic.setVisibility(View.VISIBLE);

            }
        }
    }


    public Timestamp convertToTimeStamp(DatePicker datePicker)
    {

        //need changes
        int date = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,date,0,0,0);
        Log.d("Calender", calendar.getTime().toString());

        Date dateObj = calendar.getTime();
        Log.d("Date", ""+dateObj.toString());
        Timestamp timestamp = new Timestamp(dateObj);
        Log.d("Timestamp", ""+timestamp.toString());



        return timestamp;

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