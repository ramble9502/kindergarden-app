package com.example.monic.myapplication;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by monic on 2017/9/27.
 */

public class ChatActivity extends AppCompatActivity{
    TextView person_name,person_email;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    LinearLayoutManager mLinearLayoutManager;
    String classroom;
    public FirebaseRecyclerAdapter<UserDetail,Show_Chat_ViewHolder> mFirebaseAdapter;
    final int RESULT_CODE=101;
    final int REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_chat_layout);

        final Intent intent=this.getIntent();
        classroom=intent.getStringExtra("Classroom");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String useremail=user.getEmail().replace("@","").replace(".","");
        firebaseDatabase=FirebaseDatabase.getInstance();

        mAuth=FirebaseAuth.getInstance();

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myRef=firebaseDatabase.getReference().child("class_information/"+classroom+"/stu_profile");
        progressBar=(ProgressBar)findViewById(R.id.show_chat_progressBar2);
        recyclerView=(RecyclerView)findViewById(R.id.show_chat_recyclerView);

        mLinearLayoutManager=new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(mLinearLayoutManager);

    }
    @Override
    public void onStart(){
        super.onStart();
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        progressBar.setVisibility(ProgressBar.VISIBLE);
        final Intent intent=this.getIntent();
        final String classroom=intent.getStringExtra("Classroom");
        mFirebaseAdapter=new FirebaseRecyclerAdapter<UserDetail, Show_Chat_ViewHolder>(UserDetail.class,R.layout.show_chat_single_item,Show_Chat_ViewHolder.class,myRef) {
            @Override
            protected void populateViewHolder(Show_Chat_ViewHolder viewHolder, UserDetail model, final int position) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                //if(!model.getTextViewUserName().equals("Null")){
                viewHolder.Person_Name(model.getTextViewUserName());
                viewHolder.Person_Image(model.getImageurl());
                if(model.getUserdetailemail().equals(HomeActivity.LoggedIn_User_Email)){
                    viewHolder.Layout_hide();
                }
                else {
                    viewHolder.Person_Email(model.getUserdetailemail());
                }
                //viewHolder.Layout_hide();
                //Log.d("Test","Not null");
                //}

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference ref=mFirebaseAdapter.getRef(position);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String retrieve_name=dataSnapshot.child("textViewUserName").getValue(String.class);
                                String retrieve_email=dataSnapshot.child("userdetailemail").getValue(String.class);
                                String retrieve_url=dataSnapshot.child("imageurl").getValue(String.class);
                                Intent intent=new Intent(getApplicationContext(),ChatConversationActivity.class);
                                intent.putExtra("image_id",retrieve_url);
                                intent.putExtra("email",retrieve_email);
                                intent.putExtra("name",retrieve_name);
                                intent.putExtra("Classroom",classroom);
                                startActivityForResult(intent,REQUEST_CODE);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        };
        recyclerView.setAdapter(mFirebaseAdapter);
    }



    public static class Show_Chat_ViewHolder extends RecyclerView.ViewHolder{
        private final TextView person_name,person_email;
        private final ImageView person_image;
        private final LinearLayout layout;
        final  LinearLayout.LayoutParams params;

        public Show_Chat_ViewHolder(final View itemView){
            super(itemView);
            person_name=(TextView)itemView.findViewById(R.id.chat_persion_name);
            person_email=(TextView)itemView.findViewById(R.id.chat_persion_email);
            person_image=(ImageView)itemView.findViewById(R.id.chat_persion_image);
            layout=(LinearLayout)itemView.findViewById(R.id.show_chat_single_item_layout);
            params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        private void Person_Name(String title){
            person_name.setText(title);
        }
        private void Layout_hide(){
            params.height=0;
            layout.setLayoutParams(params);
        }
        private void Person_Email(String title){
            person_email.setText(title);
        }
        private void Person_Image(String url){
            if(!url.equals("NUll")){
                Picasso.with(itemView.getContext())
                        .load(url)
                        .transform(new CircleTransform())
                        .into(person_image);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.chatroom_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.action_settings:
                mAuth.signOut();
                finish();
                startActivity(new Intent(ChatActivity.this,MainActivity.class));
                break;
            case R.id.menu_item_share:
                Intent i=new Intent(ChatActivity.this,GroupChatActivity.class);
                i.putExtra("Classroom",classroom);
                startActivity(i);
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return  true;
    }


}
