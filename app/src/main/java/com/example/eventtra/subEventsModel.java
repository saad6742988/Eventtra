package com.example.eventtra;

import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;

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
    private int minParticipants;
    private boolean openRegistration;
    private boolean streamStatus;
    private String streamLink;
    private String category;
    private Timestamp eventTime;

    //constructor

    public subEventsModel() {
    }

    public subEventsModel(String subEventId, String name, String desc, String price, String head, Uri pic, String mainEventId, String subEventDate, String subEventTime, int minParticipants, boolean openRegistration, boolean streamStatus, String streamLink) {
        this.subEventId = subEventId;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.head = head;
        this.pic = pic;
        this.mainEventId = mainEventId;
        this.subEventDate = subEventDate;
        this.subEventTime = subEventTime;
        this.minParticipants = minParticipants;
        this.openRegistration = openRegistration;
        this.streamStatus = streamStatus;
        this.streamLink = streamLink;
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
        this.minParticipants=0;
        this.openRegistration = false;
        this.streamStatus = false;
        this.streamLink = "";
        this.category = "";
        this.eventTime = Timestamp.now();
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

    public int getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public boolean isOpenRegistration() {
        return openRegistration;
    }

    public void setOpenRegistration(boolean openRegistration) {
        this.openRegistration = openRegistration;
    }

    public boolean isStreamStatus() {
        return streamStatus;
    }

    public void setStreamStatus(boolean streamStatus) {
        this.streamStatus = streamStatus;
    }

    public String getStreamLink() {
        return streamLink;
    }

    public void setStreamLink(String streamLink) {
        this.streamLink = streamLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Timestamp getEventTime() {
        return eventTime;
    }

    public void setEventTime(Timestamp eventTime) {
        this.eventTime = eventTime;
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
                ", minParticipants=" + minParticipants +
                ", openRegistration=" + openRegistration +
                ", streamStatus=" + streamStatus +
                ", streamLink='" + streamLink + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        subEventsModel that = (subEventsModel) o;

        if (minParticipants != that.minParticipants) return false;
        if (openRegistration != that.openRegistration) return false;
        if (!subEventId.equals(that.subEventId)) return false;
        if (!name.equals(that.name)) return false;
        if (!desc.equals(that.desc)) return false;
        if (!price.equals(that.price)) return false;
        if (!head.equals(that.head)) return false;
        if (!pic.equals(that.pic)) return false;
        if (!mainEventId.equals(that.mainEventId)) return false;
        if (!subEventDate.equals(that.subEventDate)) return false;
        return subEventTime.equals(that.subEventTime);
    }

    @Override
    public int hashCode() {
        int result = subEventId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + desc.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + head.hashCode();
        result = 31 * result + pic.hashCode();
        result = 31 * result + mainEventId.hashCode();
        result = 31 * result + subEventDate.hashCode();
        result = 31 * result + subEventTime.hashCode();
        result = 31 * result + minParticipants;
        result = 31 * result + (openRegistration ? 1 : 0);
        return result;
    }
}
