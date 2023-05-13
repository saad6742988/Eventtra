package com.example.eventtra;

import com.example.eventtra.stub.PaymentInfoStub;


import org.junit.Assert;
import org.junit.Test;

public class PaymentInfoTest {

    @Test
    public void PaymentValid1(){
        PaymentInfo test= new PaymentInfo("1","2","Awais",false,100,"Fatima","12345678901","1","Dota 2",12345);

        PaymentInfoStub testGetter=new PaymentInfoStub();
        PaymentInfo test2= testGetter.getList(1);

        Assert.assertEquals(test,test2);
    }

    //"2","3","tester",true,200,"Awais","12355578901","2","Cricket",13345
    @Test
    public void PaymentValid2(){
        PaymentInfo test= new PaymentInfo("2","3","tester",true,200,"Awais","12355578901","2","Cricket",13345);

        PaymentInfoStub testGetter=new PaymentInfoStub();
        PaymentInfo test2= testGetter.getList(2);

        Assert.assertEquals(test,test2);
    }

    @Test
    public void PaymentInvalid1(){
        PaymentInfo test= new PaymentInfo("4","4","tester 3",false,100,"Awais","12345678901","1","Football",12345);

        PaymentInfoStub testGetter=new PaymentInfoStub();
        PaymentInfo test2= testGetter.getList(1);

        Assert.assertNotEquals(test,test2);
    }
    @Test
    public void PaymentInvalid2(){
        PaymentInfo test= new PaymentInfo("5","5","tester 3",false,100,"Awais","12345678901","1","Football",12345);

        PaymentInfoStub testGetter=new PaymentInfoStub();
        PaymentInfo test2= testGetter.getList(1);

        Assert.assertNotEquals(test,test2);
    }
}
