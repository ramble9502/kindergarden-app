package com.example.monic.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by monic on 2017/11/4.
 */

public class EbookActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager mLinearLayoutManager;
    public FirebaseRecyclerAdapter<EbookNumber,Show_Ebook_ViewHolder> mFirebasebookAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_ebook_layout);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth= FirebaseAuth.getInstance();


        myRef=FirebaseDatabase.getInstance().getReference().child("ebook_detail");

        progressBar=(ProgressBar)findViewById(R.id.show_ebook_progressBar);
        recyclerView=(RecyclerView)findViewById(R.id.show_ebook_recyclerview);

        mLinearLayoutManager=new LinearLayoutManager(EbookActivity.this);
        mLinearLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }



    @Override
    public void onStart(){
        super.onStart();
        progressBar.setVisibility(ProgressBar.VISIBLE);
        final Intent intent=this.getIntent();
        mFirebasebookAdapter=new FirebaseRecyclerAdapter<EbookNumber, Show_Ebook_ViewHolder>(EbookNumber.class,R.layout.show_ebook_single_item,Show_Ebook_ViewHolder.class,myRef) {
            @Override
            protected void populateViewHolder(final Show_Ebook_ViewHolder viewHolder, final EbookNumber model, int position) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot datachild: dataSnapshot.getChildren()){
                            String key=datachild.getKey();
                            String url=(String)datachild.child("url").getValue();
                            String author=(String)datachild.child("contact").getValue();
                            viewHolder.Ebook_name(key);
                            viewHolder.Ebook_image(url);
                            viewHolder.Ebook_author(author);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //viewHolder.Ebook_author(model.getEbookauthor());
                //viewHolder.Ebook_image(model.getEbookurl());
                //viewHolder.Ebook_name(model.getEbookname());
            }
        };
        recyclerView.setAdapter(mFirebasebookAdapter);
    }
    public static class Show_Ebook_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView ebook_image;
        private final TextView ebook_author,ebook_name;
        private final LinearLayout layout;
        final LinearLayout.LayoutParams params;

        public Show_Ebook_ViewHolder(final View itemView){
            super(itemView);
            ebook_name=(TextView)itemView.findViewById(R.id.ebookname);
            ebook_author=(TextView)itemView.findViewById(R.id.ebookauthor);
            ebook_image=(ImageView)itemView.findViewById(R.id.ebookimage);
            layout=(LinearLayout)itemView.findViewById(R.id.show_ebook_single_item_layout);
            params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setOnClickListener(this);
        }
        private void Ebook_name(String name){
            ebook_name.setText(name);
        }
        private void Ebook_author(String author){
            ebook_author.setText(author);
        }
        private void Ebook_image(String url){
            if(!url.equals("Null")){
                Picasso.with(itemView.getContext())
                        .load(url)
                        .into(ebook_image);
            }
        }
        @Override
        public void onClick(View view) {
            Log.d("ebook_name",ebook_name.getText().toString());
            String ebookName=ebook_name.getText().toString();
            Intent intent=new Intent(view.getContext(),EbookdetailActivity.class);
            intent.putExtra("ebookName",ebookName);
            view.getContext().startActivity(intent);
        }

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
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
                startActivity(new Intent(EbookActivity.this,MainActivity.class));
                break;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return  true;
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
