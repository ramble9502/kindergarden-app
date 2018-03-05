package com.example.monic.myapplication;

/**
 * Created by monic on 2017/11/4.
 */

public class EbookNumber {
    public String ebookurl;
    public String ebookname;
    public String ebookauthor;

    public EbookNumber(String url,String name,String author){
        this.ebookname=name;
        this.ebookurl=url;
        this.ebookauthor=author;
    }
    public EbookNumber(){}
    public String getEbookurl(){
        return ebookurl;
    }
    public void setEbookurl(String url){
        ebookurl=url;
    }
    public String getEbookname(){
        return ebookname;
    }
    public void setEbookname(String name){
        ebookname=name;
    }
    public String getEbookauthor(){return ebookauthor;}
    public void setEbookauthor(String author){ebookauthor=author;}
}
