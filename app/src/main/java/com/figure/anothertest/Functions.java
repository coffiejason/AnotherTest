package com.figure.anothertest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

class Functions {
    private static List<String> utilimageuris = new ArrayList<>();
    private static List<String> mediauris = new ArrayList<>();
    private static List<String> imageNames = new ArrayList<>();
    private static List<ErrandResItem> resItems = new ArrayList<>();
    private static List<String> errandsNearBy = new ArrayList<>();
    private static List<ErrandItem> errandsNearBy2 = new ArrayList<>();
    private static List<UtilitiesERitem> utilityerrands = new ArrayList<>();
    private static List<TPPost> myList = new ArrayList<>();
    private static List<TPPost> allusersloc = new ArrayList<>();
    private static Collection<PostClusterItem> postsfrmDB = new ArrayList<>();
    int redcodenum = 0;
    int allpostsnum = 0;
    int allpostsinner = 0;

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

    void creatErrand(DatabaseReference userDB, String userID, String message, float l, float g){
        //add location parameter to Hashmap
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Message",message);
        hashMap.put("l",l);
        hashMap.put("g",g);
        hashMap.put("UserID",userID);

        DatabaseReference ref = userDB.child(userID);

        ref.child("Errands").push().setValue(hashMap);
        whoGetsNotified(ErrandMapActivity.availableUsers,new LatLng(l,g),message,userID);
    }

    void comment(DatabaseReference commentDBref, String postUserID, String message) {
       //commentDBref passed to this fun

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Message",message);
        hashMap.put("UserID",postUserID);

        commentDBref.child("Comments").push().setValue(hashMap);


    }

    void getComments(DatabaseReference postDBref, final List<TPPost> commentList) {

        postDBref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Object> h = new HashMap<>();
                String userid,postid;
                String msg;

                commentList.clear();
                for(DataSnapshot p: dataSnapshot.child("Comments").getChildren()){

                    h.put("PostID",p.getKey());

                    for(DataSnapshot finaSnapShot: p.getChildren()){

                        Log.d("ypyppypypAllMsgsFinallyyy",finaSnapShot+"");
                        h.put(""+finaSnapShot.getKey(),""+finaSnapShot.getValue());
                    }
                    msg = ""+h.get("Message");
                    userid = ""+h.get("UserID");
                    postid = ""+h.get("PostID");

                    Log.d("commmenttt",""+msg);


                    commentList.add(new TPPost(msg,userid,postid));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    /*
    void getAllPosts(DatabaseReference userDB, final Collection<PostClusterItem> postsfrmDB, final ClusterManager cm){
        //use Location.distanceBetween() to check if coordinates are in a given radius
        //list.clear();
        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> hashMap = new HashMap<>();
                String userid;
                double l,g;
                int i = 0;

                allpostsinner++;
                Log.d("allpostinnerrr",""+allpostsinner);
                //list.clear();
                for(DataSnapshot d: dataSnapshot.getChildren()){

                    Log.d("Igotthekeyskeyskeys",""+d.getKey());

                    //use location.distancebetween here to get only the keys in users location
                    redundantCode(d,postsfrmDB,cm);

                    //g = (double) d.child("Location").child("g").getValue();
                    //l = (double) d.child("Location").child("l").getValue();
                    //userid = (String) d.child("UserID").child("UserID").getValue();

                    //allusersloc.add(new TPPost(userid,l,g,userid));

                    //Log.d("qwertpo",""+l+" "+g+" "+userid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void redundantCode(DataSnapshot dataSnapshot,Collection<PostClusterItem> postsfrmDB,ClusterManager cm){

        HashMap<String, Object> h = new HashMap<>();
        String userid;
        String postid;
        String msg;
        double l,g;

        //list.clear();
        //myList.clear();
        //rList.clear();
        //postsfrmDB.clear();
        Log.d("redcodemun",""+redcodenum);
        redcodenum++;
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

                //rList.add(new TPPost(msg,l,g,userid,postid));
                postsfrmDB.add(new PostClusterItem(msg,new LatLng(l,g),userid,postid));
            }
        }
        Log.d("postdsdkjs",""+postsfrmDB.size());
        setMsgIcon2(cm,postsfrmDB);


    }

    private void setMsgIcon2(ClusterManager<PostClusterItem> cm, Collection<PostClusterItem> posts){

        cm.addItems(posts);
        cm.cluster();
    }
    */

    void getMyPosts(final GoogleMap mMap, DatabaseReference userDB){

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myList.clear();
                //redundantCode(mMap,dataSnapshot,false,null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void getErrandsDone(DatabaseReference errandsNode){
        //errands node is the node where new errands are written to if a user matches a location errand criteria


        errandsNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String message;
                List<String> uris = new ArrayList<>();

                //get the key below programmactically, when errand is complete, tipee rights to tiper newerrandcompleted node
                //tiper reads that node and notifies user on that errand is complete, tiper also gets the key from that node and deletes node
                message = ""+dataSnapshot.child("-M8t2NspO06LGgog7M42").child("Message").getValue();
                for(DataSnapshot d: dataSnapshot.child("-M8xr1ZLU4BapuMGkT6X").child("Media").getChildren()){
                    uris.add(""+d.getValue());
                }

                resItems.add(new ErrandResItem(message,uris));

                Log.d("wersews","we dey hereeeeee "+resItems.size()+" "+ uris.get(0));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("qazxdesxrdsdfghjhgffgh","here dey run "+resItems.size());
    }

    private void setMsgIcon(GoogleMap mMap, ClusterManager<PostClusterItem> cm, double l, double g, String message, String userid, String postid){
        LatLng pl = new LatLng(l,g);

        mMap.clear();
        cm.addItem(new PostClusterItem(message, pl,userid,postid));
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

    void notifyUserswithTopic(Context c,TPPost post, boolean errand){
        RequestQueue queue = Volley.newRequestQueue(c);
        String URL = "https://fcm.googleapis.com/fcm/send";

        String nt;

        if(errand){
            nt = "New Task !";
        }else{
            nt = "New Messages around you";
        }


        //whoGetsNotified(db,post,1000,topic,errand);

        JSONObject  mainObj = new JSONObject();

        //write topic to all user in post range



        try {
            mainObj.put("to", "/topics/" +"topicsname");
            JSONObject notification = new JSONObject();
            notification.put("title", ""+nt);
            notification.put("body", ""+post.getpMessage());
            mainObj.put("notification", notification);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("sucesssss", "" + response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("failllleedd", "" + error);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("Content-Type","application/json");
                    header.put("Authorization","key=AAAAtEBMzJ4:APA91bGmAnHTXM1D0TMl4xWO-cKB3dqCVqg8bR8Ncf64GQJgJP-SRNzo9bQ-6WwtBNimHU72JAbIKnL8Rj1BIqEL99TUOCJd4PGxKdBV7vOYd1tRi1NgzE5zxeRvRngv2LNCxy1LxLm_");
                    return header;
                }
            };

            queue.add(request);
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    void sideMenu(final Activity a, Toolbar tb){
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Profile Options").withIcon(R.drawable.ic_profile);
        SecondaryDrawerItem item2,item3, world, errands, myposts,myreplies;

        item3 = new SecondaryDrawerItem().withIdentifier(2).withName("Create Errand");

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

        item3.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                a.startActivity(new Intent(a,Tper1Activity.class));
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
                        new ProfileDrawerItem().withName("Jasonn").withIcon(a.getResources().getDrawable(R.drawable.egg))
                )
                .build();

//create the drawer and remember the `Drawer` result object
         new DrawerBuilder()
                .withActivity(a)
                .withToolbar(tb)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,item2,item3,
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

    static void whoGetsNotified(List<User> list,LatLng post,String msg,String tiperID){
        Location postLocation = new Location("post");
        postLocation.setLongitude(post.longitude);
        postLocation.setLatitude(post.latitude);

        HashMap<String,String> usertodisance = new HashMap<>();

        Location userLocation = new Location("userLocation");
        float[] users = new float[list.size()];

        for(int i = 0; i<list.size();i++){
            userLocation.setLatitude(list.get(i).getLat());
            userLocation.setLongitude(list.get(i).getLng());

            float distance = postLocation.distanceTo(userLocation);
            usertodisance.put(""+distance,list.get(i).getUserID());

            Log.d("alldistances", " "+distance);
            users[i] = distance;


        }
        Arrays.sort(users);

        String closetuserid = usertodisance.get("" + users[0]);

        //Log.d("cosssdsfsdf", Objects.requireNonNull(usertodisance.get("" + users[0])));

        assert closetuserid != null;
        DatabaseReference closestuserdb = FirebaseDatabase.getInstance().getReference().child("Customers available")
                .child(closetuserid);


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Message",msg);
        hashMap.put("tiperID",tiperID);

        closestuserdb.child("ErrandsNearBy").push().setValue(hashMap);


    }

    private void onpenTipBottoSheet(FragmentManager fm){
        TipBSDialogue t = new TipBSDialogue();
        t.show(fm,"hfhf");
    }

    int i = 0;

    public void checkforErrands(DatabaseReference db, final Context c){

        db.child("ErrandsNearBy").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //get tiperID here and write to ErrandsCompleted node when errand is complete

                Log.d("newErrandAvailable","You new errandss ohhhh");
                Toast.makeText(c,"You Have a new Task",Toast.LENGTH_SHORT).show();

                Log.d("howmnytimes"," "+i);
                i++;
                Log.d("howmnytimes"," "+i+": "+dataSnapshot.child("Message").getValue());

                errandsNearBy.add(""+dataSnapshot.child("Message").getValue());
                errandsNearBy2.add(new ErrandItem(""+dataSnapshot.child("tiperID").getValue(),""+dataSnapshot.child("Message").getValue()));

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

    public void checkUtilityErrands(DatabaseReference db, final Context c){

        db.child("Watsan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //get tiperID here and write to ErrandsCompleted node when errand is complete

                Log.d("howmnytimes"," "+i);
                i++;
                Log.d("howmnytimes"," "+i+": "+dataSnapshot.child("Areacode").getValue());

                utilityerrands.add(new UtilitiesERitem(""+dataSnapshot.child("Name").getValue(),""+dataSnapshot.child("Meterno").getValue(),new LatLng(Double.parseDouble(""+dataSnapshot.child("l").getValue()),Double.parseDouble(""+dataSnapshot.child("g").getValue())),""+dataSnapshot.child("Areacode").getValue()));
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

    private void getNotifyTopics(){
        //suscribes user to errand topics available to them

        String topic =  "";//read topics from database and pass to string topic

        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    private static class LoadComments extends AsyncTask<Void,Void,List<TPPost>>{
        DatabaseReference userDB; List<TPPost> list; RecyclerView rv; Context c;

        LoadComments(){}

        LoadComments( DatabaseReference userDB,List<TPPost> commentList, RecyclerView rv,Context c){
            this.userDB = userDB;
            this.list = commentList;
            this.rv = rv;
            this.c = c;
        }

        @Override
        protected List<TPPost> doInBackground(Void... voids) {
            userDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    HashMap<String, Object> h = new HashMap<>();
                    String userid,postid;
                    String msg;
                    List<TPPost> updatedlist = new ArrayList<>();

                    list.clear();
                    for(DataSnapshot p: dataSnapshot.child("Comments").getChildren()){

                        h.put("PostID",p.getKey());

                        for(DataSnapshot finaSnapShot: p.getChildren()){

                            Log.d("ypyppypypAllMsgsFinallyyy",finaSnapShot+"");
                            h.put(""+finaSnapShot.getKey(),""+finaSnapShot.getValue());
                        }
                        msg = ""+h.get("Message");
                        userid = ""+h.get("UserID");
                        postid = ""+h.get("PostID");

                        Log.d("commmenttt",""+msg);


                        list.add(new TPPost(msg,userid,postid));
                    }

                    Log.d("jsahjqwgh",""+list.size());

                }





                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

           // new LoadComments().onPostExecute(list);

            Log.d("wedeyhereohyo",list.size()+"");
            return list;
        }

        @Override
        protected void onPostExecute(List<TPPost> tpPosts) {
            super.onPostExecute(tpPosts);

            Log.d("wedeyh12345o"," "+tpPosts.size());

            String[] postMsgs,postUserids,postids;

            postMsgs = new String[tpPosts.size()+1];
            postUserids = new String[tpPosts.size()+1];
            postids = new String[tpPosts.size()+1];

            TPPost post;

            int j = 0;

            for(int i = 0; i<= tpPosts.size()-1; i++){

                post = tpPosts.get(i);

                postMsgs[i] = post.getpMessage();
                postUserids[i] = post.getpUserID();
                postids[i] = post.getpPostID();

                Log.d("whatthamessage",j+" :"+postMsgs[i]);
                j++;
            }

            ClusterListAdapter adapter = new ClusterListAdapter(c,postMsgs,postUserids,postids);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(c));
            tpPosts.clear();
        }
    }

    private void enableDarkmode(boolean choice){

    }

    void fileUploader(final Context context, Uri uri, final ProgressBar pb, final ImageView iv, final RelativeLayout rl,String eventID){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(eventID);

        //image or video name add to list and pass to tiper
        final String imageName = System.currentTimeMillis()+" "+getExtension(context,uri);
        final StorageReference ref = storageReference.child(imageName);

        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        String downloadUrl = taskSnapshot.getUploadSessionUri().toString();
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri mediauri) {
                                mediauris.add(""+mediauri);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("imageloaderror",""+e);
                            }
                        });
                        pb.setVisibility(View.GONE);
                        iv.setVisibility(View.VISIBLE);
                        rl.setClickable(false);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });

    }

    void imageUpload(final Context context, Uri uri,String eventID){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(eventID);

        //image or video name add to list and pass to tiper
        final String imageName = "meterimage"+getExtension(context,uri);
        final StorageReference ref = storageReference.child(imageName);

        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        String downloadUrl = taskSnapshot.getUploadSessionUri().toString();
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                utilimageuris.add(""+uri);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("imageloaderror",""+e);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });

    }

    void loadImages(StorageReference ref, final ImageView imageView){

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("imageloaderror",""+e);
            }
        });
    }

     String getExtension(Context c,Uri uri){
        ContentResolver cr = c.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void downloadImg(StorageReference storageRef){

        //end code below to your liking
        storageRef.child("users/me/profile.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    List<String> getImagesNames(){return imageNames;}
    void clearImageNames(){imageNames.clear();}

    /*
    List<TPPost> getWorldPosts(){
        return list;
    }*/

    List<String> getMediauris() { return mediauris; }

    List<String> getUtilimageuris() { return utilimageuris; }

    List<TPPost> getMyPostsList(){
        return myList;
    }

     List<ErrandResItem> getResItems() {

        Log.d("qazxcvb",""+resItems.size());
        return resItems;
    }

    List<String> getErrandsNearBy(){
        return errandsNearBy;
    }

    List<ErrandItem> getErrandsNearBy2(){
        Collections.reverse(errandsNearBy2);
        return errandsNearBy2;
    }

    List<UtilitiesERitem> getUtilityerrands(){
        Collections.reverse(utilityerrands);
        return utilityerrands;
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