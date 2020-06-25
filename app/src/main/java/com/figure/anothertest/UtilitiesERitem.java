package com.figure.anothertest;

import com.google.android.gms.maps.model.LatLng;

public class UtilitiesERitem {
    String customerName;
    String meterNum;
    LatLng location;
    String town;

    UtilitiesERitem(String custmrName,String meterN,LatLng loc,String town){
        this.customerName = custmrName;
        this.meterNum = meterN;
        this.location = loc;
        this.town = town;
    }

    public String getMeterNum() {
        return meterNum;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getTown() {
        return town;
    }
}
