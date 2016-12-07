package com.michelle.goldwin.tpamobile.chatinstructor;

import java.util.Date;

/**
 * Created by Michelle Neysa on 12/5/2016.
 */

public class ChatMessage {

    private String message;
    private String user;
    private long time;
    private String id;

    public ChatMessage(){


    }

    public ChatMessage(String message, String user, String id){
        this.message = message;
        this.user = user;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
