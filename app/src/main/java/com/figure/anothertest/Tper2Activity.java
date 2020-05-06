package com.figure.anothertest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Tper2Activity extends AppCompatActivity {

    RelativeLayout camerabtn;
    RelativeLayout videobtn;
    RelativeLayout audiobtn;

    RecyclerView rv;

    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Boolean ispicure;

    private ImageView testimg;
    private VideoView testvideo;
    private boolean zoomout = false;

    List<RP> rpList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tper2);

        rv = findViewById(R.id.rv_data_collected);

        camerabtn = findViewById(R.id.post_pic_button);
        audiobtn = findViewById(R.id.post_audio_btn);
        videobtn = findViewById(R.id.post_video_btn);

        //testimg = findViewById(R.id.testimage);
        //testvideo = findViewById(R.id.testvideo);


        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        videobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakeVideoIntent();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //add new photos and videos to an a list and show a show them in a listview

        if(!ispicure){
            if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
                Uri videoUri = intent.getData();
                rpList.add(new RP(videoUri));
                //testvideo.setVideoURI(videoUri);
                /*
                testvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                        testvideo.start();
                    }
                });*/
            }
        }
        else{
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                rpList.add(new RP(imageBitmap));
                //testimg.setImageBitmap(imageBitmap);
            }
        }

        //Log.d("iwqehjd","images: "+imagelist.size()+" videos: "+videolist.size());
        DataCollecionRVAdapter adapter = new DataCollecionRVAdapter(this,rpList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void dispatchTakePictureIntent() {
        ispicure = true;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakeVideoIntent() {
        ispicure = false;
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }




}
