package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetLocationActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    EditText etLocation,etname,etmeternum;

    DatabaseReference db,userIndividual;
    RelativeLayout submitBtn;

    String l,g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        db = FirebaseDatabase.getInstance().getReference().child("CWSA_userbase");
        db.keepSynced(true);
        userIndividual = db.child("Data_collection");
        submitBtn = findViewById(R.id.submitBtn);

        etname = findViewById(R.id.name_cd);
        etmeternum = findViewById(R.id.meternum_cd);

        final SwipeRefreshLayout swipe = findViewById(R.id.getLocatonsswipelayout);
        etLocation = findViewById(R.id.lUsername);

        getLocation();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLocation();
                swipe.setRefreshing(false);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Functions().saveCL(db,etname.getText().toString(),etmeternum.getText().toString(),l,g);

                etLocation.clearComposingText();
                etmeternum.clearComposingText();
                etLocation.clearComposingText();
            }
        });
    }

    void getLocation(){

        //Spinner spinner = (Spinner) findViewById(R.id.spinner);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.i("LOCATION", location.toString());
                Toast.makeText(getApplicationContext(),"L:"+location.getLatitude()+" G:"+location.getLongitude(),Toast.LENGTH_SHORT).show();
                etLocation.setText("L:"+location.getLatitude()+" G:"+location.getLongitude());
                l = ""+location.getLatitude();
                g = ""+location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
        }

       // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);

       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       // spinner.setAdapter(adapter);

    }
}
