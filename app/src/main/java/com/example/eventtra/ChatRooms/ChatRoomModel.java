package com.example.eventtra.ChatRooms;

public class ChatRoomModel {
    private String roomId,roomName;

    public ChatRoomModel(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
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
}
