package com.figure.anothertest;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorldActivity extends AppCompatActivity {

    List<TPPost> list;
    TPPost post;
    String[] postMsgs;
    String[] userIDs;
    String[] postIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);

        list = new Functions().getWorldPosts();

        RecyclerView rv = findViewById(R.id.world_recyclerview);

        postMsgs = new String[list.size()+1];
        userIDs = new String[list.size()+1];
        postIDs = new String[list.size()+1];



        for(int i = 0; i<= list.size()-1; i++){

            post = list.get(i);

            postMsgs[i] = post.getpMessage();
            userIDs[i] = post.getpUserID();
            postIDs[i] = post.getpPostID();

            Log.d("ndnjsbshs "+i,post.getpMessage()+"");

        }

        ClusterListAdapter adapter = new ClusterListAdapter(this,postMsgs,userIDs,postIDs);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }
}
