package com.figure.anothertest;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class Tper2Activity extends AppCompatActivity {

    RelativeLayout camerabtn,videobtn,audiobtn,finishbtn;


    RecyclerView rv;
    TextView errandmsg;

    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Boolean ispicure;

    String filename;
    Uri imageUri;
    private boolean zoomout = false;

    List<RP> rpList = new ArrayList<>();

    String userID;
    String eventID; //write eventID to the errands node of the tiper, tiper can access media by storagereference.child(eventid)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tper2);

        errandmsg = findViewById(R.id.errandmsg);

        String errandmessage = getIntent().getStringExtra("msg");
        String tiperId = getIntent().getStringExtra("tid");
        if(errandmessage != null){
            errandmsg.setText(errandmessage);
        }

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        eventID = userID+""+System.currentTimeMillis();



        rv = findViewById(R.id.rv_data_collected);

        camerabtn = findViewById(R.id.post_pic_button);
        audiobtn = findViewById(R.id.post_audio_btn);
        videobtn = findViewById(R.id.post_video_btn);
        finishbtn = findViewById(R.id.finisherrandbtn);

        ActivityCompat.requestPermissions(Tper2Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);


        filename = Environment.getExternalStorageDirectory().getPath() + "/test/testfile.jpg";
        imageUri = Uri.fromFile(new File(filename));


        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
                Log.d("ksbxn",""+new Functions().getImagesNames().size());
            }
        });

        videobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakeVideoIntent();
            }
        });

        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write image uris to tipers node;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            rpList.add(new RP(imageUri,true));
        }
        DataCollecionRVAdapter adapter = new DataCollecionRVAdapter(this,rpList,eventID);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void dispatchTakePictureIntent() {
        ispicure = true;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                    imageUri);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new Functions().clearImageNames();

        finish();
    }
}
