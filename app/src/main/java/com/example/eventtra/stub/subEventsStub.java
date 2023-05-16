package com.example.eventtra.stub;


import android.net.Uri;

import com.example.eventtra.Models.subEventsModel;


import java.util.Hashtable;


public class subEventsStub {



    private Hashtable<Integer, subEventsModel> table;
    public subEventsStub() {
        String uritemp="https://www.example.com/path/to/resource?query=value";
        Uri uri2=Uri.parse(uritemp);
        table=new Hashtable<Integer, subEventsModel>();

        table.put(1, new subEventsModel("1","Dota 2","This is Esports","100","Awais", uri2,"1","2/3/2023","16:16:16",5,false,false,""));
        table.put(2, new subEventsModel("2","Cricket","This is Cricket","100","Awais", uri2,"1","2/3/2023","16:16:16",5,true,false,""));


    }
    public subEventsModel getList(int id) {

        return table.get(id);

    }
}
