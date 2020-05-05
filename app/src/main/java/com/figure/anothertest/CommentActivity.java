package com.figure.anothertest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    TextView mainPost;
    EditText cEditText;
    Button postBtn;

    RecyclerView rv;
    List<TPPost> list;

    String msg;

    DatabaseReference userDB;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_comment);

        final SwipeRefreshLayout swipe = findViewById(R.id.comment_layout);

        mainPost = findViewById(R.id.tv_tp_post_comments);
        cEditText = findViewById(R.id.comment_et);
        postBtn = findViewById(R.id.comment_post_btn);
        rv = findViewById(R.id.comment_recyclerview);

        String mainPostString = getIntent().getStringExtra("msg");
        String userid = getIntent().getStringExtra("id");
        String postid = getIntent().getStringExtra("postid");

        list = new ArrayList<>();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDB = FirebaseDatabase.getInstance().getReference().child("Customers available").child(userid).child("Posts").child(postid);


        if(mainPostString != null){
            mainPost.setText(mainPostString);
        }



        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> h = new HashMap<>();
                String userid,postid;
                String msg;

                list.clear();
                for(DataSnapshot p: dataSnapshot.child("Comments").getChildren()){

                    h.put("PostID",p.getKey());

                    for(DataSnapshot finaSnapShot: p.getChildren()){

                        Log.d("ypyppypypAllMsgsFinallyyy",finaSnapShot+"");
                        h.put(""+finaSnapShot.getKey(),""+finaSnapShot.getValue());
                    }
                    msg = ""+h.get("Message");
                    userid = ""+h.get("UserID");
                    postid = ""+h.get("PostID");

                    Log.d("commmenttt",""+msg);


                    list.add(new TPPost(msg,userid,postid));

                }

                Log.d("listsize",""+list.size());
                refreshCode(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = cEditText.getText().toString();

                if(!msg.isEmpty()){
                    new Functions().comment(userDB,userID,msg);
                }
                cEditText.getText().clear();
                hideKeyboardFrom(getApplicationContext(),v);
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //refreshCode();
                swipe.setRefreshing(false);
            }
        });
    }

    private void refreshCode(List<TPPost> newlist){
        String[] postMsgs,postUserids,postids;

        if(newlist.size() != 0){
            Log.d("Wekechhereee","Wekechhereee");

            postMsgs = new String[newlist.size()+1];
            postUserids = new String[newlist.size()+1];
            postids = new String[newlist.size()+1];

            TPPost post;

            int j = 0;

            for(int i = 0; i<= newlist.size()-1; i++){

                post = newlist.get(i);

                postMsgs[i] = post.getpMessage();
                postUserids[i] = post.getpUserID();
                postids[i] = post.getpPostID();

                Log.d("whatthamessage",j+" :"+postMsgs[i]);
                j++;
            }

            ClusterListAdapter adapter = new ClusterListAdapter(CommentActivity.this,postMsgs,postUserids,postids);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
            newlist.clear();
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
