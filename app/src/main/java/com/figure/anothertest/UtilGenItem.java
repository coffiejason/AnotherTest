package com.figure.anothertest;

public class UtilGenItem {
    private String tasknum,tiperID,message;


    UtilGenItem(String tiperID,String message,String tasknum){
        this.tasknum = tasknum;
        this.tiperID = tiperID;
        this.message = message;
    }

    public String tasknum() {
        return tasknum;
    }


    public String getTiperID() {
        return tiperID;
    }

    public String getMessage() {
        return message;
    }
}
