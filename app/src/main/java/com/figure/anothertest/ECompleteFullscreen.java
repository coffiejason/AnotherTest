package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

public class ECompleteFullscreen extends AppCompatActivity {
    ImageView fullimage;
    VideoView fullview;
    androidx.appcompat.widget.Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_complete_fullscreen);

        fullimage = findViewById(R.id.fullscreenimg2);
        fullview = findViewById(R.id.fullscreenvideo2);
        tb = findViewById(R.id.fullimgtb);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
