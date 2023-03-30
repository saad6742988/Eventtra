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
    private Uri eventPic;

    public MyEvent() {
    }

    public MyEvent(String eventId, String eventName, String eventDes, String startDate, String endDate, Map<String, String> subEvents, Uri eventPic) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDes = eventDes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subEvents = subEvents;
        this.eventPic = eventPic;
    }

    public MyEvent(String eventName, String eventDes, String startDate, String endDate,Uri pic) {
        this.eventName = eventName;
        this.eventDes = eventDes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventPic=pic;
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

    @Exclude
    public Uri getEventPic() {
        return eventPic;
    }

    public void setEventPic(Uri eventPic) {
        this.eventPic = eventPic;
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDes='" + eventDes + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", subEvents=" + subEvents +
                ", eventPic=" + eventPic +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyEvent myEvent = (MyEvent) o;

        if (!eventId.equals(myEvent.eventId)) return false;
        if (!eventName.equals(myEvent.eventName)) return false;
        if (!eventDes.equals(myEvent.eventDes)) return false;
        if (!startDate.equals(myEvent.startDate)) return false;
        if (!endDate.equals(myEvent.endDate)) return false;
        if (!subEvents.equals(myEvent.subEvents)) return false;
        return eventPic.equals(myEvent.eventPic);
    }

    @Override
    public int hashCode() {
        int result = eventId.hashCode();
        result = 31 * result + eventName.hashCode();
        result = 31 * result + eventDes.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + subEvents.hashCode();
        result = 31 * result + eventPic.hashCode();
        return result;
    }
}
