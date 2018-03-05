package com.example.monic.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.monic.myapplication.adapter.ScheduleAdapter;
import com.example.monic.myapplication.base.BaseActivity;
import com.example.monic.myapplication.base.BaseFragment;
import com.example.monic.myapplication.bean.EventSet;
import com.example.monic.myapplication.fragment.ScheduleFragment;
import com.example.monic.myapplication.widget.schedule.ScheduleRecyclerView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by monic on 2017/11/8.
 */

public class ScheduleMainActivity extends BaseActivity {

    public static int ADD_EVENT_SET_CODE=1;
    public static String ADD_EVENT_SET_ACTION="action.add.event.set";

    private DrawerLayout dlMain;
    private LinearLayout llTitleDate;
    private TextView tvTitleMonth,tvTitleDay;
    private RecyclerView rvMenuEventSetList;

    private BaseFragment mEventSetFragment;
    private BaseFragment mScheduleFragment;
    private List<EventSet> mEventSets;

    private EventSet mCurrentEventSet;
    private String[] mMonthText;
    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
    private String classroom;

    private ScheduleAdapter mScheduleAdapter;
    RecyclerView recyclerView;



    @Override
    protected void bindView(){
        setContentView(R.layout.activity_schedulemain);
        //dlMain=searchViewById(R.id.dlMain);
        llTitleDate=searchViewById(R.id.llTitleDate);
        tvTitleMonth=searchViewById(R.id.tvTitleMonth);
        tvTitleDay=searchViewById(R.id.tvTitleDay);
        initUi();
        gotoScheduleFragment();
    }

    private void initUi(){
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mMonthText=getResources().getStringArray(R.array.calendar_month);
        llTitleDate.setVisibility(View.VISIBLE);

        tvTitleMonth.setText(mMonthText[Calendar.getInstance().get(Calendar.MONTH)]);
        tvTitleDay.setText(getString(R.string.calendar_today));
    }



    @Override
    protected void initData(){
        super.initData();
        resetMainTitleDate(mCurrentSelectYear,mCurrentSelectMonth,mCurrentSelectDay);


    }

    public void resetMainTitleDate(int year,int month,int day){
        llTitleDate.setVisibility(View.VISIBLE);
        Calendar calendar=Calendar.getInstance();
        if(year==calendar.get(Calendar.YEAR) &&
                month==calendar.get(Calendar.MONTH) &&
                day==calendar.get(Calendar.DAY_OF_MONTH)){
            tvTitleMonth.setText(mMonthText[month]);
            tvTitleDay.setText(getString(R.string.calendar_today));
        }else {
            if(year == calendar.get(Calendar.YEAR)){
                tvTitleMonth.setText(mMonthText[month]);
            }else {
                tvTitleMonth.setText(String.format("%s%s", String.format(getString(R.string.calendar_year),year),
                        mMonthText[month]));
            }
            tvTitleDay.setText(String.format(getString(R.string.calendar_day), day));
        }
        setCurrentSelectDate(year, month, day);




    }


    private void resetTitleText(String name){
        llTitleDate.setVisibility(View.GONE);

    }
    private void setCurrentSelectDate(int year,int month,int day){
        mCurrentSelectYear=year;
        mCurrentSelectMonth=month;
        mCurrentSelectDay=day;
    }


    private void gotoScheduleFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        Intent intent=this.getIntent();
        classroom=intent.getStringExtra("Classroom");
        Log.d("Intent",classroom);
        if (mScheduleFragment == null) {
            Bundle bundle=new Bundle();
            bundle.putString("classroom",classroom);
            mScheduleFragment = ScheduleFragment.getInstance();
            mScheduleFragment.setArguments(bundle);
            ft.add(R.id.flMainContainer, mScheduleFragment);
        }
        if (mEventSetFragment != null)
            ft.hide(mEventSetFragment);
        ft.show(mScheduleFragment);
        ft.commit();
        llTitleDate.setVisibility(View.VISIBLE);
        //dlMain.closeDrawer(Gravity.START);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return  true;
    }




}
