package com.example.eventtra.stub;

import android.net.Uri;

import com.example.eventtra.MyEvent;
import com.example.eventtra.subEventsModel;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MyEventStub {
    private Hashtable<Integer, MyEvent> table;
    public MyEventStub() {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);

        Map<String, String> myMap = new HashMap<>();
        table=new Hashtable<Integer, MyEvent>();

        myMap.put("1", "Event 1");
        myMap.put("2", "Event 2");
        table.put(1, new MyEvent("1","Event 1","This is Event 1","2-3-2023","2-3-2023",myMap,uri2));
        table.put(2, new MyEvent("2","Event 2","This is Event 2","3-3-2023","4-3-2023",myMap,uri2));


    }
    public MyEvent getList(int id) {

        return table.get(id);

    }
}
