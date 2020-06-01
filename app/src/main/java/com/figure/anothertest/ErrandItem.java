package com.figure.anothertest;

public class ErrandItem {
    String tiperID,message;

    ErrandItem(String tiperID, String msg){
        this.tiperID = tiperID;
        this.message = msg;
    }

    public String getErrandMessage() {
        return message;
    }

    public String getTiperID() {
        return tiperID;
    }
}
