package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toolbar;
import android.widget.VideoView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FullScreenImage extends AppCompatActivity {
    ImageView fullimage;
    VideoView fullview;
    androidx.appcompat.widget.Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        fullimage = findViewById(R.id.fullscreenimg);
        fullview = findViewById(R.id.fullscreenvideo);
        tb = findViewById(R.id.fullimgtb);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Uri imageUri = Uri.parse(getIntent().getStringExtra("imguri"));

        String extension = new Functions().getExtension(this,imageUri);

        if (extension.equals("jpg")  || extension.equals("jpeg") || extension.equals("png") ) {
            fullview.setVisibility(View.GONE);

            try {
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                fullimage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(extension.equals("3gp")||extension.equals("mp4")){

            fullimage.setVisibility(View.GONE);

            MediaController mediaController= new MediaController(this);
            mediaController.setAnchorView(fullview);
            //Uri uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.one);
            fullview.setMediaController(mediaController);
            fullview.setVideoURI(imageUri);
            fullview.requestFocus();

            fullview.start();
        }






        /*
        */
    }
}
