package com.figure.anothertest;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;

public class TPPost {
    //implement timestamp and username feature
    //private String pUsername;
    //private Timestamp pTime;
    private String pUserID;
    private String pPostID;
    private String pMessage;
    private LatLng pLocation;
    private int radius;
    private boolean isErrand;

    TPPost(String message,double l, double g, String userID,String postid){
        pLocation = new LatLng(l,g);
        this.pMessage = message;
        this.pUserID = userID;
        this.pPostID = postid;
    }

    TPPost(String message,double l, double g, String userID,boolean isErrand){
        pLocation = new LatLng(l,g);
        this.pMessage = message;
        this.pUserID = userID;
        this.isErrand = isErrand;
    }

    TPPost(String message,double l, double g){
        pLocation = new LatLng(l,g);
        this.pMessage = message;
    }

    TPPost(String message, String userID,String postid){
        this.pMessage = message;
        this.pUserID = userID;
        this.pPostID = postid;
    }

    public void setpMessage(String message){
        this.pMessage = message;
    }

    public void setLocation(double l,double g){
        this.pLocation = new LatLng(l,g);
    }

    public void setRadius(int radius){
        this.radius = radius;
    }


    public  String getpMessage(){
        return this.pMessage;
    }

    LatLng getLocation(){
        return this.pLocation;
    }

    public int getRadius(){ return this.radius;}

    public boolean isErrand() { return isErrand; }

    public String getpUserID(){return this.pUserID;}

    public String getpPostID(){return this.pPostID;}

}
