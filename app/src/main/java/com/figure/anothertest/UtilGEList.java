package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UtilGEList extends AppCompatActivity {

    RecyclerView rv;
    List<UtilitiesERitem> list = new ArrayList<>();
    Toolbar tb;

    DatabaseReference userDB;
    String userID;
    String tasknum;

    private static List<UtilitiesERitem> utilityerrands = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util_g_e_list);
        tb = findViewById(R.id.toolbar);
        rv = findViewById(R.id.recyclerViewuer);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userDB = FirebaseDatabase.getInstance().getReference().child("MeterRequests");
        userDB.keepSynced(true);

        tasknum = getIntent().getStringExtra("tasknum");
        Toast.makeText(this, ""+tasknum, Toast.LENGTH_SHORT).show();

        checkUtilityErrands(userDB,getApplicationContext());

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showList();
    }

    public void checkUtilityErrands(DatabaseReference db, final Context c){
        utilityerrands.clear();
        db.child(tasknum+"/Customer List").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //get tiperID here and write to ErrandsCompleted node when errand is complete

                //Log.d("howmnytimes"," "+i);
                //i++;
                //Log.d("howmnytimes"," "+i+": "+dataSnapshot.child("Areacode").getValue());

                utilityerrands.add(new UtilitiesERitem(""+dataSnapshot.child("Name").getValue(),""+dataSnapshot.child("Meterno").getValue(),new LatLng(Double.parseDouble(""+dataSnapshot.child("l").getValue()),Double.parseDouble(""+dataSnapshot.child("g").getValue())),""+dataSnapshot.child("Areacode").getValue()));
                //UtilitiesERAdapter adapter = new UtilitiesERAdapter(UtititiesERActivity.this,utilityerrands);
                //rv.setAdapter(adapter);
                //rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                showList();
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

    void showList(){
        if(utilityerrands.size() >0 ){
            UtilitiesERAdapter adapter = new UtilitiesERAdapter(UtilGEList.this,utilityerrands);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

    }
}
