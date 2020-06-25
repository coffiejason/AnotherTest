package com.figure.anothertest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MyPosts extends AppCompatActivity {

    List<TPPost> list;
    TPPost post;
    String[] postMsgs;
    String[] userIDs;
    String[] postIDs;

    RelativeLayout tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        tb = findViewById(R.id.closebtn);

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*

        list = new Functions().getMyPostsList();

        RecyclerView rv = findViewById(R.id.myposts_recyclerview);

        postMsgs = new String[list.size()+1];
        userIDs = new String[list.size()+1];
        postIDs = new String[list.size()+1];



        for(int i = 0; i<= list.size()-1; i++){

            post = list.get(i);

            postMsgs[i] = post.getpMessage();

            userIDs[i] = post.getpUserID();

            postIDs[i] = post.getpPostID();


            Log.d("sdgsdfsdfs "+i,post.getpMessage()+"");

        }

        ClusterListAdapter adapter = new ClusterListAdapter(this,postMsgs,userIDs,postIDs);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

         */
    }
}
