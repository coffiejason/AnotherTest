package com.figure.anothertest;

public class UtilGenItem {
    private String tasknum,tiperID,message,posterID,status;


    UtilGenItem(String tiperID,String message,String tasknum,String poster){
        this.tasknum = tasknum;
        this.tiperID = tiperID;
        this.message = message;
        this.posterID = poster;
    }

    UtilGenItem(String tiperID,String message,String tasknum,String poster,String status){
        this.tasknum = tasknum;
        this.tiperID = tiperID;
        this.message = message;
        this.posterID = poster;
        this.status = status;
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

    public String getPosterID(){
        return posterID;
    }

    public String getStatus(){ return status; }


}
