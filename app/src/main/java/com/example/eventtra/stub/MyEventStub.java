package com.example.eventtra.stub;

import android.net.Uri;

import com.example.eventtra.Models.MyEvent;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MyEventStub {
    private Hashtable<Integer, MyEvent> table;
    public MyEventStub() {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2023,3,3,0,0,0);
        Date dateObj = calendar.getTime();
        Timestamp startDate = new Timestamp(dateObj);
        Timestamp endDate = new Timestamp(dateObj);
        calendar.set(2023,3,4,0,0,0);
        dateObj = calendar.getTime();
        Timestamp endDate2 = new Timestamp(dateObj);

        Map<String, String> myMap = new HashMap<>();
        table=new Hashtable<Integer, MyEvent>();

        myMap.put("1", "Event 1");
        myMap.put("2", "Event 2");
        table.put(1, new MyEvent("1","Event 1","This is Event 1",startDate,endDate,myMap,uri2));
        table.put(2, new MyEvent("2","Event 2","This is Event 2",startDate,endDate2,myMap,uri2));


    }
    public MyEvent getList(int id) {

        return table.get(id);

    }
}
