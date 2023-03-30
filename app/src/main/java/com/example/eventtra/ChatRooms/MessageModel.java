package com.example.eventtra.ChatRooms;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;

public class MessageModel {
    private String messageId;
    private String message;
    private String email;
    private int priority;
    private Timestamp timeStamp;
    public MessageModel() {
    }

    public MessageModel(String message, String email, int priority) {
        this.message = message;
        this.email = email;
        this.priority = priority;
    }

    @Exclude
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "Key='" + messageId + '\'' +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
