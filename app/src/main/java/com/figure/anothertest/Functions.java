package com.figure.anothertest;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

class Functions {

    //show posts
    //show users

    /*

    public void loadPosts(DatabaseReference userDB, Location location,int radius){
        //user DB with all users

        GeoFire geoFire = new GeoFire(userDB);
        //show indicator for users available, say a color indicator, green for many, yellow for few red for little

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(),location.getLongitude()),radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            boolean driverFound = false;
            int keyid = 0;

            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.d("Key:"+keyid,""+key);



            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

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

    */

    void saveUser(DatabaseReference userDBReference, Location userLocation, String userID){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("l",userLocation.getLatitude());
        hashMap.put("g",userLocation.getLongitude());
        userDBReference.child("Location").setValue(hashMap);

        GeoFire geoFire = new GeoFire(userDBReference.child("Geofire"));
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

    void creatPostText(DatabaseReference userDB, String userID, String message, float l, float g, int radius){
        //add location parameter to Hashmap
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Message",message);
        hashMap.put("l",l);
        hashMap.put("g",g);
        hashMap.put("radius",radius);

        DatabaseReference ref = userDB.child(userID);
        ref.child("Posts").push().setValue(hashMap);



    }

    void getAllPosts(DatabaseReference userDB, final GoogleMap map){
        //use Location.distanceBetween() to check if coordinates are in a given radius

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> hashMap = new HashMap<>();
                String msg;
                double l,g;

                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Log.d("Igotthekeyskeyskeys",""+d.getKey());

                    //use location.distancebetween here to get only the keys in users location


                    for(DataSnapshot childSnapshot: d.child("Posts").getChildren()){

                        for(DataSnapshot finaSnapShot: childSnapshot.getChildren()){

                            Log.d("AllMsgsFinallyyy",finaSnapShot+"");
                            hashMap.put(""+finaSnapShot.getKey(),""+finaSnapShot.getValue());
                        }
                        Log.d("HashMapWorking3",""+hashMap.get("Message"));
                        l = Double.parseDouble(""+hashMap.get("l"));
                        g = Double.parseDouble(""+hashMap.get("g"));
                        msg = ""+hashMap.get("Message");

                        //Log.d("HashMapWorking3",""+l+" "+" "+g+" "+msg);

                        setMsgIcon2(map,l,g,msg);

                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*
    void setMsgIcon(ClusterManager<PostClusterItem> cm, double l, double g, String message){
        LatLng pl = new LatLng(l,g);

        //code below for regular posts
        cm.addItem(new PostClusterItem(message, pl));
        cm.cluster();
    }
    */
    private void setMsgIcon2(GoogleMap map, double l, double g, String message){
        LatLng pl = new LatLng(l,g);

        map.addMarker(new MarkerOptions().position(pl).title(message));
    }


}