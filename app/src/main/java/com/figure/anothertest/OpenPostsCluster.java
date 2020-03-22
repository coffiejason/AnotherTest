package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Collection;

public class OpenPostsCluster extends AppCompatActivity {
    private RecyclerView openCLusterList;

    String postMsgs[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_posts_cluster);

        openCLusterList = findViewById(R.id.openCLusterRecycler);

        Bundle bundle = getIntent().getExtras();
        int i = bundle.getInt("iterator");

        Log.d("Bundledeywork",""+i);

        for(int j = 0; j<=i;j++){
            postMsgs[j] = bundle.getString("Key"+j);
        }


        ClusterListAdapter adapter = new ClusterListAdapter(this,postMsgs);
        openCLusterList.setAdapter(adapter);
        openCLusterList.setLayoutManager(new LinearLayoutManager(this));

    }
}
