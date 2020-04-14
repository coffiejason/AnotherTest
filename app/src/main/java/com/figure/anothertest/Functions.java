package com.figure.anothertest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

class Functions {

    private static List<TPPost> list = new ArrayList<>();
    private static List<TPPost> myList = new ArrayList<>();

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
        hashMap.put("UserID",userID);

        DatabaseReference ref = userDB.child(userID);
        ref.child("Posts").push().setValue(hashMap);



    }

    void comment(DatabaseReference userDB, String postUserID, String text){
       // userDB.child(postUserID).child("Posts").
    }

    void getAllPosts(final ClusterManager<PostClusterItem>cm,DatabaseReference userDB){
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
                    redundantCode(d,list,true,cm);
                    Log.d("checkhashmap",""+hashMap.get("Message"));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void getMyPosts(DatabaseReference userDB){

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                redundantCode(dataSnapshot,myList,false,null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setMsgIcon(ClusterManager<PostClusterItem> cm, double l, double g, String message){
        LatLng pl = new LatLng(l,g);

        //code below for regular posts
        cm.addItem(new PostClusterItem(message, pl));
        cm.cluster();
    }

    Bitmap layoutToBitmap(int layout, Context c) {

        LayoutInflater layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
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

    void sideMenu(final Activity a, Toolbar tb){
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Profile Options").withIcon(R.drawable.ic_profile);
        SecondaryDrawerItem item2,item3, world, errands, myposts,myreplies;

        item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Create Errand");

        item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Notification Options").withIcon(R.drawable.ic_bell);

        world = new SecondaryDrawerItem().withName("World").withIcon(R.drawable.ic_world);

        errands = new SecondaryDrawerItem().withName("Errands").withIcon(R.drawable.ic_errands);

        myposts = new SecondaryDrawerItem().withName("My Posts").withIcon(R.drawable.ic_myposts);

        myreplies = new SecondaryDrawerItem().withName("My Replies").withIcon(R.drawable.ic_replies);

        item1.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                a.startActivity(new Intent(a,EditProfileActivity.class));
                return false;
            }
        });

        world.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                a.startActivity(new Intent(a,WorldActivity.class));
                return false;
            }
        });

        errands.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                a.startActivity(new Intent(a,Errands.class));
                return false;
            }
        });

        myposts.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                a.startActivity(new Intent(a,MyPosts.class));
                return false;
            }
        });

        myreplies.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                a.startActivity(new Intent(a,MyReplies.class));
                return false;
            }
        });


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(a)
                .withHeaderBackground(R.color.colorAccent)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withIcon(a.getResources().getDrawable(R.drawable.egg))
                )
                .build();

//create the drawer and remember the `Drawer` result object
         new DrawerBuilder()
                .withActivity(a)
                .withToolbar(tb)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,item2,
                        new DividerDrawerItem()
                        ,
                        world,
                        errands,
                        myposts,
                        myreplies,
                        new SecondaryDrawerItem().withIcon(R.drawable.ic_moon)
                )
                .build();
    }

    private void redundantCode(DataSnapshot dataSnapshot, List<TPPost> list, boolean setIcon, ClusterManager cm){

        HashMap<String, Object> h = new HashMap<>();
        String msg;
        double l,g;
        for(DataSnapshot p: dataSnapshot.child("Posts").getChildren()){

            for(DataSnapshot finaSnapShot: p.getChildren()){

                Log.d("ypyppypypAllMsgsFinallyyy",finaSnapShot+"");
                h.put(""+finaSnapShot.getKey(),""+finaSnapShot.getValue());
            }
            l = Double.parseDouble(""+h.get("l"));
            g = Double.parseDouble(""+h.get("g"));
            msg = ""+h.get("Message");

            Log.d("hsdhdhjsdhj",l+" "+g+" "+" "+msg);

            if(setIcon){
                setMsgIcon(cm,l,g,msg);
            }

            list.add(new TPPost(msg,l,g));






        }

    }

    List<TPPost> getWorldPosts(){
        return list;
    }

    List<TPPost> getMyPostsList(){
        return myList;
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