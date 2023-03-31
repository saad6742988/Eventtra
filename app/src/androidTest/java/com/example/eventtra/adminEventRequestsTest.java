package com.example.eventtra;
import org.junit.Assert;
import org.junit.Test;


import static org.junit.Assert.*;

import android.util.Log;

import com.example.eventtra.stub.eventRequestStub;

import java.util.ArrayList;
import java.util.Hashtable;

public class adminEventRequestsTest {

    //ID:00005
    @Test
    public void dataValid1()
    {


        EventRequestModel test=new EventRequestModel("1", "Daira","This is Daira", "Reject" , "11");

        eventRequestStub testGetter=new eventRequestStub();
        EventRequestModel test2= testGetter.getList(1);
        Log.d("test 2", ""+test2);
        Assert.assertEquals(test,test2);


    }
    //ID:00006
    @Test
    public void dataValid2()
    {


        EventRequestModel test=new EventRequestModel("2", "Daira2","This is Daira 2", "Accept" , "12");

        eventRequestStub testGetter=new eventRequestStub();
        EventRequestModel test2= testGetter.getList(2);
        Log.d("test 2", ""+test2);
        Assert.assertEquals(test,test2);


    }

    //ID:00007
    @Test
    public void dataInvalid1() {
        EventRequestModel test=new EventRequestModel("1", "Daira2","This is Daira", "Reject" , "11");

        eventRequestStub testGetter=new eventRequestStub();
        EventRequestModel test2= testGetter.getList(1);
        Log.d("test 2", ""+test2);
        Assert.assertNotEquals(test,test2);

    }
    //ID:00008
    @Test
    public void dataInvalid2() {
        EventRequestModel test=new EventRequestModel("1", "Daira3","This is Daira", "Reject" , "11");

        eventRequestStub testGetter=new eventRequestStub();
        EventRequestModel test2= testGetter.getList(1);
        Log.d("test 2", ""+test2);
        Assert.assertNotEquals(test,test2);

    }
}
