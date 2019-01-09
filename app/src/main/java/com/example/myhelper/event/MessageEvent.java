package com.example.myhelper.event;

/**
 * Created by Administrator on 2019/1/9.
 */

public class MessageEvent {
    private String message;
    public  MessageEvent(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
