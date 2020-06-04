package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

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

        //new Functions().getErrandsDone(db);

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
                message = ""+dataSnapshot.child("-M8t2NspO06LGgog7M42").child("Message").getValue();
                for(DataSnapshot d: dataSnapshot.child("-M8xr1ZLU4BapuMGkT6X").child("Media").getChildren()){
                    uris.add(""+d.getValue());
                }

                RecievedAdapter ra = new RecievedAdapter(RecieveActivity.this,new ErrandResItem(message,uris));
                rv.setAdapter(ra);
                rv.setLayoutManager(new LinearLayoutManager(RecieveActivity.this));

                //Log.d("wersews","we dey hereeeeee "+resItems.size()+" "+ uris.get(0));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Log.d("qazxdesxrdsdfghjhgffgh","here dey run "+resItems.size());
    }

}
