package com.example.eventtra;

import android.net.Uri;

import com.example.eventtra.Models.subEventsModel;
import com.example.eventtra.stub.subEventsStub;

import org.junit.Assert;
import org.junit.Test;

public class subEventRequestsTest {

    @Test
    public void subEventRequestsValid1()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);

        subEventsModel test=new subEventsModel("1","Dota 2","This is Esports","100","Awais", uri2,"1","2/3/2023","16:16:16",5,false,false,"");
        subEventsStub testGetter= new subEventsStub();

        subEventsModel test2= testGetter.getList(1);
        Assert.assertEquals(test,test2);


    }
    @Test
    public void subEventRequestsValid2()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);

        subEventsModel test=new subEventsModel("2","Cricket","This is Cricket","100","Awais", uri2,"1","2/3/2023","16:16:16",5,true,false,"");
        subEventsStub testGetter= new subEventsStub();

        subEventsModel test2= testGetter.getList(2);
        Assert.assertEquals(test,test2);


    }

    @Test
    public void subEventRequestsNotValid1()
    {

        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);
        subEventsModel test=new subEventsModel("1","Dota 3","This is not released","100","Awais", uri2,"1","2/3/2023","16:16:16",5,false,false,"");
        subEventsStub testGetter= new subEventsStub();

        subEventsModel test2= testGetter.getList(1);
        Assert.assertNotEquals(test,test2);


    }


    @Test
    public void subEventRequestsNotValid2()
    {

        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);
        subEventsModel test=new subEventsModel("1","Tekken 7","This is not released","100","Awais", uri2,"1","2/3/2023","16:16:16",5,false,false,"");
        subEventsStub testGetter= new subEventsStub();

        subEventsModel test2= testGetter.getList(1);
        Assert.assertNotEquals(test,test2);


    }
}
