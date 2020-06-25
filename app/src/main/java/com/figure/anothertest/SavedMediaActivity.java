package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavedMediaActivity extends AppCompatActivity {
    DatabaseReference db;
    String userID;
    List<String> keys = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    RecyclerView rv;
    RelativeLayout tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_media);

        rv = findViewById(R.id.saved_data_rv);
        tb = findViewById(R.id.closebtn);

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        db = FirebaseDatabase.getInstance().getReference().child("Customers available").child(userID).child("Completed");
        getKeys();

        if(keys.size() > 0){


        }

    }

    void getKeys(){
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    keys.add(""+d.getKey());
                    titles.add(""+d.child("Message").getValue());
                    Log.d("allerrandcompletedkeys",""+d.getKey());

                    SavedMediaAdapter adapter = new SavedMediaAdapter(SavedMediaActivity.this,keys,titles);
                    rv.setAdapter(adapter);
                    rv.setLayoutManager(new LinearLayoutManager(SavedMediaActivity.this));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
