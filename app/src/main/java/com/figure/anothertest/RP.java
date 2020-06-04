package com.figure.anothertest;

import android.graphics.Bitmap;
import android.net.Uri;

public class RP {
    private boolean isPicture;
    private Bitmap bitmap;
    private Uri imageUri;
    Uri videouri;

    RP(Bitmap image){
        this.bitmap = image;
        isPicture = true;

    }
    RP(Uri image,Boolean isPic){
        this.imageUri = image;
        isPicture = true;
    }
    RP(Uri video){
        this.videouri = video;
        isPicture = true;
    }

    boolean getIsPicture(){ return this.isPicture;}

    Bitmap getBitmap(){return this.bitmap;}

    Uri getImageUri(){return this.imageUri;}

    Uri getVideouri(){return this.videouri;}
    
}
