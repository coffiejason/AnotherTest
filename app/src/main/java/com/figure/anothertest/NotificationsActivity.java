package com.figure.anothertest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView rv;
    Toolbar closebtn;

    DatabaseReference userDB;
    String userID;

    private static List<String> errandsNearBy = new ArrayList<>();
    private static List<ErrandItem> errandsNearBy2 = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        rv = findViewById(R.id.notifyrv);
        closebtn = findViewById(R.id.notifications_tb);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userDB = FirebaseDatabase.getInstance().getReference().child("Customers available").child(userID);
        userDB.keepSynced(true);


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
        checkforErrands(userDB,getApplicationContext());
    }

    public void checkforErrands(DatabaseReference db, final Context c){

        int i = 0;

        errandsNearBy2.clear();

        db.child("ErrandsNearBy").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //get tiperID here and write to ErrandsCompleted node when errand is complete

                Log.d("newErrandAvailable","You new errandss ohhhh");
                Toast.makeText(c,"You Have a new Task",Toast.LENGTH_SHORT).show();

                //Log.d("howmnytimes"," "+i);
               // i++;
                Log.d("howmnytimes", ""+dataSnapshot.child("Message").getValue());

                errandsNearBy.add(""+dataSnapshot.child("Message").getValue());
                errandsNearBy2.add(new ErrandItem(""+dataSnapshot.child("tiperID").getValue(),""+dataSnapshot.child("Message").getValue()));

                NotificationsAdapter adapter = new NotificationsAdapter(NotificationsActivity.this,errandsNearBy2);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }







}
