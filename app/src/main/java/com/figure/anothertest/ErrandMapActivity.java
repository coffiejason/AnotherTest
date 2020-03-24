package com.figure.anothertest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Collection;
import java.util.HashMap;

/* POSSIBLE BUGS
 **APP CANT DIFFERENTIATE B/N USERS
 * */

public class ErrandMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,ClusterManager.OnClusterClickListener<PostClusterItem>{

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;

    private int radius = 1; //should be custom adjusted by user via UI, determines the range of other Users avaialable

    String userID;

    DatabaseReference userAvailabilityRef;
    DatabaseReference userIndividual;

    LatLng defaultLocation;

    Bundle args;

    private ClusterManager<PostClusterItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_errand_map);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userAvailabilityRef = FirebaseDatabase.getInstance().getReference().child("Customers available");
        userIndividual = userAvailabilityRef.child(userID);

        RelativeLayout post_button = findViewById(R.id.post_errand);

        args = new Bundle();

        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ErrandMapActivity.this, CreatePost.class);
                intent.putExtra("default_l",args.getFloat("l"));
                intent.putExtra("default_g",args.getFloat("g"));
                startActivity(intent);
                Animatoo.animateSlideLeft(ErrandMapActivity.this);
                /*
                customerRequestLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(customerRequestLocation).title("Errand Location here")); //make marker movable
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

        mClusterManager = new ClusterManager<>(this, mMap);
        //request for User permission
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        //locationRequest.setInterval(1000);  //update location every secend
        //locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY); //change for better accuracy
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
        new Functions().saveUser(userIndividual,location,userID);
        new Functions().getAllPosts(userAvailabilityRef,mClusterManager);
        //put theres in oncreate
        TPClusterRenderer renderer = new TPClusterRenderer(ErrandMapActivity.this,mMap,mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setRenderer(renderer);
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

    public void getPost(DatabaseReference userDBReference){
        userDBReference.child("Posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                double l,g;
                String  msg;
                //Log.d("weback",""+dataSnapshot.child("Message").getValue().toString());

                l = Double.parseDouble(dataSnapshot.child("l").getValue().toString());
                g = Double.parseDouble(dataSnapshot.child("g").getValue().toString());
                msg = dataSnapshot.child("Message").getValue().toString();

                Log.d("Checkingmsgs",""+msg);
                new Functions().setMsgIcon(mClusterManager,l,g,msg);
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

    private Bitmap layoutToBitmap(int layout) {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layout, null);

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);

        view.draw(canvas);

        return bitmap;
    }
}
