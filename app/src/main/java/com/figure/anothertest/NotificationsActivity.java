package com.figure.anothertest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        rv = findViewById(R.id.notifyrv);

        NotificationsAdapter adapter = new NotificationsAdapter(NotificationsActivity.this,new Functions().getErrandsNearBy());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));


    }
}
