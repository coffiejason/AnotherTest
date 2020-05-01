package com.figure.anothertest;

import java.sql.Timestamp;

public class Comment {
    private String message;
    private Timestamp time;
    private int commentid;

    Comment(String message){
        this.message = message;
    }

    Comment(String message,Timestamp time,int commentid){
        this.message = message;
        this.time = time;
        this.commentid = commentid;
    }

    public String getMessage(){return message;}
    public Timestamp getTime(){return time;}
    public int getCommentid(){return commentid;}



}
