package com.example.eventtra;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


public class addEventdetails extends Fragment {



    private Button nextBtn;
    private EditText eventName,eventDes;
    private TextInputLayout eventNameLayout,eventDesLayout;
    private TextView addEventPic;
    private ImageView eventPic;
    private DatePicker startDatePick,endDatePick;
    MyEvent newEvent = new MyEvent();
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
        endDatePick=view.findViewById(R.id.endDatePick);
        startDatePick.setMinDate(System.currentTimeMillis()-1000);
        endDatePick.setMinDate(System.currentTimeMillis()-1000);

        eventPic=view.findViewById(R.id.eventPicture);
        addEventPic=view.findViewById(R.id.addEventPicBtn);

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
                addEventPic.setVisibility(View.GONE);
                eventPic.setVisibility(View.VISIBLE);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new addSubeventsAndHeads()).addToBackStack(null).commit();
            }
        });
        return view;
    }

    private void changePic() {
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGallery,1000);
    }

    private void validateData() {
        String name = eventName.getText().toString();
        String des = eventDes.getText().toString();
        Log.d("Event", newEvent.toString());
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                Uri pictureUri = data.getData();
                Log.d("picture URi", "onActivityResult: "+pictureUri.toString());
                eventPic.setImageURI(pictureUri);
                newEvent.setEventPicUri(pictureUri);
                //profilePic.setImageURI(pictureUri);
//                showLoading();
//                uploadToFirebase(pictureUri);

            }
        }
    }
}