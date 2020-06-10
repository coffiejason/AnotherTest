package com.figure.anothertest;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView rv;
    Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        rv = findViewById(R.id.notifyrv);
        tb = findViewById(R.id.toolbarnotify);

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        NotificationsAdapter adapter = new NotificationsAdapter(NotificationsActivity.this,new Functions().getErrandsNearBy2());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));


    }
}
