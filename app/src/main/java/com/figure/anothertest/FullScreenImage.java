package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FullScreenImage extends AppCompatActivity {
    ImageView fullimage;
    androidx.appcompat.widget.Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        fullimage = findViewById(R.id.fullscreenimg);
        tb = findViewById(R.id.fullimgtb);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imguri"));

        try {
            final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            fullimage.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
