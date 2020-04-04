package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;


public class OpenPostsCluster extends AppCompatActivity {

    String[] postMsgs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_open_posts_cluster);

        RecyclerView openCLusterList = findViewById(R.id.openCLusterRecycler);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        int i = bundle.getInt("iterator");

        postMsgs = new String[i+1];

        Log.d("Bundledeywork",""+i);

        for(int j = i; j >= 0; j--){
            postMsgs[j] = bundle.getString("Key"+j);
            Log.d("Fandthaeumtymessageost",""+postMsgs[j]);
        }


        ClusterListAdapter adapter = new ClusterListAdapter(this,postMsgs);
        openCLusterList.setAdapter(adapter);
        openCLusterList.setLayoutManager(new LinearLayoutManager(this));

    }
}
