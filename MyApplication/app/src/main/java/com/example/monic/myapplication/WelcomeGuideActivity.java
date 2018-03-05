package com.example.monic.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.monic.myapplication.global.AppConstants;
import com.example.monic.myapplication.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monic on 2017/9/16.
 */

public class WelcomeGuideActivity extends Activity implements View.OnClickListener {
    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private Button startBtn;
    private static final String ACTIVITY_TAG="LogDemo";
    private static final int[] pics={
            R.layout.guid_view1,R.layout.guid_view2,
            R.layout.guid_view3, R.layout.guid_view4   };

    private ImageView[] dots;

    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        views=new ArrayList<View>();
        for(int i=0;i<pics.length;i++){
            View view= LayoutInflater.from(this).inflate(pics[i], null);
            if (i == pics.length - 1) {
                startBtn = (Button) findViewById(R.id.btn_enter);
                startBtn.setTag(1);
                startBtn.setOnClickListener(this);
            }
            views.add(view);
        }
        vp=(ViewPager)findViewById(R.id.vp_guide);
        adapter=new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new PageChangeListener());
        initDots();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        SpUtils.putBoolean(WelcomeGuideActivity.this, AppConstants.FIRST_OPEN,true);
        finish();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    private void initDots(){
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.line1);
        dots=new ImageView[pics.length];

        for(int i=0;i<pics.length;i++){
            dots[i]=(ImageView)linearLayout.getChildAt(i);
            dots[i].setEnabled(false);
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);
        }
        currentIndex=0;
        dots[currentIndex].setEnabled(true);
    }
    private void setCurView(int position){
        if(position<0 || position>=pics.length){
            return;
        }
        vp.setCurrentItem(position);
    }
    private void setCurDot(int position){
        if(position<0 || position>pics.length ||currentIndex==position){
            return;
        }
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(true);
        currentIndex=position;
    }
    @Override
    public void onClick(View v){
        if(v.getTag().equals(1)){
            enterMainActivity();
            return;
        }
        int position=(Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }
    private void enterMainActivity(){
        Intent intent=new Intent(WelcomeGuideActivity.this,SplashActivity.class);
        startActivity(intent);
        SpUtils.putBoolean(WelcomeGuideActivity.this,AppConstants.FIRST_OPEN,true);
        finish();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //滑動時調用此方法

        }

        @Override
        public void onPageSelected(int position) {
            setCurDot(position);
            if(position==3){
                LinearLayout linearLayout=(LinearLayout)findViewById(R.id.line1);
                linearLayout.setVisibility(LinearLayout.GONE);
                startBtn.setVisibility(0);
                Log.d(WelcomeGuideActivity.ACTIVITY_TAG,"position==3");
            }
            //此法頁面跳轉候用，position 是當前選中的頁面position
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //狀態改變使用，state 1.SCROLL_STATE_DRAGGING（1），按在屏幕開始拖動。
            //2.SCROLL_STATE_IDLE（0） 滑動動畫做完的狀態
            //3.SCROLL_STATE_SETTLING（2） 手指離開屏幕
        }
    }

}
