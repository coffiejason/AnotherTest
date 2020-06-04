package com.figure.anothertest;

import java.util.List;

public class ErrandResItem {
    String message;
    List<String> uriNames;

    ErrandResItem(String msg,List<String> uris){
        this.message = msg;
        this.uriNames = uris;
    }

    public List<String> getUriNames() { return uriNames; }

    public String getTitle() { return message; }
}
