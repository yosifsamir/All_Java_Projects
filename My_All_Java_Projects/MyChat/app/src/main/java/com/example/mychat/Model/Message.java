package com.example.mychat.Model;

public class Message {
    private String messageId;
    private String uId;
    private String roomId;
    private String message;
    private String created_at;

    public Message() {
    }

    public Message(String messageId, String uId, String roomId, String message, String created_at) {
        this.messageId = messageId;
        this.uId = uId;
        this.roomId = roomId;
        this.message = message;
        this.created_at = created_at;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
