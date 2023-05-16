package com.example.eventtra.Models;

import com.google.firebase.firestore.Exclude;

public class EventRequestModel{
    private String requestId,requestName,requestDes,requestStatus,userID;

    public EventRequestModel(String requestName, String requestDes, String requestStatus, String userID) {
        this.requestName = requestName;
        this.requestDes = requestDes;
        this.requestStatus = requestStatus;
        this.userID = userID;
    }

    public EventRequestModel(String requestId, String requestName, String requestDes, String requestStatus, String userID) {
        this.requestId = requestId;
        this.requestName = requestName;
        this.requestDes = requestDes;
        this.requestStatus = requestStatus;
        this.userID = userID;
    }

    public EventRequestModel() {
    }

    @Exclude
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getRequestDes() {
        return requestDes;
    }

    public void setRequestDes(String requestDes) {
        this.requestDes = requestDes;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "EventRequestModel{" +
                "requestId='" + requestId + '\'' +
                ", requestName='" + requestName + '\'' +
                ", requestDes='" + requestDes + '\'' +
                ", requestStatus='" + requestStatus + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventRequestModel that = (EventRequestModel) o;

        if (!requestId.equals(that.requestId)) return false;
        if (!requestName.equals(that.requestName)) return false;
        if (!requestDes.equals(that.requestDes)) return false;
        if (!requestStatus.equals(that.requestStatus)) return false;
        return userID.equals(that.userID);
    }

    @Override
    public int hashCode() {
        int result = requestId.hashCode();
        result = 31 * result + requestName.hashCode();
        result = 31 * result + requestDes.hashCode();
        result = 31 * result + requestStatus.hashCode();
        result = 31 * result + userID.hashCode();
        return result;
    }
}
