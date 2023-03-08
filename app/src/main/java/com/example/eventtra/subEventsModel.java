package com.example.eventtra;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.util.PrimitiveIterator;

public class subEventsModel {
    private String subEventId;
    private String name; //name
    private String desc; //description
    private String price;   //price
    private String head;
    private Uri pic;
    private String mainEventId;
    private String subEventDate;
    private String subEventTime;

    //constructor

    public subEventsModel() {
    }

    public subEventsModel(String name, String desc, String price, String head, Uri pic, String mainEventId, String subEventDate,String subEventTime) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.head = head;
        this.pic = pic;
        this.mainEventId = mainEventId;
        this.subEventDate = subEventDate;
        this.subEventTime=subEventTime;
    }

    public subEventsModel(String name, String head, String mainEventId) {
        this.name = name;
        this.head = head;
        this.mainEventId = mainEventId;
        this.desc = "";
        this.price = "";
        this.pic = null;
        this.subEventDate="";
        this.subEventTime="";
    }

    @Exclude
    public String getSubEventId() {
        return subEventId;
    }

    public void setSubEventId(String subEventId) {
        this.subEventId = subEventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Exclude
    public Uri getPic() {
        return pic;
    }

    public void setPic(Uri pic) {
        this.pic = pic;
    }


    public String getMainEventId() {
        return mainEventId;
    }

    public void setMainEventId(String mainEventId) {
        this.mainEventId = mainEventId;
    }

    public String getSubEventDate() {
        return subEventDate;
    }

    public void setSubEventDate(String subEventDate) {
        this.subEventDate = subEventDate;
    }

    public String getSubEventTime() {
        return subEventTime;
    }

    public void setSubEventTime(String subEventTime) {
        this.subEventTime = subEventTime;
    }

    @Override
    public String toString() {
        return "subEventsModel{" +
                "subEventId='" + subEventId + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price='" + price + '\'' +
                ", head='" + head + '\'' +
                ", pic=" + pic +
                ", mainEventId='" + mainEventId + '\'' +
                ", subEventDate='" + subEventDate + '\'' +
                ", subEventTime='" + subEventTime + '\'' +
                '}';
    }
}
