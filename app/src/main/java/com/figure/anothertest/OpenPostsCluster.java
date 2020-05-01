package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;


public class OpenPostsCluster extends AppCompatActivity {

    String[] postMsgs;
    String[] postUserids;
    String[] postids;

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
        postUserids = new String[i+1];
        postids = new String[i+1];

        Log.d("Bundledeywork",""+i);

        //user you if userid == firebase.instance,getuid

        for(int j = i; j >= 0; j--){
            postMsgs[j] = bundle.getString("Key"+j);
            postUserids[j] = bundle.getString("IDKey"+j);
            postids[j] = bundle.getString("postIDKey"+j);
            Log.d("Fandthaeumtymessageost",""+postids[j]);
        }



        ClusterListAdapter adapter = new ClusterListAdapter(this,postMsgs,postUserids,postids);
        openCLusterList.setAdapter(adapter);
        openCLusterList.setLayoutManager(new LinearLayoutManager(this));

    }
}
