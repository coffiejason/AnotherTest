package com.figure.anothertest;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentActivity extends AppCompatActivity {

    TextView mainPost;
    EditText cEditText;

    String userID;
    DatabaseReference userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_comment);

        mainPost = findViewById(R.id.tv_tp_post_comments);

        String mainPostString = getIntent().getStringExtra("msg");

        if(mainPostString != null){
            mainPost.setText(mainPostString);
        }

        userDB = FirebaseDatabase.getInstance().getReference().child("Customers available");



    }
}
