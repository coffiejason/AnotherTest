package com.figure.anothertest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.drawee.backends.pipeline.Fresco;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/* POSSIBLE BUGS
 **APP CANT DIFFERENTIATE B/N USERS
 * */


//add code to http post request to show notification when a user posts data

public class ErrandMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,ClusterManager.OnClusterClickListener<PostClusterItem>{

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    Toolbar tb;

    RelativeLayout mapLayoutSub;

    //private int radius = 1; //should be custom adjusted by user via UI, determines the range of other Users avaialable

    String userID;

    DatabaseReference userAvailabilityRef;
    DatabaseReference userIndividual;
    TPClusterRenderer renderer;

    LatLng defaultLocation;
    Bundle args;

    AutocompleteSupportFragment autocompleteFragment;


    private ClusterManager<PostClusterItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_errand_map);

        new TPMessagingService();

        init();

        getToken();

        //Log.d("SharedPrefsToken22222",SharedPrefs.getInstance(ErrandMapActivity.this).getToken()+"");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("Maps Activity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Maps Activity", "Can't find style. Error: ", e);
        }*/

        buildGoogleApiClient();

        mClusterManager = new ClusterManager<>(ErrandMapActivity.this, mMap);
        //request for User permission
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraIdleListener(mClusterManager);
        //mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.cluster();
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

        renderer = new TPClusterRenderer(ErrandMapActivity.this,mMap,mClusterManager);
        mClusterManager.setRenderer(renderer);

        new Functions().saveUser(userIndividual,location,userID);

        new Functions().getAllPosts(mClusterManager,userAvailabilityRef);

        new Functions().getMyPosts(userAvailabilityRef.child(userID));
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
            i++;
        }

        bundle.putInt("iterator",i);

        Intent intent = new Intent(ErrandMapActivity.this, OpenPostsCluster.class);
        intent.putExtras(bundle);
        startActivity(intent);

        return true;
    }

    void init(){

        tb = findViewById(R.id.mapToolbar);

        mapLayoutSub = findViewById(R.id.notifyer_space);

        new Functions().sideMenu(ErrandMapActivity.this,tb);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        FirebaseMessaging.getInstance().subscribeToTopic("topicsname");

        userAvailabilityRef = FirebaseDatabase.getInstance().getReference().child("Customers available");
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

        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

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


}
