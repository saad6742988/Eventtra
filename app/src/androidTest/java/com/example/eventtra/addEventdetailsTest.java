package com.example.eventtra;

import org.junit.Test;


import static org.junit.Assert.*;



public class addEventdetailsTest {

    /**
     * Test case ID: addEventdetailsTest.testValidDate
     * Input Values: 2023, 3, 29,2023,3,30
     * Expected Output: true
     * Actual Output: true
     * Pass/Fail Status: Pass
     */
    @Test
    public void testValidDate() {
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


    @Test
    public void testInvalidDate() {
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
}
