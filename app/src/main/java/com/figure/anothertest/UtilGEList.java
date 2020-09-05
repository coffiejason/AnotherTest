package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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

public class UtilGEList extends AppCompatActivity {

    RecyclerView rv;
    List<UtilitiesERitem> list = new ArrayList<>();
    Toolbar tb;

    DatabaseReference userDB;
    String userID;
    String tasknum,posterID;
    View parentLayout;

    private static List<UtilitiesERitem> utilityerrands = new ArrayList<>();

    //uploading deps
    RelativeLayout uploadBtn;
    ProgressBar pb;
    ImageView uploadImg, doneImg;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Watsan Demo");
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CWSA_userbase");

    HashMap<String, Object> h = new HashMap<>();

    int i = 0;

    int progressDiff = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util_g_e_list);
        tb = findViewById(R.id.toolbar);
        rv = findViewById(R.id.recyclerViewuer);
        parentLayout = findViewById(android.R.id.content);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userDB = FirebaseDatabase.getInstance().getReference().child("MeterRequests");
        userDB.keepSynced(true);

        tasknum = getIntent().getStringExtra("tasknum");
        posterID = getIntent().getStringExtra("posterID");

        //Toast.makeText(this, ""+tasknum, Toast.LENGTH_SHORT).show();

        checkUtilityErrands(userDB,getApplicationContext());

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showList();

        //endTasksAlert();

        init();
    }

    void init(){
        uploadBtn = findViewById(R.id.er_upload_button);
        uploadImg = findViewById(R.id.erubid);
        doneImg = findViewById(R.id.erdid);
        pb = findViewById(R.id.erpbid);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);

                if(!utilityerrands.isEmpty()){
                    //Toast.makeText(getApplicationContext(),""+(100/utilityerrands.size()),Toast.LENGTH_SHORT).show();
                    progressDiff = 100/utilityerrands.size();
                }

                if(SharedPrefs.getMeterRead("reading"+(utilityerrands.size()-1)) == "none" ){
                    Toast.makeText(getApplicationContext(),"Get remaining readings to complete errand",Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadReadings();
                }

            }
        });
    }

    public void checkUtilityErrands(DatabaseReference db, final Context c){
        utilityerrands.clear();
        db.child(tasknum+"/Customer List").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //get tiperID here and write to ErrandsCompleted node when errand is complete

                //Log.d("howmnytimes"," "+i);
                //i++;
                //Log.d("howmnytimes"," "+i+": "+dataSnapshot.child("Areacode").getValue());

                utilityerrands.add(new UtilitiesERitem(""+dataSnapshot.child("Name").getValue(),""+dataSnapshot.child("Meterno").getValue(),new LatLng(Double.parseDouble(""+dataSnapshot.child("l").getValue()),Double.parseDouble(""+dataSnapshot.child("g").getValue())),""+dataSnapshot.child("Areacode").getValue()));
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
            UtilitiesERAdapter adapter = new UtilitiesERAdapter(UtilGEList.this,utilityerrands,tasknum);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

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
                            public void onSuccess(Uri uri2) {
                                Log.d("srwqsdxd","were here");
                                Log.d("srwqsdxd",""+h.get("meterno"));
                                h.put("imgurl",""+uri2);
                                ref.child(posterID).child("31082020").child("mt"+h.get("meterno")).setValue(h);
                                //finish();
                                pb.setProgress(pb.getProgress()+progressDiff);
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

        //i++;
    }

    void isComplete(){
        for(int i = 0; i < utilityerrands.size(); i++){
            SharedPrefs.getMeternum("meternum"+i);

            Log.d("hes done all errands",""+SharedPrefs.getMeternum("meternum"+i));

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        //checkUtilityErrands(userDB,getApplicationContext());
        showList();

        //endTasksAlert();
    }
}
