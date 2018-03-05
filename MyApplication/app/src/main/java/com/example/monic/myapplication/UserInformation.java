package com.example.monic.myapplication;

/**
 * Created by monic on 2017/9/22.
 */

public class UserInformation {
    private String Email;
    private String Name;
    private String Classroom;

    private long Likescount;

    public UserInformation(){}
    public UserInformation(String email,String name,String classroom){
        Email=email;
        Name=name;
        Classroom=classroom;
    }

    public UserInformation(String email,String name,String classroom,long likesCount){
        Email=email;
        Name=name;
        Classroom=classroom;
        Likescount=likesCount;
    }
    public String getEmail(){
        return Email;
    }
    public void setEmail(String email){
        Email=email;
    }
    public String getName(){
        return Name;
    }
    public void setName(String name){
        Name=name;
    }
    public String getClassroom(){
        return Classroom;
    }
    public void setClassroom(String classroom){
        Classroom=classroom;
    }
    public long getLikescount(){return Likescount;}
    public void setLikescount(long likescount){Likescount=likescount;}
}
