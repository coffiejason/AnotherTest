package com.figure.anothertest;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChooseLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    float l,g;
    int radius;
    String msg;

    LatLng defaultLocation, finalLocation;

    String userID;
    DatabaseReference userDB;

    RelativeLayout postBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        l = intent.getFloatExtra("default_l",(float)0.00000);
        g = intent.getFloatExtra("default_g",(float)0.00000);
        msg = intent.getStringExtra("message");
        defaultLocation = new LatLng(l,g);

        if( FirebaseAuth.getInstance().getCurrentUser() != null){
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }


        userDB = FirebaseDatabase.getInstance().getReference().child("Customers available");

        postBtn = findViewById(R.id.lc_done);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                Log.i("centerLat",""+cameraPosition.target.latitude);

                Log.i("centerLong",""+cameraPosition.target.longitude);

                finalLocation = new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude);
            }
        });

        googleMap.setMyLocationEnabled(true);
    }

    protected void post(){
        if(finalLocation != null){
            new Functions().creatPostText(userDB,userID,msg, (float) finalLocation.latitude, (float) finalLocation.longitude,radius);
            finish();
        }

    }


}
