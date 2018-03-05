package com.example.monic.myapplication;

/**
 * Created by monic on 2017/9/22.
 */

public class UserDetail {
    public String textViewUserName;
    public String textViewChildname;
    public String textViewChildclass;
    public String textViewUsercontact;
    public String textViewUseraddress;
    public String imageurl;
    public String userdetailemail;
    public long likesCount;

    public UserDetail(){}
    public UserDetail(String textViewUserName,String textViewChildname,String textViewChildclass
            ,String textViewUsercontact,String textViewUseraddress,String imageurl,String email){
        this.textViewUserName=textViewUserName;
        this.textViewChildname=textViewChildname;
        this.textViewUsercontact=textViewUsercontact;
        this.textViewUseraddress=textViewUseraddress;
        this.textViewChildclass=textViewChildclass;
        this.imageurl=imageurl;
        this.userdetailemail=email;
    }
    public  String getTextViewUserName(){
        return textViewUserName;
    }
    public void setTextViewUserName(String name){
        textViewUserName=name;
    }


    public String getTextViewChildname(){
        return  textViewChildname;
    }
    public void setTextViewChildname(String childname){
        textViewChildname=childname;
    }


    public String getTextViewChildclass(){
        return  textViewChildclass;
    }
    public void setTextViewChildclass(String classroom){
        textViewChildclass=classroom;
    }


    public String getTextViewUsercontact(){
        return  textViewUsercontact;
    }
    public void setTextViewUsercontact(String contact){
        textViewUsercontact=contact;
    }


    public String getTextViewUseraddress(){
        return  textViewUseraddress;
    }
    public void  setTextViewUseraddress(String address){
        textViewUseraddress=address;
    }


    public String getImageurl(){
        return imageurl;
    }
    public void setImageurl(String image_url){
        imageurl=image_url;
    }


    public String getUserdetailemail(){
        return userdetailemail;
    }
    public void setUserdetailemail(String email){
        userdetailemail=email;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

}
