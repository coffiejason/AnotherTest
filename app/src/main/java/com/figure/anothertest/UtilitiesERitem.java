package com.figure.anothertest;

import com.google.android.gms.maps.model.LatLng;

public class UtilitiesERitem {
    String customerName;
    String meterNum;
    LatLng location;
    String town;
    String isIndoor;

    UtilitiesERitem(String custmrName,String meterN,LatLng loc,String town,String isIndoor){
        this.customerName = custmrName;
        this.meterNum = meterN;
        this.location = loc;
        this.town = town;
        this.isIndoor = isIndoor;
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

    public String getIsIndoor() { return isIndoor; }
}
