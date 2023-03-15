package com.example.eventtra;

import com.google.firebase.firestore.Exclude;

public class EventRequestModel {
    private String requestId,requestName,requestDes,requestStatus,userID;

    public EventRequestModel(String requestName, String requestDes, String requestStatus, String userID) {
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
}
