package com.figure.anothertest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.figure.anothertest.bottomvavigation.BadgeBottomNavigtion;
import com.figure.anothertest.bottomvavigation.BottomAdapter;
import com.figure.anothertest.bottomvavigation.BottomItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//add layout to to map activity that shows when there's a new tip available
//find a better way to show posts and sort current users

public class ErrandMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,ClusterManager.OnClusterClickListener<PostClusterItem>, BottomAdapter.BottomItemClickInterface, TipBSDialogue.BottomSheetListner {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    Toolbar tb;
    CardView newNotif;
    RelativeLayout notifIcon;

    RelativeLayout mapLayoutSub;

    private BadgeBottomNavigtion badgeBottomNavigtion;

    //private int radius = 1; //should be custom adjusted by user via UI, determines the range of other Users avaialable

    String userID;

    DatabaseReference userAvailabilityRef;
    DatabaseReference userIndividual;
    TPClusterRenderer renderer;

    LatLng defaultLocation;
    Bundle args;

    AutocompleteSupportFragment autocompleteFragment;

    //bottom nav stuff, might delete
    private final int HOME = 0;
    private final int NOTIFICATIONS = 1;
    private final int FRIENDS = 2;
    private final int SETTINGS = 3;
    private final int CART = 4;
    private int selectedId = 0;


    private ClusterManager<PostClusterItem> mClusterManager;
    private static Collection<PostClusterItem> postsfrmDB;

    public static List<User> availableUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_errand_map);

        new TPMessagingService();

        badgeBottomNavigtion = new BadgeBottomNavigtion(findViewById(R.id.BottomNavigation), ErrandMapActivity.this, ErrandMapActivity.this);
        initBottomItems();

        init();

        getToken();

        getAllPosts();

        onpenTipBottoSheet();
        //new Functions().getAllPosts(userAvailabilityRef,postsfrmDB,mClusterManager);
        //new Functions().getMyPosts(mMap,userAvailabilityRef.child(userID));

        Log.d("qwertrewqwer",""+postsfrmDB.size());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Add styled Map
        /*
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.));

            if (!success) {
                Log.e("Maps Activity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Maps Activity", "Can't find style. Error: ", e);
        }*/



        buildGoogleApiClient();


        //request for User permission
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraIdleListener(mClusterManager);
        //mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager = new ClusterManager<>(ErrandMapActivity.this, mMap);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.cluster();

        renderer = new TPClusterRenderer(ErrandMapActivity.this,mMap,mClusterManager);
        mClusterManager.setRenderer(renderer);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        //locationRequest.setInterval(1000);  //update location every secend
        //locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); //change for better accuracy
        //noinspection deprecation
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        defaultLocation = new LatLng(location.getLatitude(),location.getLongitude()); //users current location,Idea i got was to reduce float number for a better result
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        args.putFloat("l", (float) location.getLatitude());
        args.putFloat("g", (float) location.getLongitude());

        //code to save user location to Firebase
        //getPost(userIndividual);

        readCameraChanges();



        new Functions().saveUser(userIndividual,location,userID);
        mClusterManager.cluster();

        //mMap.clear();
        //mClusterManager.clearItems();


        //TPPost p = new TPPost("heyy",5.209,0.2994);

        //Functions.whoGetsNotified(userAvailabilityRef,p,1000,"sometopic",true);
    }

    protected synchronized void buildGoogleApiClient(){
        //noinspection deprecation
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onClusterClick(Cluster<PostClusterItem> cluster) {
        Toast.makeText(this, "Cluster clicked", Toast.LENGTH_SHORT).show();
        Collection<PostClusterItem> pCluster = cluster.getItems();

        Bundle bundle = new Bundle();

        int i = 0;

        for(PostClusterItem p: pCluster){
            Log.d("GotAllMsg",""+p.getTitle());
            bundle.putString("Key"+i,p.getTitle());
            bundle.putString("IDKey"+i,p.getUserID());
            bundle.putString("postIDKey"+i,p.getPostid());
            i++;
        }

        bundle.putInt("iterator",i);

        Intent intent = new Intent(ErrandMapActivity.this, OpenPostsCluster.class);
        intent.putExtras(bundle);
        startActivity(intent);

        return true;
    }

    private static final User[] Users =  new User[] {
            new User(0.00,0.00,"kjwd"),
    };


    private static final String[] COUNTRIES = new String[] {
            "Belgium","Beru","Brixton", "France", "Italy", "Germany", "Spain"
    };

    void init(){

        postsfrmDB = new ArrayList<>();
        availableUsers = new ArrayList<>();

        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Functions.GTTOWNS);

        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.fasttravel);
        textView.setAdapter(adapter);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("yuiooiuytrew","yessss  "+parent.getItemAtPosition(position));
            }
        });*/



        tb = findViewById(R.id.mapToolbar);

        newNotif = findViewById(R.id.toolbar_badge_parent);
        notifIcon  = findViewById(R.id.toolbar_body_parent);

        notifIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewNotification(false);
                startActivity(new Intent(ErrandMapActivity.this,NotificationsActivity.class));
            }
        });

        mapLayoutSub = findViewById(R.id.notifyer_space);

        //new Functions().sideMenu(ErrandMapActivity.this,tb);

        //get UserID from login and pass it as intent

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        FirebaseMessaging.getInstance().subscribeToTopic("topicsname");

        //this too should be acquired at login
        userAvailabilityRef = FirebaseDatabase.getInstance().getReference().child("Customers available");
        userAvailabilityRef.keepSynced(true);
        userIndividual = userAvailabilityRef.child(userID);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("wouldthisowrkeverytime", SharedPrefs.getInstance(ErrandMapActivity.this).getToken() + "");
            }
        };

        registerReceiver(broadcastReceiver,new IntentFilter(TPMessagingService.TOKEN_BROADCAST));

        RelativeLayout post_button = findViewById(R.id.post_errand);

        args = new Bundle();

        //hide api
        Places.initialize(getApplicationContext(),"AIzaSyAYWdmSCJ9MyVh0bvBFbrQb9ELgmu3LYu8");

        //autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("PlacesGot", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("PlacesError", "An error occurred: " + status);
            }
        });

        //LayoutInflater inflater = this.getLayoutInflater();
        //final View child = inflater.inflate(R.layout.errand_notifyer,null);

        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //mapLayoutSub.addView(child);



                Intent intent = new Intent(ErrandMapActivity.this, CreatePost.class);
                intent.putExtra("default_l",args.getFloat("l"));
                intent.putExtra("default_g",args.getFloat("g"));
                startActivity(intent);
                Animatoo.animateSlideLeft(ErrandMapActivity.this);

            }

        });
    }

    // for new devices sake, save the token in TPMessagingservice, if already generated do this . all must be dont on start
    private void getToken(){
        Log.d("Testttttt","dsfsf");

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(ErrandMapActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.d("FCM Token", token);
                //saveToken(token);

                SharedPrefs.getInstance(ErrandMapActivity.this).storeToken(token);

            }

        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("reasssonnnnnn",""+e);
            }
        });

        Log.d("SharedPrefsToken",SharedPrefs.getInstance(ErrandMapActivity.this).getToken()+"");
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

    @Override
    public void itemSelect(int itemId) {
        Toast.makeText(this," "+itemId,Toast.LENGTH_SHORT).show();
        switch (itemId){
            case 0:
                break;

            case 1:
                Intent i1 = new Intent(ErrandMapActivity.this,CreatePost.class);
                startActivity(i1);
                break;

            case 3:
                Intent i = new Intent(ErrandMapActivity.this,SettingsActivity.class);
                startActivity(i);
                break;
        }

    }

    @SuppressLint("ResourceType")
    private void initBottomItems() {
        BottomItem home = new BottomItem(HOME, R.drawable.ic_home, "Home", false);
        BottomItem notifications = new BottomItem(NOTIFICATIONS, R.drawable.ic_plus, "Notifications", false);
        BottomItem settings = new BottomItem(SETTINGS, R.drawable.ic_settings, "Settings", false);

        badgeBottomNavigtion.addBottomItem(home);
        badgeBottomNavigtion.addBottomItem(notifications);
        badgeBottomNavigtion.addBottomItem(settings);

        badgeBottomNavigtion.apply(selectedId, getString(R.color.colorAccent), getString(R.color.tipeeGreenDark));
        itemSelect(selectedId);
    }

    void getAllPosts(){
        //use Location.distanceBetween() to check if coordinates are in a given radius
        //list.clear();
        userAvailabilityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> hashMap = new HashMap<>();
                String userid;
                double l,g;

                postsfrmDB.clear();
                availableUsers.clear();
                mClusterManager.clearItems();
                for(DataSnapshot d: dataSnapshot.getChildren()){

                    Log.d("Igotthekeyskeyskeys",""+d.getKey());
                    userid = d.getKey();
                    //use location.distancebetween here to get only the keys in users location
                    redundantCode(d);
                    if(d.child("Location").child("g").getValue() !=null){

                        g = (double) d.child("Location").child("g").getValue();
                        l = (double) d.child("Location").child("l").getValue();

                        availableUsers.add(new User(l,g,userid));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setMsgIcon2(ClusterManager<PostClusterItem> cm, Collection<PostClusterItem> posts){

        cm.addItems(posts);
        cm.cluster();
    }

    private void redundantCode(DataSnapshot dataSnapshot){

        HashMap<String, Object> h = new HashMap<>();
        String userid;
        String postid;
        String msg;
        double l,g;
        for(DataSnapshot p: dataSnapshot.child("Posts").getChildren()){

            h.put("PostID",""+p.getKey());

            for(DataSnapshot finaSnapShot: p.getChildren()){

                Log.d("ypyppypypAllMsgsFinallyyy",finaSnapShot+"");

                h.put(""+finaSnapShot.getKey(),""+finaSnapShot.getValue());
            }

            if(h.get("l") != null){
                l = Double.parseDouble(""+h.get("l"));
                g = Double.parseDouble(""+h.get("g"));
                msg = ""+h.get("Message");
                userid = ""+h.get("UserID");
                postid = ""+h.get("PostID");

                Log.d("hsdhdhjsdhj",l+" "+g+" "+" "+msg);

                postsfrmDB.add(new PostClusterItem(msg,new LatLng(l,g),userid,postid));
            }
        }
        Log.d("postdsdkjs",""+postsfrmDB.size());
        setMsgIcon2(mClusterManager,postsfrmDB);
    }

    private void readCameraChanges(){
        final float zoom = mMap.getCameraPosition().zoom;
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mClusterManager.cluster();
                //mClusterManager.setRenderer(renderer);

            }
        });

    }

    private void onpenTipBottoSheet(){
        TipBSDialogue t = new TipBSDialogue();
        t.show(getSupportFragmentManager(),"hfhf");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllPosts();
        badgeBottomNavigtion.apply(selectedId,getString(R.color.colorAccent), getString(R.color.tipeeGreenDark));
    }


    @Override
    public void buttonClicked(Boolean choice) {
        //open tip activity here
        if(choice){
            startActivity(new Intent(ErrandMapActivity.this,NewPost.class));
        }
    }
}
