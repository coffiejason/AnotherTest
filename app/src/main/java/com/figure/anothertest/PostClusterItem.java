package com.figure.anothertest;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class PostClusterItem implements ClusterItem {

    private String title;
    private LatLng latLng;
    private String userid;
    private String postid;

    PostClusterItem(String title, LatLng latLng, String userid,String postid) {
        this.title = title;
        this.latLng = latLng;
        this.userid = userid;
        this.postid = postid;
    }

    PostClusterItem(String title, LatLng latLng, String userid) {
        this.title = title;
        this.latLng = latLng;
        this.userid = userid;
    }

    @Override
    public LatLng getPosition() { return latLng; }

    @Override
    public String getTitle() { return title; }

    @Override
    public String getSnippet() { return null; }

    public String getUserID() { return userid; }

    public String getPostid(){return this.postid;}




}
