package com.figure.anothertest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.figure.anothertest.bottomvavigation.BadgeBottomNavigtion;
import com.figure.anothertest.bottomvavigation.BottomAdapter;
import com.figure.anothertest.bottomvavigation.BottomItem;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorldActivity extends AppCompatActivity implements BottomAdapter.BottomItemClickInterface{

    //bottom nav stuff, might delete
    private final int HOME = 0;
    private final int NOTIFICATIONS = 1;
    private final int FRIENDS = 2;
    private final int SETTINGS = 3;
    private final int CART = 4;
    private int selectedId = 0;
    RecyclerView rv;

    private BadgeBottomNavigtion badgeBottomNavigtion;

    RelativeLayout mapBtn,notifIcon;
    CardView newNotif;

    List<UtilGenItem> errands;
    DatabaseReference ref;

    private static List<UtilitiesERitem> utilityerrands = new ArrayList<>();

    private HashMap<String, Object> j = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);

        //list = new Functions().getWorldPosts();

        badgeBottomNavigtion = new BadgeBottomNavigtion(findViewById(R.id.BottomNavigation), WorldActivity.this, WorldActivity.this);
        initBottomItems();

        errands = new ArrayList<>();

        init();

        ref = FirebaseDatabase.getInstance().getReference().child("MeterRequests");

        getErrands(ref,this);

        checkUtilityErrands(ref,getApplicationContext());





    }

    void init(){
        mapBtn = findViewById(R.id.mapbtn);
        notifIcon  = findViewById(R.id.toolbar_body_parent);
        newNotif = findViewById(R.id.toolbar_badge_parent);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Log.d("allpostsinlist",""+ErrandMapActivity.allposts.size());
       rv = findViewById(R.id.world_recyclerview);
        showList();


        notifIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewNotification(false);
                startActivity(new Intent(WorldActivity.this,NotificationsActivity.class));
            }
        });


    }

    void showList(){
        if(errands.size() > 0){
            WorldAdapter adapter = new WorldAdapter(WorldActivity.this,errands);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
    }

    @SuppressLint("ResourceType")
    private void initBottomItems() {
        BottomItem home = new BottomItem(HOME, R.drawable.ic_home, "Home", false);
        BottomItem notifications = new BottomItem(NOTIFICATIONS, R.drawable.ic_plus, "Post", false);
        //BottomItem friends = new BottomItem(FRIENDS, R.drawable.ic_people, "Friends", false);
        //BottomItem cart = new BottomItem(CART, R.drawable.ic_cart, "Cart", true);
        BottomItem settings = new BottomItem(SETTINGS, R.drawable.ic_settings, "Settings", false);

        badgeBottomNavigtion.addBottomItem(home);
        badgeBottomNavigtion.addBottomItem(notifications);
        //badgeBottomNavigtion.addBottomItem(friends);
        //badgeBottomNavigtion.addBottomItem(cart);
        badgeBottomNavigtion.addBottomItem(settings);

        badgeBottomNavigtion.apply(selectedId, getString(R.color.colorAccent), getString(R.color.tipeeGreenDark));
        itemSelect(selectedId);
    }

    @Override
    public void itemSelect(int itemId) {
        Toast.makeText(this," "+itemId,Toast.LENGTH_SHORT).show();
        switch (itemId){
            case 0:
                break;

            case 1:
                Intent i1 = new Intent(WorldActivity.this,Tper1Activity.class);
                startActivity(i1);
                break;

            case 3:
                Intent i = new Intent(WorldActivity.this,SettingsActivity.class);
                startActivity(i);
                break;
        }
    }

    void setNewNotification(final Boolean choice){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(choice){
                    newNotif.setVisibility(View.VISIBLE);
                }else{
                    newNotif.setVisibility(View.GONE);
                }

            }
        });



    }

    public void getErrands(DatabaseReference db, final Context c){
        Log.d("dowegeterrand22s"," where here");
        utilityerrands.clear();
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //get tiperID here and write to ErrandsCompleted node when errand is complete

                 Log.d("dowegeterrands"," where here ");

                 String eid = "";

                //i++;
                //Log.d("howmnytimes"," "+i+": "+dataSnapshot.child("Areacode").getValue());

                //utilityerrands.add(new UtilitiesERitem(""+errands,""+dataSnapshot.child("Meterno").getValue(),new LatLng(Double.parseDouble(""+dataSnapshot.child("l").getValue()),Double.parseDouble(""+dataSnapshot.child("g").getValue())),""+dataSnapshot.child("Areacode").getValue()));
                //dataSnapshot.getChildren()
                //errands.add(new TPPost(""+dataSnapshot.child("Message").getValue()));

                HashMap<String, Object> h = new HashMap<>();


                Log.d("jjwjwjdtassnapcsj"," "+dataSnapshot.getKey());
                eid = ""+dataSnapshot.getKey();

                for(DataSnapshot p: dataSnapshot.getChildren()){

                    Log.d("dowegeterrands2"," "+p.getKey());

                    h.put(p.getKey(),p.getValue());
                    Log.d("dowegeterrands3"," "+p);
                    for(DataSnapshot p2: p.getChildren()){

                        Log.d("tryagain",""+p2);
                       // h.put(p.getKey(),p.getValue());

                        for(DataSnapshot p3: p2.getChildren()){
                            Log.d("tryagain2",""+p3);
                            Log.d("tryagain3",p3.getKey()+" - "+p3.getValue());
                            j.put(eid+""+p3.getKey(),p3.getValue());



                        }
                        Log.d("hhsswty1y12",""+j.get(eid+"Name"));
                        utilityerrands.add(new UtilitiesERitem(""+j.get(eid+"Name"),""+j.get(eid+"Meterno"),new LatLng(Double.parseDouble(""+j.get(eid+"l")),Double.parseDouble(""+j.get(eid+"g"))),""+j.get(eid+"Areacode")));
                    }

                }

                /*
                try {
                    JSONObject cl = new JSONObject(""+h.get("Customer List").toString());
                    Log.d("wedeygetoooh",""+cl.getString("Meterno"));
                } catch (JSONException e) {
                   Log.d("errorreading",""+e.toString());
                }*/



                Log.d("learningagain",""+h.get("Customer List"));
                errands.add(new UtilGenItem(""+h.get("TiperID"),""+h.get("Message"),eid));
                showList();
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void checkUtilityErrands(DatabaseReference db, final Context c){
        utilityerrands.clear();
        db.child("task00/Customer List").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //get tiperID here and write to ErrandsCompleted node when errand is complete

                //Log.d("howmnytimes"," "+i);
                //i++;
                //Log.d("howmnytimes"," "+i+": "+dataSnapshot.child("Areacode").getValue());

                Log.d("hjahjdhjdhj",""+dataSnapshot.child("Name").getValue());

                utilityerrands.add(new UtilitiesERitem(""+dataSnapshot.child("Name").getValue(),""+dataSnapshot.child("Meterno").getValue(),new LatLng(Double.parseDouble(""+dataSnapshot.child("l").getValue()),Double.parseDouble(""+dataSnapshot.child("g").getValue())),""+dataSnapshot.child("Areacode").getValue()));
                //UtilitiesERAdapter adapter = new UtilitiesERAdapter(UtititiesERActivity.this,utilityerrands);
                //rv.setAdapter(adapter);
                //rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
