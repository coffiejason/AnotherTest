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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);

        list = new Functions().getWorldPosts();

        RecyclerView rv = findViewById(R.id.world_recyclerview);

        postMsgs = new String[list.size()+1];



        for(int i = 0; i<= list.size()-1; i++){

            post = list.get(i);

            postMsgs[i] = post.getpMessage();

            Log.d("ndnjsbshs "+i,post.getpMessage()+"");

        }

        ClusterListAdapter adapter = new ClusterListAdapter(this,postMsgs);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }
}
