package com.michelle.goldwin.tpamobile.object;

import java.util.Date;

/**
 * Created by Michelle Neysa on 12/5/2016.
 */

public class ChatMessage {

    private String message;
    private String sender;
    private String receiver;
    private long time;
    private String id;

    public ChatMessage(){


    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public ChatMessage(String message, String sender,String receiver, String id){
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.id = id;
        time = new Date().getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
