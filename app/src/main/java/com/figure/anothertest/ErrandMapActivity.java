package com.figure.anothertest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;


/* POSSIBLE BUGS
 **APP CANT DIFFERENTIATE B/N USERS
 * */

public class ErrandMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    public static boolean makeErrand = false;
    private LatLng senderErrandLocation;// same aas customer Pickup location

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;

    private String customerID;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private DatabaseReference customerDatabaseRef;
    private LatLng customerRequestLocation;
    private RelativeLayout post_button;
    private RelativeLayout post_fb;

    private RelativeLayout mapActivity;

    private DatabaseReference driveraLocationRef;
    private int radius = 1; //should be custom adjusted by user via UI, determines the range of other Users avaialable

    private boolean driverFound = false;
    private String driverFoundID; //would be an array of found User IDs

    private View child;

    String userID;

    String loc;

    DatabaseReference userAvailabilityRef;

    LatLng defaultLocation;

    Bundle args;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errand_map);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userAvailabilityRef = FirebaseDatabase.getInstance().getReference().child("Customers available");



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        customerID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        post_button = findViewById(R.id.post_errand);

        mapActivity = findViewById(R.id.maplayoutRelative);

        args = new Bundle();






        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //mapActivity.addView(child);

                Intent intent = new Intent(ErrandMapActivity.this, CreatePost.class);
                intent.putExtra("default_l",args.getFloat("l"));
                intent.putExtra("default_g",args.getFloat("g"));
                startActivity(intent);
                //CustomIntent.customType(ErrandMapActivity.this,"left-to-right");

                /*
                GeoFire geoFire = new GeoFire(customerDatabaseRef);
                geoFire.setLocation(customerID,new GeoLocation(lastLocation.getLatitude(),lastLocation.getLongitude()));

                customerRequestLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(customerRequestLocation).title("Errand Location here")); //make marker movable

                getDriver();

                 */
            }

        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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

        //request for User permission
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        //locationRequest.setInterval(1000);  //update location every secend
        //locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY); //change for better accuracy

        //original location service code
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

        Log.d("myTag", args.getFloat("l")+" "+args.getFloat("g"));


        //code to save user location to Firebase
        new Functions().saveUserLocation(userAvailabilityRef,location,userID);

    }

    protected synchronized void buildGoogleApiClient(){
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



}
