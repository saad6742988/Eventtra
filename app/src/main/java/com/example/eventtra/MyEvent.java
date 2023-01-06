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
    private Date startDate;
    private Date endDate;
    private Uri eventPicUri;
    private Map<String,String>[] subEvents;

    public MyEvent() {
    }


    public MyEvent(String eventId, String eventName, String eventDes, Date startDate, Date endDate, Uri eventPicUri, Map<String, String>[] subEvents) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDes = eventDes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventPicUri = eventPicUri;
        this.subEvents = subEvents;
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

    public Uri getEventPicUri() {
        return eventPicUri;
    }

    public void setEventPicUri(Uri eventPicUri) {
        this.eventPicUri = eventPicUri;
    }

    public Map<String, String>[] getSubEvents() {
        return subEvents;
    }

    public void setSubEvents(Map<String, String>[] subEvents) {
        this.subEvents = subEvents;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDes='" + eventDes + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", eventPicUri='" + eventPicUri + '\'' +
                ", subEvents=" + Arrays.toString(subEvents) +
                '}';
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
