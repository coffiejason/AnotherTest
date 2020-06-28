package com.figure.anothertest;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView rv;
    Toolbar closebtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        rv = findViewById(R.id.notifyrv);
        closebtn = findViewById(R.id.notifications_tb);

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);



        refreshData();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    void refreshData(){
        if(new Functions().getErrandsNearBy2().size() > 0){
            NotificationsAdapter adapter = new NotificationsAdapter(NotificationsActivity.this,new Functions().getErrandsNearBy2());
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));
        }
    }







}
