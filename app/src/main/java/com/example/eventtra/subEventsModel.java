package com.example.eventtra;

import com.google.firebase.firestore.Exclude;

import java.util.PrimitiveIterator;

public class subEventsModel {
    private String subEventId;
    private String name; //name
    private String desc; //description
    private String price;   //price
    private String head;
    private String pic;
    private String mainEventId;
    private int p;

    //constructor

    public subEventsModel() {
    }


    public subEventsModel(String name, String desc, String price, String head, String pic, String mainEventId) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.head = head;
        this.pic = pic;
        this.mainEventId = mainEventId;
    }

    public subEventsModel(String name, String head,String mainEventId) {
        this.name = name;
        this.head = head;
        this.mainEventId = mainEventId;
        this.desc = null;
        this.price = null;
        this.pic = null;
    }

    public subEventsModel(int pic, String text, String desc, String price) {
        this.p = pic;
        this.name = text;
        this.desc = desc;
        this.price = price;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getP() {
        return p;
    }

    public String getMainEventId() {
        return mainEventId;
    }

    public void setMainEventId(String mainEventId) {
        this.mainEventId = mainEventId;
    }

    public void setP(int p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return "subEventsModel{" +
                "subEventId='" + subEventId + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price='" + price + '\'' +
                ", head='" + head + '\'' +
                ", pic='" + pic + '\'' +
                ", mainEventId='" + mainEventId + '\'' +
                '}';
    }
}
