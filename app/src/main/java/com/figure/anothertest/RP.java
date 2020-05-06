package com.figure.anothertest;

import android.graphics.Bitmap;
import android.net.Uri;

public class RP {
    private boolean isPicture;
    private Bitmap bitmap;
    Uri videouri;

    RP(Bitmap image){
        this.bitmap = image;
        isPicture = true;

    }
    RP(Uri video){
        this.videouri = video;
        isPicture = false;

    }

    boolean getIsPicture(){ return this.isPicture;}

    Bitmap getBitmap(){return this.bitmap;}

    Uri getVideouri(){return this.videouri;}

}
