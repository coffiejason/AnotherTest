package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UtititiesERActivity extends AppCompatActivity {

    //remove mt from meter ref.child()

    RecyclerView rv;
    List<UtilitiesERitem> list = new ArrayList<>();
    Toolbar tb;

    DatabaseReference userDB;
    String userID;

    RelativeLayout uploadBtn;
    ProgressBar pb;
    ImageView uploadImg, doneImg;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Watsan Demo");
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Watsan Demo");

    HashMap<String, Object> h = new HashMap<>();

    int i = 0;


    private static List<UtilitiesERitem> utilityerrands = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utitities_e_r);
        tb = findViewById(R.id.toolbar);
        rv = findViewById(R.id.recyclerViewuer);
        init();

        checkUtilityErrands(userDB,getApplicationContext());



        showList();


    }

    void init(){
        uploadBtn = findViewById(R.id.er_upload_button);
        uploadImg = findViewById(R.id.erubid);
        doneImg = findViewById(R.id.erdid);
        pb = findViewById(R.id.erpbid);


        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userDB = FirebaseDatabase.getInstance().getReference().child("Customers available").child(userID);
        userDB.keepSynced(true);

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);

                //pb.setProgress(pb.getProgress()+10);
                uploadReadings();
            }
        });
    }

    public void checkUtilityErrands(DatabaseReference db, final Context c){
        utilityerrands.clear();
        db.child("ErrandsNearBy/-MDyZpAmKlmeUVR8igZC/Customer List").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //get tiperID here and write to ErrandsCompleted node when errand is complete

                //Log.d("howmnytimes"," "+i);
                //i++;
                //Log.d("howmnytimes"," "+i+": "+dataSnapshot.child("Areacode").getValue());

                utilityerrands.add(new UtilitiesERitem(""+dataSnapshot.child("Name").getValue(),""+dataSnapshot.child("Meterno").getValue(),new LatLng(Double.parseDouble(""+dataSnapshot.child("l").getValue()),Double.parseDouble(""+dataSnapshot.child("g").getValue())),""+dataSnapshot.child("Areacode").getValue(),""+dataSnapshot.child("isIndoor").getValue()));
                //UtilitiesERAdapter adapter = new UtilitiesERAdapter(UtititiesERActivity.this,utilityerrands);
                //rv.setAdapter(adapter);
                //rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                showList();
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

    void showList(){
        if(utilityerrands.size() >0 ){
            UtilitiesERAdapter adapter = new UtilitiesERAdapter(UtititiesERActivity.this,utilityerrands);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            Log.d("sharedprefsvalue",""+SharedPrefs.getMeternum("meternum0"));
            Log.d("sharedprefsvalue2",""+utilityerrands.get(0).getMeterNum());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d("hownigareyou",""+utilityerrands.size());
        //checkUtilityErrands(userDB,getApplicationContext());
        //Log.d("hownigareyou",""+utilityerrands.size());
        showList();
    }

    void imageUpload(final Context context, Uri uri, String eventID){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(eventID);

        //image or video name add to list and pass to tiper
        final String imageName = "meterimage"+ Calendar.getInstance().getTimeInMillis()+getExtension(context,uri);
        final StorageReference sRef = storageReference.child(imageName);

        sRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        String downloadUrl = taskSnapshot.getUploadSessionUri().toString();
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //h.put("Name",name);
                                //h.put("imgurl"," "+uri);
                                //h.put("Usage",meterNumInput.getText().toString()+" ");
                                //ref.child(town).child(meterno).setValue(h);
                                //finish();

                                //Log.d("agocatchyou",""+name);
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

    String getExtension(Context c,Uri uri){
        ContentResolver cr = c.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    void uploadReadings(){


        Uri uri = Uri.parse(SharedPrefs.getImageUri("picread"+i).toString());

        //image or video name add to list and pass to tiper
        final String imageName = "meterimage"+ Calendar.getInstance().getTimeInMillis()+getExtension(getApplicationContext(),uri);
        final StorageReference sRef = storageReference.child(imageName);

        h.put("Name",utilityerrands.get(i).getCustomerName());
        h.put("imgurl",""+uri);
        h.put("Usage",SharedPrefs.getMeterRead("reading"+i));
        h.put("meterno",""+SharedPrefs.getMeternum("meternum"+i));

        Log.d("writinghis",""+utilityerrands.get(i).getCustomerName()+" "+uri+" "+SharedPrefs.getMeterRead("reading"+i)+" "+SharedPrefs.getMeternum("meternum"+i) );

        sRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        String downloadUrl = taskSnapshot.getUploadSessionUri().toString();
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("srwqsdxd","were here");
                                Log.d("srwqsdxd",""+h.get("meterno"));
                                ref.child("ABLJ").child("mt"+h.get("meterno")).setValue(h);
                                //finish();
                                pb.setProgress(pb.getProgress()+10);
                                Log.d("agocatchyou","uploaded sucessfully");

                                uploadReadings();
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

        Log.d("kjdsjkjs",""+pb.getProgress());

        i++;
    }





}
