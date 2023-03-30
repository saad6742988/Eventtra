package com.example.eventtra;

import android.net.Uri;

import com.example.eventtra.stub.MyEventStub;
import com.example.eventtra.stub.subEventsStub;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MyEventTest {

    @Test
    public void MyEventValid()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);

        Map<String, String> myMap = new HashMap<>();

        myMap.put("1", "Event 1");
        myMap.put("2", "Event 2");

        MyEvent test=new MyEvent("1","Event 1","This is Event 1","2-3-2023","2-3-2023",myMap,uri2);
        MyEventStub testGetter= new MyEventStub();

        MyEvent test2= testGetter.getList(1);
        Assert.assertEquals(test,test2);


    }


    @Test
    public void MyEventInvalid()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);

        Map<String, String> myMap = new HashMap<>();
        myMap.put("1", "Event 1");
        myMap.put("2", "Event 2");

        MyEvent test=new MyEvent("2","Event 3","This is Event 3","2-3-2023","2-3-2023",myMap,uri2);
        MyEventStub testGetter= new MyEventStub();

        MyEvent test2= testGetter.getList(1);
        Assert.assertNotEquals(test,test2);


    }
}
