package com.figure.anothertest;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView rv,newrv;
    RelativeLayout closebtn;

    List<ErrandItem> errandItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        rv = findViewById(R.id.notifyrv);
        newrv = findViewById(R.id.new_notifyrv);
        closebtn = findViewById(R.id.closebtn);

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        errandItems.add(new Functions().getErrandsNearBy2().get(0));

        NotificationsAdapter adapter = new NotificationsAdapter(NotificationsActivity.this,new Functions().getErrandsNearBy2());
        NotificationsAdapter adapter2 = new NotificationsAdapter(NotificationsActivity.this,errandItems);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));

        newrv.setAdapter(adapter2);
        newrv.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));


    }
}
