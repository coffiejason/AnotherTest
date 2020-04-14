package com.figure.anothertest;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;

public class TPPost {
    //implement timestamp and username feature
    //private String pUsername;
    //private Timestamp pTime;
    private String pMessage;
    private LatLng pLocation;

    TPPost(String message,double l, double g){
        pLocation = new LatLng(l,g);
        this.pMessage = message;
    }

    public void setpMessage(String message){
        this.pMessage = message;
    }

    public void setLocation(double l,double g){
        this.pLocation = new LatLng(l,g);
    }

    public String getpMessage(){
        return this.pMessage;
    }

    public LatLng getLocation(){
        return this.pLocation;
    }
}
