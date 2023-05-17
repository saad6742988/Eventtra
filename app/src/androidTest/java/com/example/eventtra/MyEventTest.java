package com.example.eventtra;

import android.net.Uri;

import com.example.eventtra.Models.MyEvent;
import com.example.eventtra.stub.MyEventStub;
import com.google.firebase.Timestamp;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyEventTest {

    @Test
    public void MyEventValid1()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2023,3,3,0,0,0);
        Date dateObj = calendar.getTime();
        Timestamp startDate = new Timestamp(dateObj);
        Timestamp endDate = new Timestamp(dateObj);


        Map<String, String> myMap = new HashMap<>();

        myMap.put("1", "Event 1");
        myMap.put("2", "Event 2");

        MyEvent test=new MyEvent("1","Event 1","This is Event 1",startDate,endDate,myMap,uri2);
        MyEventStub testGetter= new MyEventStub();

        MyEvent test2= testGetter.getList(1);
        Assert.assertEquals(test,test2);


    }

    @Test
    public void MyEventValid2()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2023,3,3,0,0,0);
        Date dateObj = calendar.getTime();
        Timestamp startDate = new Timestamp(dateObj);
        calendar.set(2023,3,4,0,0,0);
        dateObj = calendar.getTime();
        Timestamp endDate = new Timestamp(dateObj);

        Map<String, String> myMap = new HashMap<>();

        myMap.put("1", "Event 1");
        myMap.put("2", "Event 2");

        MyEvent test=new MyEvent("2","Event 2","This is Event 2",startDate,endDate,myMap,uri2);
        MyEventStub testGetter= new MyEventStub();

        MyEvent test2= testGetter.getList(2);
        Assert.assertEquals(test,test2);


    }


    @Test
    public void MyEventInvalid1()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2023,3,3,0,0,0);
        Date dateObj = calendar.getTime();
        Timestamp startDate = new Timestamp(dateObj);
        Timestamp endDate = new Timestamp(dateObj);

        Map<String, String> myMap = new HashMap<>();
        myMap.put("1", "Event 1");
        myMap.put("2", "Event 2");

        MyEvent test=new MyEvent("2","Event 3","This is Event 3",startDate,endDate,myMap,uri2);
        MyEventStub testGetter= new MyEventStub();

        MyEvent test2= testGetter.getList(1);
        Assert.assertNotEquals(test,test2);


    }

    @Test
    public void MyEventInvalid2()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2023,3,3,0,0,0);
        Date dateObj = calendar.getTime();
        Timestamp startDate = new Timestamp(dateObj);
        Timestamp endDate = new Timestamp(dateObj);

        Map<String, String> myMap = new HashMap<>();
        myMap.put("1", "Event 1");
        myMap.put("2", "Event 2");

        MyEvent test=new MyEvent("2","Event 4","This is Event 4",startDate,endDate,myMap,uri2);
        MyEventStub testGetter= new MyEventStub();

        MyEvent test2= testGetter.getList(2);
        Assert.assertNotEquals(test,test2);


    }
}
