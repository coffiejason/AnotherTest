package com.figure.anothertest.models;

import com.google.android.gms.maps.model.LatLng;

public class Post {
    private String message;
    private LatLng location;

    public Post(String msg, LatLng loc){
        this.location = loc;
        this.message = msg;
    }

    public String getMessage(){return message; }

    public void setMessage(String msg){this.message = msg;}

    public LatLng getLocation(){return location;}

    public void setLocation(LatLng loc){this.location = loc;}
}
