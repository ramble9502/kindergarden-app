package com.example.monic.myapplication;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by monic on 2017/10/6.
 */

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private String messageTime;
    private String messageImg;

    public ChatMessage(String messageText, String messageUser,String messageImage) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageImg = messageImage;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date dt=new Date();
        String dts=sdf.format(dt);
        messageTime = dts;
    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public void setMessageImg(String messageImg){this.messageImg=messageImg;}

    public String getMessageImg(){return messageImg;}
}
