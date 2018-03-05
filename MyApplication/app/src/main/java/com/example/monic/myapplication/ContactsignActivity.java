package com.example.monic.myapplication;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by monic on 2017/12/5.
 */

public class ContactsignActivity extends Activity{
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static int RESULT_LOAD_IMAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    private String today,name,classroom,id;
    private DatabaseReference databaseReference;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_sign);
        verifyStoragePermissions(this);
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });
        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar mCal = Calendar.getInstance();
                String dateformat = "yyyyMMdd";
                SimpleDateFormat df = new SimpleDateFormat(dateformat);
                today = df.format(mCal.getTime());
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                name=firebaseUser.getEmail();
                id=firebaseUser.getUid();
                final Intent intent=getIntent();
                classroom=intent.getStringExtra("Classroom");
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                signatureBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] data=baos.toByteArray();

                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(classroom).child("contactbook").child(today).child(id);
                UploadTask uploadTask=storageReference.putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl=taskSnapshot.getDownloadUrl();
                        databaseReference= FirebaseDatabase.getInstance().getReference("class_information/"+classroom+"/contactsign/"+today+"/"+id);
                        Contactsign contactsign=new Contactsign();
                        contactsign.setUsercreate(name);
                        contactsign.setDownloadurl(downloadUrl.toString());
                        databaseReference.setValue(contactsign);
                        Toast.makeText(ContactsignActivity.this,"完成上傳回到首頁....",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(ContactsignActivity.this,MainActivity.class);
                        startActivity(i);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ContactsignActivity.this, "無法傳送圖片...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
