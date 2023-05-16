package com.example.eventtra.stub;

import android.net.Uri;

import com.example.eventtra.Models.MyUser;


import java.util.Hashtable;


public class MyUserStub {
    private Hashtable<Integer, MyUser> table;
    public MyUserStub() {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);
        table=new Hashtable<Integer, MyUser>();

        table.put(1, new MyUser("Awais","Shahid","awaishahid.147@gmail.com","03061601266","attendee","1",uri2,"token 1"));
        table.put(2, new MyUser("Muhammad","Saad","m.saad.147@gmail.com","03331111111","attendee","1",uri2,"token 1"));


    }
    public MyUser getList(int id) {

        return table.get(id);

    }
}
