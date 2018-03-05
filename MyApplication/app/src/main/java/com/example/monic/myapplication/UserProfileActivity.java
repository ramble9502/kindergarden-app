package com.example.monic.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.firebase.ui.auth.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by monic on 2017/9/21.
 */

public class UserProfileActivity extends AppCompatActivity {

    private ImageView imageView;
    private RoundImage roundImage;
    private String email, classroom, imageurl;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    private TextView textViewUserName, textViewChildname, usermail,
            textViewChildclass,textViewUsercontact, textViewUseraddress, useremail;
    private Button buttonEditprofile;
    private String imageprofile;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageView=(ImageView)findViewById(R.id.profilephoto);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.profile_photo);
        roundImage=new RoundImage(bitmap);
        imageView.setImageDrawable(roundImage);
        mAuth=FirebaseAuth.getInstance();


        final Intent intent=this.getIntent();

        final String classroomuser=intent.getStringExtra("Classroom");


        textViewUserName=(TextView)findViewById(R.id.textViewUserName);
        textViewUsercontact=(TextView)findViewById(R.id.textViewUsercontact);
        textViewUseraddress=(TextView)findViewById(R.id.textViewUseraddress);
        textViewChildname=(TextView)findViewById(R.id.textViewChildname);
        textViewChildclass=(TextView)findViewById(R.id.textViewChildclass);
        useremail=(TextView)findViewById(R.id.usermail);
        buttonEditprofile=(Button)findViewById(R.id.buttonEditprofile);
        imageView=(ImageView)findViewById(R.id.profilephoto);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        String emailuser=user.getEmail().replace("@","").replace(".","");
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,HomeActivity.class));
        }
        useremail.setText(user.getEmail());
        textViewChildclass.setText(classroomuser);


        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference("class_information/"+classroomuser+"/stu_profile/"+emailuser);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    UserDetail userdetail=dataSnapshot.getValue(UserDetail.class);
                    textViewUserName.setText("家長真實姓名 ： "+userdetail.textViewUserName);
                    textViewUseraddress.setText("孩童真實姓名 ： "+userdetail.textViewUseraddress);
                    textViewChildclass.setText( "孩童校園班級 ： "+userdetail.textViewChildclass);
                    textViewChildname.setText( "孩童家長關係 ： "+userdetail.textViewChildname);
                    textViewUsercontact.setText( "孩童實際住址 ： "+userdetail.textViewUsercontact);
                    Picasso.with(getApplicationContext())
                            .load(userdetail.imageurl)
                            .error(R.drawable.profile_photo)
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                    roundImage=new RoundImage(imageBitmap);
                                    imageView.setImageDrawable(roundImage);

                                }

                                @Override
                                public void onError() {

                                }
                            });

                }
                else {

                    Intent intent=new Intent(UserProfileActivity.this,ProfileEditActivity.class);
                    intent.putExtra("Classroom",classroomuser);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        buttonEditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserProfileActivity.this,ProfileEditActivity.class);
                intent.putExtra("Classroom",classroomuser);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.action_settings:
                mAuth.signOut();
                finish();
                startActivity(new Intent(UserProfileActivity.this,MainActivity.class));
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return  true;
    }





}
