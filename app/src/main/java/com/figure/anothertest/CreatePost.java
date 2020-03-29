package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.snackbar.Snackbar;
public class CreatePost extends AppCompatActivity {

    private RelativeLayout Postbtn;
    private Toolbar Closebtn;
    float l,g;

    EditText postEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_create_post);

        Intent intent = getIntent();

        l = intent.getFloatExtra("default_l",(float)0.00000);

        g = intent.getFloatExtra("default_g",(float)0.00000);

        init();

        Closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        Postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(postEditText.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,"I can post your idea if you dont say anything",3000);
                    snackbar.show();
                }
                else{
                    Intent i = new Intent(CreatePost.this,ChooseLocationActivity.class);
                    i.putExtra("default_l",l);
                    i.putExtra("default_g",g);
                    i.putExtra("message",postEditText.getText().toString());
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    public void init(){
        Postbtn = findViewById(R.id.btn_post);
        Closebtn = findViewById(R.id.toolbar);
        postEditText = findViewById(R.id.tp_post_text);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(CreatePost.this);
    }
}
