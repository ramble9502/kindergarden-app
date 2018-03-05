package com.example.monic.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.monic.myapplication.bean.EventSet;
import com.example.monic.myapplication.fragment.ScheduleFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.freesoulapps.preview.android.Preview;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by monic on 2017/12/5.
 */

public class ContactActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Query lastQuery;
    private RecyclerView contextrecycler;
    private Button contactbutton;
    public FirebaseRecyclerAdapter<Contact,Show_Contact_ViewHolder> mFirebaseContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactbook);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        contextrecycler=(RecyclerView)findViewById(R.id.contact_list_view);
        contactbutton=(Button)findViewById(R.id.contactbutton);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        contextrecycler.setHasFixedSize(true);
        contextrecycler.setLayoutManager(new LinearLayoutManager(this));

        final Intent intent=this.getIntent();
        final String classroom=intent.getStringExtra("Classroom");
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("class_information/"+classroom+"/contactbook");
        Log.d("databaseReference",databaseReference.toString());
        mFirebaseContactAdapter=new FirebaseRecyclerAdapter<Contact, Show_Contact_ViewHolder>(Contact.class,R.layout.contactbook_item,Show_Contact_ViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(Show_Contact_ViewHolder viewHolder, Contact model, int position) {
                viewHolder.Contact_Date(model.getDate());
                viewHolder.Contact_Title(model.getTitle());
                viewHolder.Contact_Honorlist(model.getHonorlist());
                viewHolder.Contact_Image(getApplicationContext(),model.getImage());
                viewHolder.Contact_Teacher(model.getTeacher());
                viewHolder.Contact_Content(model.getContent());
                viewHolder.Contact_Recommand(getApplication(),model.getRecommend());

            }
        };
        contextrecycler.setAdapter(mFirebaseContactAdapter);
        contactbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ContactActivity.this,ContactsignActivity.class);
                i.putExtra("Classroom",classroom);
                startActivity(i);
            }
        });

    }


    public static class Show_Contact_ViewHolder extends RecyclerView.ViewHolder implements Preview.PreviewListener{
        private final TextView date,content,honorlist,teacher,title;
        private final ImageView image;
        private LinearLayout.LayoutParams params;
        private Button buttonsign;
        private LinearLayout layout;
        private Preview mPreview;

        public Show_Contact_ViewHolder(final View itemView){
            super(itemView);
            date=(TextView)itemView.findViewById(R.id.update_date);
            content=(TextView)itemView.findViewById(R.id.contactdetail);
            title=(TextView)itemView.findViewById(R.id.contacttitle);
            teacher=(TextView)itemView.findViewById(R.id.contacteditor);
            image=(ImageView)itemView.findViewById(R.id.contactimg);
            honorlist=(TextView)itemView.findViewById(R.id.contacthonor);
            mPreview=(Preview)itemView.findViewById(R.id.preview);

            params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        private void Contact_Date(String update){date.setText(update);}
        private void Contact_Content(String contactdetail){content.setText(contactdetail);}
        private void Contact_Teacher(String contactteacher){teacher.setText(contactteacher);}
        private void Contact_Image(Context ctxx,String contactimage){
            Picasso.with(ctxx)
                    .load(contactimage)
                    .into(image);
            Log.d("contactimage","image");
        }
        private void Contact_Honorlist(String contacthonorlist){honorlist.setText(contacthonorlist);}
        private void Contact_Title(String contacttitle){title.setText(contacttitle);}
        private void Contact_Recommand(final Context context, final String command){
            mPreview.setListener(this);
            mPreview.setData(command);
            mPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse(command));
                    context.startActivity(intent);
                }
            });
        }
        private void Layout_hide(){
            params.height=0;
            layout.setLayoutParams(params);
        }
        @Override
        public void onDataReady(Preview preview) {
        }

    }
}
