package com.example.monic.myapplication;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email,password,name;
    private Button signin;
    FirebaseUser user;
    public static FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(mDatabase==null){
            mDatabase=FirebaseDatabase.getInstance();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            //the firebase database client will cache synchronize data and keep track of all write you're initiated while
            //your application is running
        }
        mAuth=FirebaseAuth.getInstance();
        signin=(Button)findViewById(R.id.buttonSignin);

        email=(EditText)findViewById(R.id.editTextEmail);
        password=(EditText)findViewById(R.id.editTextPassword);

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getemail=email.getText().toString().trim();
                String getpassword=password.getText().toString().trim();
                callsignin(getemail,getpassword);
            }
        });


        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent emailIntent=new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"monica2021323@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_CC,new String[]{"snsd520cnblueforever@gmail.com "});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"相關聯絡事項(包含問題、諮詢等)");
                emailIntent.putExtra(Intent.EXTRA_TEXT,"書寫內容");
                emailIntent.setType("message/rfc882");
                startActivity(Intent.createChooser(emailIntent,"Choose email client...."));
                //Snackbar.make(view,"Replace with your own action",Snackbar.LENGTH_LONG).setAction("Action",null).show();
            }
        });
    }


    private void callsignin(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.w("Testing","signInWithEmail:failed",task.getException());
                    Toast.makeText(MainActivity.this,"登入失敗，請確認帳號密碼或聯絡管理員",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.w("Testing","signinWithEmail:SUCCESS");
                    Intent i=new Intent(MainActivity.this,HomeActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        });
    }


}
