package com.example.eventtra;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class MyEvent {
    private String eventId;
    private String eventName;
    private String eventDes;
    private String startDate;
    private String endDate;
    private Map<String,String> subEvents;

    public MyEvent() {
    }


    public MyEvent(String eventName, String eventDes, String startDate, String endDate, Map<String, String> subEvents) {
        this.eventName = eventName;
        this.eventDes = eventDes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subEvents = subEvents;
    }

    public MyEvent(String eventName, String eventDes, String startDate, String endDate) {
        this.eventName = eventName;
        this.eventDes = eventDes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subEvents = null;
    }

    @Exclude
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDes() {
        return eventDes;
    }

    public void setEventDes(String eventDes) {
        this.eventDes = eventDes;
    }

    public Map<String, String> getSubEvents() {
        return subEvents;
    }

    public void setSubEvents(Map<String, String> subEvents) {
        this.subEvents = subEvents;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }


    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDes='" + eventDes + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", subEvents=" + subEvents +
                '}';
    }
}
