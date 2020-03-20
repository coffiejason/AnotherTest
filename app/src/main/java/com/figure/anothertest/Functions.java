package com.figure.anothertest;

import android.location.Location;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class Functions {

    public void getUsers(DatabaseReference userDB, LatLng post_location,int radius){


        GeoFire geoFire = new GeoFire(userDB);
        //show indicator for users available, say a color indicator, green for many, yellow for few red for little



        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(post_location.latitude,post_location.longitude),radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            boolean driverFound = false;

            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!driverFound){
                    driverFound = true;
                    String driverFoundID = key;
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if(!driverFound){
                    /*
                    radius = radius + 1; //radius will be determined by user
                    //recursion code below
                    getDriver();*/
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    public void saveUserLocation(DatabaseReference userDBReference, Location userLocation, String userID){
        GeoFire geoFire = new GeoFire(userDBReference);
        geoFire.setLocation(userID, new GeoLocation(userLocation.getLatitude(),userLocation.getLongitude()),new
                GeoFire.CompletionListener(){
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                    }
                });
    }

    public void removeUser(DatabaseReference userDBReference,String userID){
        GeoFire geoFire = new GeoFire(userDBReference);
        geoFire.removeLocation(userID);
    }
}