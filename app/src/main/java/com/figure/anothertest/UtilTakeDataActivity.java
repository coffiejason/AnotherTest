package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.figure.anothertest.utils.AlbumStorageDirFactory;
import com.figure.anothertest.utils.BaseAlbumDirFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class UtilTakeDataActivity extends AppCompatActivity {
    String name,meterno,town;
    TextView userdetails;
    RelativeLayout postPicbtn,imageclick,finishbtn;
    CardView image,finishbtnview;
    Toolbar tb;

    EditText meterNumInput;

    private String mCurrentPhotoPath;

    private File imagefile;

    /*FOR CAMERA INTENT */
    private static final int ACTION_TAKE_PHOTO = 1;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    Bitmap mImageBitmap;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private ImageView ivSelected;

    Context context;
    DatabaseReference ref;

    HashMap<String, Object> h = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util_take_data);

        ActivityCompat.requestPermissions(UtilTakeDataActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        ref = FirebaseDatabase.getInstance().getReference().child("Watsan Demo");
        init();
        initListeners();
    }

    void init(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        context = UtilTakeDataActivity.this;
        userdetails = findViewById(R.id.user);
        finishbtn = findViewById(R.id.finisherrandbtn);
        imageclick = findViewById(R.id.recieved_img);
        postPicbtn = findViewById(R.id.post_pic_button);
        image = findViewById(R.id.post_pic);
        finishbtnview = findViewById(R.id.fbtn);
        tb = findViewById(R.id.toolbar);
        ivSelected = findViewById(R.id.recievedImage);
        meterNumInput = findViewById(R.id.meterno_edittext);

        //finishbtnview.setVisibility(View.GONE);
        //image.setVisibility(View.GONE);

        name = getIntent().getStringExtra("Name");
        meterno = getIntent().getStringExtra("Meterno");
        town = getIntent().getStringExtra("Town");

        userdetails.setText(name+" - "+meterno);

        mAlbumStorageDirFactory = new BaseAlbumDirFactory();
    }

    void initListeners(){
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postPicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
            }
        });

        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String meterReading = meterNumInput.getText().toString();

                if(meterReading.isEmpty()){
                    Toast.makeText(UtilTakeDataActivity.this,"Enter Metre Usage Number",Toast.LENGTH_SHORT).show();
                }
                else if(meterReading.length() < 5){
                    Toast.makeText(UtilTakeDataActivity.this,"Invalid Metre Usage Number",Toast.LENGTH_SHORT).show();
                }
                else {
                    Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", imagefile);
                    imageUpload(UtilTakeDataActivity.this,uri,"WatsanDemo");
                }
            }
        });
    }

    void imageUpload(final Context context, Uri uri,String eventID){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(eventID);

        //image or video name add to list and pass to tiper
        final String imageName = "meterimage"+Calendar.getInstance().getTimeInMillis()+getExtension(context,uri);
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
                                h.put("Name",name);
                                h.put("imgurl"," "+uri);
                                h.put("Usage",meterNumInput.getText().toString()+" ");
                                ref.child(town).child(meterno).setValue(h);
                                finish();

                                Log.d("agocatchyou",""+name);
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

    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        File f = null;

        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }
        startActivityForResult(takePictureIntent, actionCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACTION_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Log.d("qwgasg", "dsnkdcksndkjnsdk");
                handleBigCameraPhoto();
            }
        }
    }

    private File setUpPhotoFile() throws IOException {

        imagefile = createImageFile();
        mCurrentPhotoPath = imagefile.getAbsolutePath();

        return imagefile;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private void setPic() {

        Picasso.get().load(FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", imagefile)).into(ivSelected);
        ivSelected.setVisibility(View.VISIBLE);
    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            Log.d("ajbh","ubyqwy");
            galleryAddPic();
            setPic();
            mCurrentPhotoPath = null;
        }

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        ivSelected.setImageBitmap(mImageBitmap);
        ivSelected.setVisibility(savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? ImageView.VISIBLE : ImageView.INVISIBLE
        );

    }

    private String getAlbumName() {
        return "MeterImages";
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        snack(ivSelected, " failed to create directory");
                        return null;
                    }
                }
            }

        }

        return storageDir;
    }

    public void snack(View view, String message){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }
}
