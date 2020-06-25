package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class UtititiesERActivity extends AppCompatActivity {

    RecyclerView rv;
    List<UtilitiesERitem> list = new ArrayList<>();
    List<UtilitiesERitem> list2;
    Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utitities_e_r);
        tb = findViewById(R.id.toolbar);

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list.add(new UtilitiesERitem("Jason Coffie","10623109",new LatLng(0.000,0.000),"PNTG"));
        list.add(new UtilitiesERitem("Opoku Nana Kwame","10623109",new LatLng(0.000,0.000),"PRKU"));
        list.add(new UtilitiesERitem("Michael Kofi","10623109",new LatLng(0.000,0.000),"BOI"));

        list2 = new Functions().getUtilityerrands();

        if(list2.size() > 0){
            rv = findViewById(R.id.recyclerViewuer);
            UtilitiesERAdapter adapter = new UtilitiesERAdapter(UtititiesERActivity.this,list2);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(this));
        }

    }
}
