package com.figure.anothertest;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.HashMap;

public class Tper1Activity extends AppCompatActivity {

    private RelativeLayout Postbtn;
    private Toolbar Closebtn;
    float l,g;

    EditText postEditText;
    HashMap<String, LatLng>location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_tper1);
        location = new HashMap<>();

        Intent intent = getIntent();

        //hide api
        Places.initialize(getApplicationContext(),"AIzaSyAYWdmSCJ9MyVh0bvBFbrQb9ELgmu3LYu8");
        PlacesClient placesClient = Places.createClient(this);

        l = intent.getFloatExtra("default_l",(float)0.00000);

        g = intent.getFloatExtra("default_g",(float)0.00000);

        init();
    }

    public void init(){
        Postbtn = findViewById(R.id.tper_btn_post);
        Closebtn = findViewById(R.id.tper_toolbar);
        postEditText = findViewById(R.id.tper_post_text);

        goToSearch();

        deliverToSearch();

        Closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        Postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if(location.get("Goto")!=null){
                    Toast.makeText(Tper1Activity.this,location.get("Goto")+"",Toast.LENGTH_SHORT).show();
                }*/

                if(postEditText.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,"We didn't quite get your Errand Message",3000);
                    snackbar.show();
                }
                else{
                    Intent i = new Intent(Tper1Activity.this,ChooseLocationActivity.class);
                    i.putExtra("default_l",l);
                    i.putExtra("default_g",g);
                    i.putExtra("message",postEditText.getText().toString());
                    i.putExtra("errand",true);
                    startActivity(i);
                }
            }
        });



    }

    protected void goToSearch(){


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.places_errand_location);

        autocompleteFragment.setHint("Go to ");

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("PlacesGot", "Place: " + place.getLatLng().latitude + ", " + place.getLatLng().latitude);
                location.put("Goto",place.getLatLng());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("PlacesError", "An error occurred: " + status);
            }
        });

    }

    protected void deliverToSearch(){

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.places_deliver_location);


        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setHint("Deliver to");

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

    }
}
