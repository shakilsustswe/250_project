package com.shakilsustswe.locationtracker;

public class Messages {
    String senderId;
    String message;
    long time;
    String reciverId;


    public Messages() {
    }

    public Messages(String message, long time, String reciverId) {
        this.message = message;
        this.time = time;
        this.reciverId = reciverId;
    }

    public Messages(String senderId, String message, long time) {
        this.senderId = senderId;
        this.message = message;
        this.time = time;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
