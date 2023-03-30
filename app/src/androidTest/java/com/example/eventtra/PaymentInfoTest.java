package com.example.eventtra;

import com.example.eventtra.stub.PaymentInfoStub;


import org.junit.Assert;
import org.junit.Test;

public class PaymentInfoTest {

    @Test
    public void PaymentValid(){
        PaymentInfo test= new PaymentInfo("1","2","Awais",false,100,"Fatima","12345678901","1","Dota 2",12345);

        PaymentInfoStub testGetter=new PaymentInfoStub();
        PaymentInfo test2= testGetter.getList(1);

        Assert.assertEquals(test,test2);
    }

    @Test
    public void PaymentInvalid(){
        PaymentInfo test= new PaymentInfo("4","4","tester 3",false,100,"Awais","12345678901","1","Football",12345);

        PaymentInfoStub testGetter=new PaymentInfoStub();
        PaymentInfo test2= testGetter.getList(1);

        Assert.assertNotEquals(test,test2);
    }
}
