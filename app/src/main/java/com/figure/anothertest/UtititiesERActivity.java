package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UtititiesERActivity extends AppCompatActivity {

    RecyclerView rv;
    List<UtilitiesERitem> list = new ArrayList<>();
    Toolbar tb;

    DatabaseReference userDB;
    String userID;

    private static List<UtilitiesERitem> utilityerrands = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utitities_e_r);
        tb = findViewById(R.id.toolbar);
        rv = findViewById(R.id.recyclerViewuer);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userDB = FirebaseDatabase.getInstance().getReference().child("Customers available").child(userID);
        userDB.keepSynced(true);

        checkUtilityErrands(userDB,getApplicationContext());

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list.add(new UtilitiesERitem("Jason Coffie","10623109",new LatLng(0.000,0.000),"PNTG"));
        list.add(new UtilitiesERitem("Opoku Nana Kwame","10623109",new LatLng(0.000,0.000),"PRKU"));
        list.add(new UtilitiesERitem("Michael Kofi","10623109",new LatLng(0.000,0.000),"BOI"));

    }

    public void checkUtilityErrands(DatabaseReference db, final Context c){
        utilityerrands.clear();
        db.child("ErrandsNearBy/-MDyZpAmKlmeUVR8igZC/Customer List").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //get tiperID here and write to ErrandsCompleted node when errand is complete

                //Log.d("howmnytimes"," "+i);
                //i++;
                //Log.d("howmnytimes"," "+i+": "+dataSnapshot.child("Areacode").getValue());

                utilityerrands.add(new UtilitiesERitem(""+dataSnapshot.child("Name").getValue(),""+dataSnapshot.child("Meterno").getValue(),new LatLng(Double.parseDouble(""+dataSnapshot.child("l").getValue()),Double.parseDouble(""+dataSnapshot.child("g").getValue())),""+dataSnapshot.child("Areacode").getValue()));
                UtilitiesERAdapter adapter = new UtilitiesERAdapter(UtititiesERActivity.this,utilityerrands);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
