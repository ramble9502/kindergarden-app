package com.example.monic.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by monic on 2017/10/6.
 */

public class GroupChatActivity extends AppCompatActivity {
    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_main;
    private DatabaseReference databaseReference;
    final int RESULT_CODE=101;

    //Add Emojicon
    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton;
    EmojIconActions emojIconActions;
    String classroom,email,useremail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);


        final Intent intent=this.getIntent();
        classroom=intent.getStringExtra("Classroom");

        activity_main = (RelativeLayout)findViewById(R.id.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Add Emoji
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText)findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_main,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        email=firebaseUser.getEmail();
        useremail=email.replace("@","").replace(".","");
        databaseReference=FirebaseDatabase.getInstance().getReference("class_information/"+classroom+"/stu_profile/"+useremail);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserDetail userDetail=dataSnapshot.getValue(UserDetail.class);
                        DatabaseReference myRef=FirebaseDatabase.getInstance().getReference("class_information/"+classroom+"/groupchatroom");
                        ChatMessage chatMessage=new ChatMessage(emojiconEditText.getText().toString(),userDetail.getTextViewUserName(),userDetail.getImageurl());
                        myRef.push().setValue(chatMessage);
                        emojiconEditText.setText("");
                        emojiconEditText.requestFocus();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        //Check if not sign-in then navigate Signin page
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_main,"歡迎來到班級共同聊天室：",Snackbar.LENGTH_SHORT).show();
            //Load content
            displayChatMessage();
        }


    }





    private void displayChatMessage() {

        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
        DatabaseReference newRed=FirebaseDatabase.getInstance().getReference("class_information/"+classroom+"/groupchatroom");
        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.chatgrouplist_item,newRed)
        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                //Get references to the views of list_item.xml
                TextView messageText, messageUser, messageTime;
                ImageView messageimage;
                messageText = (EmojiconTextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);
                messageimage=(ImageView)v.findViewById(R.id.chatgroup_item);

                Picasso.with(v.getContext())
                        .load(model.getMessageImg())
                        .transform(new CircleTransform())
                        .into(messageimage);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(model.getMessageTime());


            }
        };
        listOfMessage.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return  true;
    }
}
