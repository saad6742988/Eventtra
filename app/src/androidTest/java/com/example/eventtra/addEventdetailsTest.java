package com.example.eventtra;

import org.junit.Test;


import static org.junit.Assert.*;



public class addEventdetailsTest {

    //ID:00001
    @Test
    public void testValidDate1() {
        addEventdetails activity = new addEventdetails();
        int year1 = 2023;
        int month1 = 3;
        int day1 = 29;
        int year2 = 2023;
        int month2 = 3;
        int day2 = 30;

        System.out.println("Input values: " + year1 + "-" + month1 + "-" + day1 + ", " + year2 + "-" + month2 + "-" + day2);

        assertTrue("Expected output: true", activity.validDate(year1, month1, day1, year2, month2, day2));
    }

    //ID:00002
    @Test
    public void testValidDate2() {
        addEventdetails activity = new addEventdetails();
        int year1 = 2023;
        int month1 = 4;
        int day1 = 15;
        int year2 = 2023;
        int month2 = 5;
        int day2 = 20;

        System.out.println("Input values: " + year1 + "-" + month1 + "-" + day1 + ", " + year2 + "-" + month2 + "-" + day2);

        assertTrue("Expected output: true", activity.validDate(year1, month1, day1, year2, month2, day2));
    }

    //ID:00003
    @Test
    public void testInvalidDate1() {
        addEventdetails activity = new addEventdetails();


        int year1 = 2023;
        int month1 = 4;
        int day1 = 1;
        int year2 = 2023;
        int month2 = 3;
        int day2 = 30;

        System.out.println("Input values: " + year1 + "-" + month1 + "-" + day1 + ", " + year2 + "-" + month2 + "-" + day2);

        assertFalse("Expected output: true", activity.validDate(year1, month1, day1, year2, month2, day2));
    }
    //ID:00004
    @Test
    public void testInvalidDate2() {
        addEventdetails activity = new addEventdetails();


        int year1 = 2023;
        int month1 = 5;
        int day1 = 2;
        int year2 = 2023;
        int month2 = 3;
        int day2 = 30;

        System.out.println("Input values: " + year1 + "-" + month1 + "-" + day1 + ", " + year2 + "-" + month2 + "-" + day2);

        assertFalse("Expected output: true", activity.validDate(year1, month1, day1, year2, month2, day2));
    }
}
