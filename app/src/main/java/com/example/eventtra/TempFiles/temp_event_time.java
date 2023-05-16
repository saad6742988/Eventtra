package com.example.eventtra.TempFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TimePicker;

import com.example.eventtra.R;

public class temp_event_time extends AppCompatActivity {

    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_time);
        timePicker= findViewById(R.id.sub_event_time_picker);
    }


}