package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecieveActivity extends AppCompatActivity {

    RecyclerView rv;
    StorageReference[] refs;

    //find a way to pass uri strings instead of Storage references


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve);

        refs = new StorageReference[3];

        refs[0] = FirebaseStorage.getInstance().getReference("mtJX0dji3mMVCTVb1EFzFxAIPAn21589067783631").child("1589067813170 jpg");
        refs[1] = FirebaseStorage.getInstance().getReference("mtJX0dji3mMVCTVb1EFzFxAIPAn21589067783631").child("1589067813170 jpg");
        refs[2] = FirebaseStorage.getInstance().getReference("mtJX0dji3mMVCTVb1EFzFxAIPAn21589067783631").child("1589067813170 jpg");



        rv = findViewById(R.id.rv_data_collected);
        RecievedAdapter ra = new RecievedAdapter(this,refs);
        rv.setAdapter(ra);
        rv.setLayoutManager(new LinearLayoutManager(this));


    }
}
