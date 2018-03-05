package com.example.monic.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.monic.myapplication.global.AppConstants;
import com.example.monic.myapplication.utils.SpUtils;

/**
 * Created by monic on 2017/9/16.
 */

public class SplashActivity extends Activity {

    private static final String ACTIVITY_TAG="LogDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);

        if(!isFirstOpen){
            Intent intent=new Intent(this,WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enterHomeActivity();
            }
        },2000);

    }

    private void enterHomeActivity(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
