package com.figure.anothertest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditProfileActivity extends AppCompatActivity {

    Toolbar closebtn;
    LinearLayout addPhone,ln_edit_name,addID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        closebtn = findViewById(R.id.closebtn);

        addID = findViewById(R.id.addID);

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
}
