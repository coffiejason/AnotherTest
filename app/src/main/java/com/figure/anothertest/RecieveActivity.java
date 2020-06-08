package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RecieveActivity extends AppCompatActivity {

    RecyclerView rv;
   List<String> uris;
   List<ErrandResItem> resItem;
   TextView msg;

    //find a way to pass uri strings instead of Storage references
    DatabaseReference db;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        db = FirebaseDatabase.getInstance().getReference().child("Customers available").child(userID).child("Completed");

        msg = findViewById(R.id.recieved_tp_post);

        uris = new Functions().getMediauris();

        resItem = new Functions().getResItems();

        Log.d("sasazzzx",""+new Functions().getResItems().size());

        rv = findViewById(R.id.rv_data_collected);
        getErrandsDone(db);

    }

    void getErrandsDone(DatabaseReference errandsNode){
        //errands node is the node where new errands are written to if a user matches a location errand criteria


        errandsNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String message;
                List<String> uris = new ArrayList<>();

                //get the key below programmactically, when errand is complete, tipee rights to tiper newerrandcompleted node
                //tiper reads that node and notifies user on that errand is complete, tiper also gets the key from that node and deletes node
                message = ""+dataSnapshot.child("-M8xr1ZLU4BapuMGkT6X").child("Message").getValue();
                for(DataSnapshot d: dataSnapshot.child("-M8xr1ZLU4BapuMGkT6X").child("Media").getChildren()){
                    uris.add(""+d.getValue());
                }

                RecievedAdapter ra = new RecievedAdapter(RecieveActivity.this,new ErrandResItem(message,uris));
                rv.setAdapter(ra);
                rv.setLayoutManager(new LinearLayoutManager(RecieveActivity.this));
                if(message != null){
                    msg.setText(message);
                }



                //Log.d("wersews","we dey hereeeeee "+resItems.size()+" "+ uris.get(0));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Log.d("qazxdesxrdsdfghjhgffgh","here dey run "+resItems.size());
    }

}
