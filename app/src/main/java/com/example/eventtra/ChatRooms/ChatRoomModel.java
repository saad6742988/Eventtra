package com.example.eventtra.ChatRooms;

import android.util.Log;

import com.google.firebase.Timestamp;

public class ChatRoomModel implements Comparable<ChatRoomModel> {
    private String roomId,roomName;
    private String message;
    private Timestamp timeStamp;

    public ChatRoomModel(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.message="";
        this.timeStamp=null;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        roomName = roomName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int compareTo(ChatRoomModel o) {
        Log.d("timeStamp chaeck", timeStamp+" : "+timeStamp);
        if (timeStamp.compareTo(o.timeStamp)==0)
            return 0;
        else if (timeStamp.compareTo(o.timeStamp)>0)
            return 1;
        else
            return -1;
    }
}
