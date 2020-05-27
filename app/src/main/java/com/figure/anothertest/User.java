package com.figure.anothertest;

public class User {
    private double lat,lng;
    private String userID;

    User(double mLat,double mLng,String mUserID){
        this.lat = mLat;
        this.lng = mLng;
        this.userID = mUserID;

    }

    double getLat(){return this.lat;}
    double getLng(){return this.lng;}
    String getUserID(){return this.userID;}
}
