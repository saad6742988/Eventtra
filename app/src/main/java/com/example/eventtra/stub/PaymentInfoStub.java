package com.example.eventtra.stub;

import com.example.eventtra.EventRequestModel;
import com.example.eventtra.PaymentInfo;

import java.util.Hashtable;

public class PaymentInfoStub {
    private Hashtable<Integer, PaymentInfo> table;
    public PaymentInfoStub() {
        table=new Hashtable<Integer, PaymentInfo>();
        table.put(1, new PaymentInfo("1","2","Awais",false,100,"Fatima","12345678901","1","Dota 2",12345));
        table.put(2, new PaymentInfo("2","3","tester",true,200,"Awais","12355578901","2","Cricket",13345));

    }

    public PaymentInfo getList(int id) {

        return table.get(id);

    }
}
