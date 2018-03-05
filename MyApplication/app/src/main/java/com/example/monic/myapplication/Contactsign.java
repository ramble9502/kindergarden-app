package com.example.monic.myapplication;

/**
 * Created by monic on 2017/12/6.
 */

public class Contactsign {
    public String downloadurl;
    public String usercreate;
    public Contactsign(){}
    public Contactsign(String download,String email){
        this.downloadurl=download;
        this.usercreate=email;
    }
    public void setDownloadurl(String url){
        downloadurl=url;
    }
    public String getDownloadurl(){
        return downloadurl;
    }
    public void setUsercreate(String email){
        usercreate=email;
    }
    public String getUsercreate(){
        return usercreate;
    }

}
