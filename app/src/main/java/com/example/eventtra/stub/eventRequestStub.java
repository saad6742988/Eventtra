package com.example.eventtra.stub;

import com.example.eventtra.Models.EventRequestModel;

import java.util.Hashtable;

public class eventRequestStub {




        private Hashtable<Integer, EventRequestModel> table;

        public eventRequestStub() {
            table=new Hashtable<Integer, EventRequestModel>();
            table.put(1, new EventRequestModel("1", "Daira","This is Daira", "Reject" , "11"));
            table.put(2, new EventRequestModel("2", "Daira2","This is Daira 2", "Accept" , "12"));

        }

        public EventRequestModel getList(int id) {

            return table.get(id);

        }






}
