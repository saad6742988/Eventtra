package com.example.eventtra;

import com.google.firebase.firestore.Exclude;

public class PaymentInfo {
    private String id;
    private String tid;
    private String madeBy;
    private Boolean status;
    private int amount;
    private String participantName,participantCnic,subEventID,subEventName;

    public PaymentInfo(String id, String tid, String madeBy, int totalParticipants, Boolean status, int totalAmount) {
        this.id = id;
        this.tid = tid;
        this.madeBy = madeBy;
        this.status = status;
        this.amount = totalAmount;
    }

    public PaymentInfo() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(String madeBy) {
        this.madeBy = madeBy;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int totalAmount) {
        this.amount = totalAmount;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getParticipantCnic() {
        return participantCnic;
    }

    public void setParticipantCnic(String participantCnic) {
        this.participantCnic = participantCnic;
    }

    public String getSubEventID() {
        return subEventID;
    }

    public void setSubEventID(String subEventID) {
        this.subEventID = subEventID;
    }

    public String getSubEventName() {
        return subEventName;
    }

    public void setSubEventName(String subEventName) {
        this.subEventName = subEventName;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "id='" + id + '\'' +
                ", tid='" + tid + '\'' +
                ", madeBy='" + madeBy + '\'' +
                ", status=" + status +
                ", amount=" + amount +
                ", participantName='" + participantName + '\'' +
                ", participantCnic='" + participantCnic + '\'' +
                ", subEventID='" + subEventID + '\'' +
                ", subEventName='" + subEventName + '\'' +
                '}';
    }
}
