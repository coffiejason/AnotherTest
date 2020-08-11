package com.figure.anothertest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditProfileActivity extends AppCompatActivity {

    Toolbar closebtn;
    LinearLayout addPhone,ln_edit_name,addID;
    TextView tv_username,tv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findViewById(R.id.tv_username);

        closebtn = findViewById(R.id.closebtn);

        addID = findViewById(R.id.addID);

        tv_username = findViewById(R.id.tv_username);
        tv_phone = findViewById(R.id.tv_phone);

        tv_username.setText(""+SharedPrefs.getUsername());
        tv_phone.setText(""+SharedPrefs.getPhonenum());

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
                startActivity(new Intent(EditProfileActivity.this,AddIDCardActivity.class));
            }
        });

        ln_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this,AddNameActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_username.setText(""+SharedPrefs.getUsername());
        tv_phone.setText(""+SharedPrefs.getPhonenum());
    }
}
