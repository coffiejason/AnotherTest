package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class NamesnIDSActivity extends AppCompatActivity {
    Toolbar closebtn;
    RelativeLayout next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namesn_i_d_s);

        next = findViewById(R.id.login);
        closebtn = findViewById(R.id.closebtn);


        closebtn.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> startActivity(new Intent(NamesnIDSActivity.this,PDFTutorialActivity.class)));
    }
}
