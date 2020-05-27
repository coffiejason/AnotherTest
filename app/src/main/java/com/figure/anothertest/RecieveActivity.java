package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class RecieveActivity extends AppCompatActivity {

    RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve);

        rv = findViewById(R.id.rv_data_collected);
        RecievedAdapter ra = new RecievedAdapter(this);
        rv.setAdapter(ra);
        rv.setLayoutManager(new LinearLayoutManager(this));


    }
}
