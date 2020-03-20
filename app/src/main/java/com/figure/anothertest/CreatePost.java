package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatePost extends AppCompatActivity {

    private Button Postbtn;
    private Toolbar Closebtn;
    int postRadius=1;
    float l,g;
    LatLng postLocation;
    String userID;
    DatabaseReference userDB;

    EditText postEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_create_post);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDB = FirebaseDatabase.getInstance().getReference().child("Customers available");

        Intent intent = getIntent();

        l = intent.getFloatExtra("default_l",(float)0.00000);

        g = intent.getFloatExtra("default_g",(float)0.00000);

        postLocation = new LatLng(l,g);

        //check if l and g were got
        //Log.d("posloc",l+" "+g);

        init();

        Closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePost.this, ErrandMapActivity.class));
                finish();
            }
        });

        Postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Functions().creatPostText(userDB,userID,postEditText.getText().toString(),l,g,postRadius);
                finish();
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
    }
}
