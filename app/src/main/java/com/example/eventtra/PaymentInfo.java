package com.example.eventtra;

import com.google.firebase.firestore.Exclude;

public class PaymentInfo {
    private String id;
    private String tid;
    private String madeBy;
    private Boolean status;
    private int amount;
    private String participantName,participantCnic,subEventID,subEventName;
    private long timeStamp;

    public PaymentInfo(String id, String tid, String madeBy, int totalParticipants, Boolean status, int totalAmount) {
        this.id = id;
        this.tid = tid;
        this.madeBy = madeBy;
        this.status = status;
        this.amount = totalAmount;
    }

    public PaymentInfo(String id, String tid, String madeBy, Boolean status, int amount, String participantName, String participantCnic, String subEventID, String subEventName, long timeStamp) {
        this.id = id;
        this.tid = tid;
        this.madeBy = madeBy;
        this.status = status;
        this.amount = amount;
        this.participantName = participantName;
        this.participantCnic = participantCnic;
        this.subEventID = subEventID;
        this.subEventName = subEventName;
        this.timeStamp = timeStamp;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
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
                ", timeStamp=" + timeStamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentInfo that = (PaymentInfo) o;

        if (amount != that.amount) return false;
        if (timeStamp != that.timeStamp) return false;
        if (!id.equals(that.id)) return false;
        if (!tid.equals(that.tid)) return false;
        if (!madeBy.equals(that.madeBy)) return false;
        if (!status.equals(that.status)) return false;
        if (!participantName.equals(that.participantName)) return false;
        if (!participantCnic.equals(that.participantCnic)) return false;
        if (!subEventID.equals(that.subEventID)) return false;
        return subEventName.equals(that.subEventName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + tid.hashCode();
        result = 31 * result + madeBy.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + amount;
        result = 31 * result + participantName.hashCode();
        result = 31 * result + participantCnic.hashCode();
        result = 31 * result + subEventID.hashCode();
        result = 31 * result + subEventName.hashCode();
        result = 31 * result + (int) (timeStamp ^ (timeStamp >>> 32));
        return result;
    }
}
