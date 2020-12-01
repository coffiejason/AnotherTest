package com.figure.anothertest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.multidex.MultiDex;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.figure.anothertest.bottomvavigation.BadgeBottomNavigtion;
import com.figure.anothertest.bottomvavigation.BottomAdapter;
import com.figure.anothertest.bottomvavigation.BottomItem;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

//write user location to firebase at login and or signup

public class ErrandMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, BottomAdapter.BottomItemClickInterface, TipBSDialogue.BottomSheetListner {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    Toolbar tb;
    CardView newNotif;
    RelativeLayout notifIcon;

    RelativeLayout mapLayoutSub,listBtn;

    private BadgeBottomNavigtion badgeBottomNavigtion;

    //private int radius = 1; //should be custom adjusted by user via UI, determines the range of other Users avaialable

    String userID;

    DatabaseReference userAvailabilityRef;
    DatabaseReference userIndividual;


    LatLng defaultLocation;
    Bundle args;

    public static Bundle coordsLoc = new Bundle();
    public static String versionNum = "1.0.2";

    AutocompleteSupportFragment autocompleteFragment;

    //bottom nav stuff, might delete
    private final int HOME = 0;
    private final int NOTIFICATIONS = 1;
    private final int FRIENDS = 2;
    private final int SETTINGS = 3;
    private final int CART = 4;
    private int selectedId = 0;

    DatabaseReference ref,appversion;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        MultiDex.install(this);
        setContentView(R.layout.activity_errand_map);

        new TPMessagingService();

        badgeBottomNavigtion = new BadgeBottomNavigtion(findViewById(R.id.BottomNavigation), ErrandMapActivity.this, ErrandMapActivity.this);
        initBottomItems();

        init();


        getToken();

        //onpenTipBottoSheet();

        ref = FirebaseDatabase.getInstance().getReference().child("MeterRequests");

        getErrands(ref,getApplicationContext());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear();
                getErrands(ref,getApplicationContext());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

        coordsLoc.putFloat("l", (float) location.getLatitude());
        coordsLoc.putFloat("g", (float) location.getLongitude());

        new Functions().saveUser(userIndividual,location,userID);

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

    void init(){


        tb = findViewById(R.id.mapToolbar);

        newNotif = findViewById(R.id.toolbar_badge_parent);
        notifIcon  = findViewById(R.id.toolbar_body_parent);

        newNotif.setVisibility(View.GONE);

        listBtn = findViewById(R.id.listbtn);

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ErrandMapActivity.this,WorldActivity.class));
            }
        });

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

        //new Functions().checkforErrands(userIndividual,getApplicationContext());
        //new Functions().checkUtilityErrands(userIndividual,getApplicationContext());


        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("wouldthisowrkeverytime", SharedPrefs.getInstance(ErrandMapActivity.this).getToken() + "");
            }
        };

        registerReceiver(broadcastReceiver,new IntentFilter(TPMessagingService.TOKEN_BROADCAST));

        args = new Bundle();
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

    @Override
    public void itemSelect(int itemId) {
        //Toast.makeText(this," "+itemId,Toast.LENGTH_SHORT).show();
        switch (itemId){
            case 0:
                break;

            case 1:
                Intent i1 = new Intent(ErrandMapActivity.this,Tper1Activity.class);
                i1.putExtra("default_l",args.getFloat("l"));
                i1.putExtra("default_g",args.getFloat("g"));
                startActivity(i1);
                break;

            case 3:
                Intent i = new Intent(ErrandMapActivity.this,SettingsActivity.class);
                startActivity(i);
                break;
        }

    }

    public void getErrands(DatabaseReference db, final Context c){
        Log.d("dowegeterrand22s"," where here");
        //utilityerrands.clear();
        //errands.clear();
        final HashMap<String, Object> tasknums = new HashMap<>();
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //get tiperID here and write to ErrandsCompleted node when errand is complete

                Log.d("dowegeterrands"," where here ");

                HashMap<String, Object> h = new HashMap<>();
                HashMap<String, Object> j = new HashMap<>();
                tasknums.put("tasknum",""+dataSnapshot.getKey());

                for(DataSnapshot p: dataSnapshot.getChildren()){

                    h.put(p.getKey(),p.getValue());
                    //Log.d("dowegeterrands3"," "+tasknum);

                    for(DataSnapshot p2: p.getChildren()){

                        for(DataSnapshot p3: p2.getChildren()){
                            Log.d("tryagain2",""+p3);
                            Log.d("tryagain3",p3.getKey()+" - "+p3.getValue());
                            j.put(p3.getKey(),p3.getValue());



                        }
                        //Log.d("hhsswty1y12",""+j.get(eid+"Name"));
                        //utilityerrands.add(new UtilitiesERitem(""+j.get(eid+"Name"),""+j.get(eid+"Meterno"),new LatLng(Double.parseDouble(""+j.get(eid+"l")),Double.parseDouble(""+j.get(eid+"g"))),""+j.get(eid+"Areacode")));

                    }

                    //Log.d("valuuueeesss",""+h.get("TiperID")+" "+h.get("Message")+" "+h.get("posterID")+" "+h.get("STATUS")+" "+""+h.get("Date")+" "+j.get("l")+" "+j.get("g"));
                }

                Log.d("valuuueeesss",""+h.get("TiperID")+" "+h.get("Message")+" "+h.get("posterID")+" "+h.get("STATUS")+" "+""+h.get("Date")+" "+j.get("l")+" "+j.get("g"));

                if(j.get("l") != null){

                    setMsgIcon(mMap," "+h.get("Message"),new LatLng(Double.parseDouble(""+j.get("l")),Double.parseDouble(""+j.get("g"))),""+tasknums.get("tasknum"));


                }

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


    private void setMsgIcon(GoogleMap map, String title, LatLng latLng, final String tasknum){

        map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(new Functions().layoutToBitmap(R.layout.post_icon,getApplicationContext()))).title(title)).setSnippet(tasknum);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        Log.d("jkdsnsd"," "+tasknum);
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d("jkdsnsd2","we dey hererer "+marker.getSnippet());
                Intent intent = new Intent(getApplicationContext(),UtilGEList.class);
                intent.putExtra("tasknum",""+marker.getSnippet());
                startActivity(intent);
            }
        });
    }

    public void checkVersion(){

        appversion = FirebaseDatabase.getInstance().getReference().child("App-Version").child("current");

        getErrands(appversion,getApplicationContext());
        final HashMap<String, String> h = new HashMap<>();
        appversion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("appvvvv",""+dataSnapshot.getValue().toString());
                h.put("current",""+dataSnapshot.getValue().toString());

                if(dataSnapshot.getValue().toString() != null){
                    String temp = dataSnapshot.getValue().toString();
                    if(!versionNum.equals(dataSnapshot.getValue().toString())){
                        startActivity(new Intent(ErrandMapActivity.this,UpdateActivity.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        checkVersion();
        badgeBottomNavigtion.apply(selectedId,getString(R.color.colorAccent), getString(R.color.tipeeGreenDark));
    }

    @Override
    public void buttonClicked(Boolean choice) {
        //open tip activity here
        if(choice){
            startActivity(new Intent(ErrandMapActivity.this,GetLocationActivity.class));
        }
    }
}
