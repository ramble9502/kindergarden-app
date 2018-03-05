package com.example.monic.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by monic on 2017/9/19.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private LinearLayout linearLayoutcontact;
    private LinearLayout linearLayoutchatroom;
    private LinearLayout linearLayoutmemory;
    private  LinearLayout linearLayoutebook;
    private LinearLayout linearLayoutuserprofile;
    private LinearLayout linearLayoutannounce;
    static String LoggedIn_User_Email;
    public static int Device_Width;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearLayoutcontact=(LinearLayout) findViewById(R.id.contact_icon);
        linearLayoutchatroom=(LinearLayout)findViewById(R.id.chatroom_icon);
        linearLayoutmemory=(LinearLayout)findViewById(R.id.memory_icon);
        linearLayoutebook=(LinearLayout)findViewById(R.id.ebook_icon);
        linearLayoutuserprofile=(LinearLayout)findViewById(R.id.stuinformation_icon);
        linearLayoutannounce=(LinearLayout)findViewById(R.id.annouce_icon);

        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        Device_Width = metrics.widthPixels;


        if(MainActivity.mDatabase == null){
            MainActivity.mDatabase= FirebaseDatabase.getInstance();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        mAuth=FirebaseAuth.getInstance();


        if(mAuth.getCurrentUser() == null){
            this.finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        FirebaseUser user=mAuth.getCurrentUser();
        LoggedIn_User_Email=user.getEmail();

        linearLayoutcontact.setOnClickListener(this);
        linearLayoutannounce.setOnClickListener(this);
        linearLayoutchatroom.setOnClickListener(this);
        linearLayoutebook.setOnClickListener(this);
        linearLayoutmemory.setOnClickListener(this);
        linearLayoutuserprofile.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        if(view==linearLayoutcontact){
            FirebaseUser user=mAuth.getCurrentUser();
            final String username = user.getEmail().replace("@","").replace(".","");
            DatabaseReference myRef=FirebaseDatabase.getInstance().getReference("users");
            myRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInformation userInformation=dataSnapshot.getValue(UserInformation.class);
                    Intent i=new Intent(HomeActivity.this,ContactActivity.class);
                    i.putExtra("Classroom",userInformation.getClassroom());
                    startActivity(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        if(view==linearLayoutchatroom){
            FirebaseUser user=mAuth.getCurrentUser();
            final String username = user.getEmail().replace("@","").replace(".","");
            DatabaseReference myRef=FirebaseDatabase.getInstance().getReference("users");
            myRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInformation userInformation=dataSnapshot.getValue(UserInformation.class);
                    Intent i=new Intent(HomeActivity.this,ChatActivity.class);
                    i.putExtra("Classroom",userInformation.getClassroom());
                    startActivity(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        if(view==linearLayoutmemory){
            FirebaseUser user=mAuth.getCurrentUser();
            final String username = user.getEmail().replace("@","").replace(".","");
            DatabaseReference myRef=FirebaseDatabase.getInstance().getReference("users");
            myRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInformation userInformation=dataSnapshot.getValue(UserInformation.class);
                    Intent i=new Intent(HomeActivity.this,DynamicardActivity.class);
                    i.putExtra("Classroom",userInformation.getClassroom());
                    startActivity(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if(view==linearLayoutebook){
            Intent i=new Intent(HomeActivity.this,EbookActivity.class);
            startActivity(i);
        }
        if(view==linearLayoutuserprofile){
            FirebaseUser user=mAuth.getCurrentUser();
            final String username = user.getEmail().replace("@","").replace(".","");
            DatabaseReference myRef=FirebaseDatabase.getInstance().getReference("users");
            myRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInformation userInformation=dataSnapshot.getValue(UserInformation.class);
                    Intent i=new Intent(HomeActivity.this,UserProfileActivity.class);
                    i.putExtra("Email",userInformation.getEmail());
                    i.putExtra("Classroom",userInformation.getClassroom());
                    i.putExtra("Classroom",userInformation.getClassroom());
                    startActivity(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        if(view==linearLayoutannounce){
            FirebaseUser user=mAuth.getCurrentUser();
            final String username = user.getEmail().replace("@","").replace(".","");
            DatabaseReference myRef=FirebaseDatabase.getInstance().getReference("users");
            myRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInformation userInformation=dataSnapshot.getValue(UserInformation.class);
                    Intent i=new Intent(HomeActivity.this,ScheduleMainActivity.class);
                    i.putExtra("Email",userInformation.getEmail());
                    i.putExtra("Classroom",userInformation.getClassroom());
                    startActivity(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                mAuth.signOut();;
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        return false;
    }
}
