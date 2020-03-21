package com.figure.anothertest;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class PostClusterItem implements ClusterItem {

    private String title;
    private LatLng latLng;

    public PostClusterItem(String title, LatLng latLng) {
        this.title = title;
        this.latLng = latLng;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
