package com.example.eventtra;

import android.net.Uri;

import com.example.eventtra.stub.MyUserStub;

import org.junit.Assert;
import org.junit.Test;


public class MyUserTest {
    @Test
    public void MyUserValid()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);


        MyUser test=new MyUser("Awais","Shahid","awaishahid.147@gmail.com","03061601266","attendee","1",uri2,"token 1");
        MyUserStub testGetter= new MyUserStub();

        MyUser test2= testGetter.getList(1);
        Assert.assertEquals(test,test2);


    }
    @Test
    public void MyUserInValid()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);


        MyUser test=new MyUser("Muhammad","Saad","awaishahid.147@gmail.com","03061601266","attendee","1",uri2,"token 1");
        MyUserStub testGetter= new MyUserStub();

        MyUser test2= testGetter.getList(1);
        Assert.assertNotEquals(test,test2);


    }
}
