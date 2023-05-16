package com.example.eventtra;

import android.net.Uri;

import com.example.eventtra.Models.MyUser;
import com.example.eventtra.stub.MyUserStub;

import org.junit.Assert;
import org.junit.Test;


public class MyUserTest {
    //id:00013
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
    //id:00014
    @Test
    public void MyUserValid2()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);


        MyUser test=new MyUser("Muhammad","Saad","m.saad.147@gmail.com","03331111111","attendee","1",uri2,"token 1");
        MyUserStub testGetter= new MyUserStub();

        MyUser test2= testGetter.getList(2);
        Assert.assertEquals(test,test2);


    }
    //id:00015
    @Test
    public void MyUserInValid1()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);


        MyUser test=new MyUser("test1","test1","awaishahid.147@gmail.com","03061601266","attendee","1",uri2,"token 1");
        MyUserStub testGetter= new MyUserStub();

        MyUser test2= testGetter.getList(1);
        Assert.assertNotEquals(test,test2);


    }
    //id:00016
    @Test
    public void MyUserInValid2()
    {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);


        MyUser test=new MyUser("test2","test2","awaishahid.147@gmail.com","03061601266","attendee","1",uri2,"token 1");
        MyUserStub testGetter= new MyUserStub();

        MyUser test2= testGetter.getList(1);
        Assert.assertNotEquals(test,test2);


    }

}
