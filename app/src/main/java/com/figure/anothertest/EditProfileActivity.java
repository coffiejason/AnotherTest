package com.figure.anothertest;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;

public class EditProfileActivity extends AppCompatActivity {

    Toolbar closebtn;
    LinearLayout addPhone,ln_edit_name,addID;
    TextView tv_username,tv_phone,tv_id_card;

    Button btn_log_out;

    FloatingActionButton fab;

    CircularImageView image_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findViewById(R.id.tv_username);

        closebtn = findViewById(R.id.closebtn);

        addID = findViewById(R.id.addID);

        btn_log_out = findViewById(R.id.btn_log_out);

        image_profile = findViewById(R.id.image_profile);
        fab = findViewById(R.id.fab_camera);
        tv_username = findViewById(R.id.tv_username);
        tv_phone = findViewById(R.id.tv_phone);
        tv_id_card = findViewById(R.id.tv_id_card);

        setProfileDetails();

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addPhone = findViewById(R.id.add_phone);
        ln_edit_name = findViewById(R.id.ln_edit_name);

        addPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this,AddPhoneActivity.class));
            }
        });

        addID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this,TPCertActivity.class));
            }
        });

        ln_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this,AddNameActivity.class));
            }
        });

        btn_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("profilesharedpref",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor2 = preferences.edit();
                editor2.clear();
                editor2.apply();

                SharedPreferences pref = getSharedPreferences("fcmsharedpref",MODE_PRIVATE);
                SharedPreferences.Editor editor3 = pref.edit();
                editor3.clear();
                editor3.apply();

                Intent i = new Intent(EditProfileActivity.this,HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ImagePicker.create(EditProfileActivity.this).start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfileDetails();
    }

    void setProfileDetails(){
        String username = "",phone = "",id = "";

        username = username + SharedPrefs.getUsername();
        phone = phone + SharedPrefs.getUsername();
        //id = id + SharedPrefs.getUsername();

        if(username.isEmpty()){
            tv_username.setText("Add username");
        }
        else{
            tv_username.setText(""+SharedPrefs.getUsername());
        }

        if(phone.isEmpty()){
            tv_phone.setText("Add Phone number");
        }
        else{
            tv_phone.setText("+"+SharedPrefs.getPhonenum());
        }
        /*
        if(id.isEmpty()){
            tv_id_card.setText("Add a Valid ID");
        }
        else{
            tv_id_card.setText(""+SharedPrefs.getIDnumber());
        }*/
    }
}
