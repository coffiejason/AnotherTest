package com.figure.anothertest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.figure.anothertest.bottomvavigation.BadgeBottomNavigtion;
import com.figure.anothertest.bottomvavigation.BottomAdapter;
import com.figure.anothertest.bottomvavigation.BottomItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

//write user location to firebase at login and or signup

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

    RelativeLayout mapLayoutSub,listBtn;

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

    public static List<TPPost> allposts = new ArrayList<>();
    public static LatLng myLocation;


    private ClusterManager<PostClusterItem> mClusterManager;
    private static Collection<PostClusterItem> postsfrmDB;

    public static List<User> availableUsers;
    private int gotErrand = 0;

    int AUTOCOMPLETE_REQUEST_CODE = 100;

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

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

        onpenTipBottoSheet();

        getAllPosts();
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

        Log.d("allpostsList",""+allposts.size());



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

        RelativeLayout post_button = findViewById(R.id.post_errand);

        args = new Bundle();

        //hide api
        Places.initialize(getApplicationContext(),"AIzaSyAYWdmSCJ9MyVh0bvBFbrQb9ELgmu3LYu8");

        PlacesClient placesClient = Places.createClient(this);

        //AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                //getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        //assert autocompleteFragment != null;
        //autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        /*
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Toast.makeText(ErrandMapActivity.this,""+place.getLatLng().latitude+" "+place.getLatLng().longitude+" ",Toast.LENGTH_SHORT).show();
                Log.d("PlacesGot", "Place: " + place.getName() + ", " + place.getId());
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),15));
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.d("PlacesError", "An error occurred: " + status);
            }
        });*/

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

    int i = 0;

    void getAllPosts(){
        //use Location.distanceBetween() to check if coordinates are in a given radius
        //list.clear();
        i++;

        Log.d("howmanyimes"," "+i);
        userAvailabilityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> hashMap = new HashMap<>();
                String userid;
                double l,g;

                postsfrmDB.clear();
                availableUsers.clear();
                allposts.clear();
                mClusterManager.clearItems();
                for(DataSnapshot d: dataSnapshot.getChildren()){

                    Log.d("Igotthekeyskeyskeys",""+d.getKey());
                    userid = d.getKey();
                    //use location.distancebetween here to get only the keys in users location
                    redundantCode(d);
                    if(d.child("Location").child("g").getValue() !=null){

                        g = (double) d.child("Location").child("g").getValue();
                        l = (double) d.child("Location").child("l").getValue();

                        Log.d("closestsUsers"," "+userid+"  "+l+" "+g);

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

                allposts.add(new TPPost(msg,l,g,userid,false));

                postsfrmDB.add(new PostClusterItem(msg,new LatLng(l,g),userid,postid));
            }
        }
        Log.d("postdsdkjs",""+postsfrmDB.size());
        setMsgIcon2(mClusterManager,postsfrmDB);

        for(DataSnapshot p: dataSnapshot.child("Errands").getChildren()){

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

                allposts.add(new TPPost(msg,l,g,userid,true));

                postsfrmDB.add(new PostClusterItem(msg,new LatLng(l,g),userid,postid));
            }
        }
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

    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("GH")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    public boolean isServicesOk(){
        Log.d("isServicesOk","Checking Goolge Services Version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ErrandMapActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is Ok
            Log.d(TAG,"isServicesOk, GooglePlay Services is Working");
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //resolvable error eg version issue
            Log.d(TAG,"Error occurred but can be fix");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(ErrandMapActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(ErrandMapActivity.this,"You cant make map requests",Toast.LENGTH_SHORT);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("PlacesGot", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                Toast.makeText(ErrandMapActivity.this, "ID: " + place.getId() + "address:" + place.getAddress() + "Name:" + place.getName() + " latlong: " + place.getLatLng(), Toast.LENGTH_LONG).show();
                String address = place.getAddress();
                // do query with address

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(ErrandMapActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                Log.i("ErrorTAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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
            startActivity(new Intent(ErrandMapActivity.this,UtititiesERActivity.class));
            Log.d("howmnyavailableusers"," "+availableUsers.size());
        }
    }
}
