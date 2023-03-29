package com.example.eventtra;
import org.junit.Assert;
import org.junit.Test;


import static org.junit.Assert.*;

import android.util.Log;

import com.example.eventtra.stub.eventRequestStub;

import java.util.ArrayList;
import java.util.Hashtable;

public class adminEventRequestsTest {
    @Test
    public void populateListValid()
    {


        EventRequestModel test=new EventRequestModel("1", "Daira","This is Daira", "Reject" , "11");

        eventRequestStub testGetter=new eventRequestStub();
        EventRequestModel test2= testGetter.getList(1);
        Log.d("test 2", ""+test2);
        Assert.assertEquals(test,test2);


    }

    @Test
    public void populateListInvalid() {
        EventRequestModel test=new EventRequestModel("1", "Daira2","This is Daira", "Reject" , "11");

        eventRequestStub testGetter=new eventRequestStub();
        EventRequestModel test2= testGetter.getList(1);
        Log.d("test 2", ""+test2);
        Assert.assertNotEquals(test,test2);

    }
}
