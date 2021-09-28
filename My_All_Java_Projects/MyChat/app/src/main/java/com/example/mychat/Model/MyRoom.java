package com.example.mychat.Model;

public class MyRoom {
    private String Uid;
    private String Rid;

    public MyRoom() {
    }

    public MyRoom(String uid, String rid) {
        Uid = uid;
        Rid = rid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getRid() {
        return Rid;
    }

    public void setRid(String rid) {
        Rid = rid;
    }
}
