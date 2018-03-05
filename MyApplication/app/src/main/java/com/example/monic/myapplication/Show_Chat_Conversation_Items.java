package com.example.monic.myapplication;

/**
 * Created by monic on 2017/9/27.
 */

public class Show_Chat_Conversation_Items {
    private String message;
    private  String sender;
    public Show_Chat_Conversation_Items(){}
    public Show_Chat_Conversation_Items(String message,String sender){
        this.message=message;
        this.sender=sender;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message=message;
    }
    public String getSender(){
        return sender;
    }
    public void setSender(String sender){
        this.sender=sender;
    }
}
