package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;

public class NamesnIDSActivity extends AppCompatActivity {
    Toolbar closebtn;
    RelativeLayout next,post_pic_button;
    FloatingActionButton fab;
    ImageView recievedImage;
    CircularImageView circularImageView;

    EditText name,idnumber;

    private static final int ACTION_TAKE_PHOTO = 1;
    private static final int ACTION_CAMERA_PHOTO = 2;

    String id_name, id_num;
    Uri profileImg;
    Bitmap idCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namesn_i_d_s);

        init();
    }

    void init(){
        next = findViewById(R.id.login);
        closebtn = findViewById(R.id.closebtn);
        fab = findViewById(R.id.fab_camera);
        circularImageView = findViewById(R.id.image_profile);
        post_pic_button = findViewById(R.id.post_pic_button);
        recievedImage = findViewById(R.id.recievedImage);
        name = findViewById(R.id.id_name);
        idnumber = findViewById(R.id.id_number);

        ActivityCompat.requestPermissions(NamesnIDSActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        closebtn.setOnClickListener(v -> onBackPressed());
        next.setOnClickListener(v -> checkAllData());

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, ACTION_TAKE_PHOTO);

        });

        post_pic_button.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, ACTION_CAMERA_PHOTO);
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == ACTION_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (resultData != null) {
                // this is the image selected by the user
                Uri imageUri = resultData.getData();
                profileImg = resultData.getData();
                circularImageView.setImageURI(imageUri);
            }
        }
        else if (requestCode == ACTION_CAMERA_PHOTO && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) resultData.getExtras().get("data");
            idCard = (Bitmap) resultData.getExtras().get("data");
            recievedImage.setImageBitmap(photo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_CAMERA_PHOTO)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, ACTION_CAMERA_PHOTO);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void checkAllData(){
        id_name = name.getText().toString();
        id_num = idnumber.getText().toString();

        if(id_num.isEmpty()){
            Toast.makeText(NamesnIDSActivity.this,"Enter your ID number",Toast.LENGTH_SHORT).show();
        }
        else if(id_name.isEmpty()){
            Toast.makeText(NamesnIDSActivity.this,"Enter the Name on your ID",Toast.LENGTH_SHORT).show();
        }
        else if (profileImg == null){
            Toast.makeText(NamesnIDSActivity.this,"Your Need a profile photo",Toast.LENGTH_SHORT).show();
        }
        else if (idCard == null){
            Toast.makeText(NamesnIDSActivity.this,"Take a picture of your ID",Toast.LENGTH_SHORT).show();
        }
        else{
            startActivity(new Intent(NamesnIDSActivity.this,PDFTutorialActivity.class));
        }
    }


}
