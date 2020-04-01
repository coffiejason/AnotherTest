package com.figure.anothertest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

    void getAllPosts(final Context c,DatabaseReference userDB, final GoogleMap map){
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

                        setMsgIcon2(map,c,l,g,msg);

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
    private void setMsgIcon2(GoogleMap map,Context c, double l, double g, String message){
        LatLng pl = new LatLng(l,g);

        map.addMarker(new MarkerOptions().position(pl).title(message).icon(BitmapDescriptorFactory.fromBitmap(layoutToBitmap(R.layout.post_icon,c))));
    }

    private Bitmap layoutToBitmap(int layout, Context c) {

        LayoutInflater layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    void notifyUsers(Context c,String title, String message){
        RequestQueue queue = Volley.newRequestQueue(c);

        //change from Apache localhost server to a cloud server
        String url2 = "https://192.168.42.22/sennndddd/sendd.php?title="+title+"&message="+message;

        handleSSLHandshake();

        Log.d("doesthisevenwork","gjhkjlm");

        StringRequest myReq = new StringRequest(Request.Method.GET,
                url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Wegothere", "sthhh");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Sthwentwrong",""+error);
            }
        });

        queue.add(myReq);

    }

    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    private static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @SuppressLint("BadHostnameVerifier")
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }



}