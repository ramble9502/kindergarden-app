package com.example.monic.myapplication.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monic.myapplication.R;
import com.example.monic.myapplication.ScheduleMainActivity;
import com.example.monic.myapplication.adapter.ScheduleAdapter;
import com.example.monic.myapplication.base.BaseFragment;
import com.example.monic.myapplication.bean.EventSet;
import com.example.monic.myapplication.bean.Schedule;
import com.example.monic.myapplication.listener.OnCalendarClickListener;
import com.example.monic.myapplication.listener.OnTaskFinishedListener;
import com.example.monic.myapplication.widget.schedule.CalendarUtils;
import com.example.monic.myapplication.widget.schedule.MonthView;
import com.example.monic.myapplication.widget.schedule.ScheduleLayout;
import com.example.monic.myapplication.widget.schedule.ScheduleRecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by monic on 2017/11/14.
 */

public class ScheduleFragment extends BaseFragment implements OnCalendarClickListener,View.OnClickListener
        , OnTaskFinishedListener<List<Schedule>>{

    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    DatabaseReference myRef, puRef;
    RecyclerView recyclerView;

    private ScheduleLayout slSchedule;
    private ScheduleRecyclerView rvScheduleList,rvScheduleList2;
    private EditText etInputContent;
    private RelativeLayout rLNoTask;
    String strtext;
    private String emailuser;
    private int selHour=0;
    private int selMinutes;
    boolean calendartest;


    private ScheduleAdapter mScheduleAdapter;
    private int mCurrentSelectYear,mCurrentSelectMonth,mCurrentSelectDay;
    private long mTime;

    public static ScheduleFragment getInstance(){return new ScheduleFragment();}



    public FirebaseRecyclerAdapter<EventSet,Show_CalendarEvent_Viewholder> mFirebaseEventAdapter;
    public FirebaseRecyclerAdapter<EventSet,Show_CalendarEvent_Viewholder> mFirebaseTaskAdapter;




    @Nullable
    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container){
        strtext = getArguments().getString("classroom");
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference("class_information/"+strtext+"/calendar");
        puRef=firebaseDatabase.getReference("Calendarschool/");
        emailuser=firebaseUser.getEmail().replace("@","").replace(".","");


        return inflater.inflate(R.layout.schedule_fragment_schedule, container, false);

    }


    @Override
    public void onPageChange(int year, int month, int day) {
    }

    @Override
    protected void initData() {
        super.initData();
        initDate();
    }


    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        setCurrentSelectDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        final DatabaseReference classcalender=firebaseDatabase.getReference("class_information/"+strtext+"/calendar/");
        CalendarUtils.getreference(classcalender);
    }




    @Override
    public void onClickDate(int year, int month, int day) {
        setCurrentSelectDate(year, month, day);
        String firebasedate=Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
        resetScheduleList(firebasedate);


    }
    public void resetScheduleList(String calenderday) {
        final DatabaseReference calendatreadRef=firebaseDatabase.getReference("class_information/"+strtext+"/calendar/"+calenderday);
        mFirebaseEventAdapter=new FirebaseRecyclerAdapter<EventSet, Show_CalendarEvent_Viewholder>(EventSet.class,R.layout.item_schedule,Show_CalendarEvent_Viewholder.class,calendatreadRef) {
                @Override
            protected void populateViewHolder(final Show_CalendarEvent_Viewholder viewHolder, EventSet model, int position) {
                rLNoTask.setVisibility(View.GONE);
                viewHolder.Schedule_Title(model.getName());
                viewHolder.Schedule_Date(model.getTime());
            }

        };
        rvScheduleList.setAdapter(mFirebaseEventAdapter);
    }






    private void setCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
        if (mActivity instanceof ScheduleMainActivity) {
            ((ScheduleMainActivity) mActivity).resetMainTitleDate(year, month, day);
        }
    }

    @Override
    protected void bindView() {
        slSchedule = searchViewById(R.id.slSchedule);
        etInputContent = searchViewById(R.id.etInputContent);
        rLNoTask = searchViewById(R.id.rlNoTask);
        slSchedule.setOnCalendarClickListener(this);
        searchViewById(R.id.ibMainOk).setOnClickListener(this);
        searchViewById(R.id.ibMainClock).setOnClickListener(this);
        initScheduleList();
        initBottomInputBar();
    }
    private void initScheduleList() {
        rvScheduleList = slSchedule.getSchedulerRecyclerView();
        rvScheduleList2=slSchedule.getSchedulerRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvScheduleList.setLayoutManager(manager);
        rvScheduleList2.setLayoutManager(manager);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        rvScheduleList.setItemAnimator(itemAnimator);
        //rvScheduleList2.setItemAnimator(itemAnimator);
        mScheduleAdapter = new ScheduleAdapter(mActivity, this);
        rvScheduleList.setAdapter(mScheduleAdapter);
        //rvScheduleList2.setAdapter(mScheduleAdapter);
    }

    private void initBottomInputBar() {
        etInputContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etInputContent.setGravity(s.length() == 0 ? Gravity.CENTER : Gravity.CENTER_VERTICAL);
            }
        });
        etInputContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibMainOk:
                addSchedule();
                break;
            case R.id.ibMainClock:
                addClock();
                break;
        }
    }


    private void addClock(){
        LinearTimePickerDialog.Builder.with(getActivity())
                .setButtonCallback(new LinearTimePickerDialog.ButtonCallback() {
                    @Override
                    public void onPositive(DialogInterface dialog, int hour, int minutes) {
                        selHour=hour;
                        selMinutes=minutes;
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {

                    }
                }).build()
                .show();
    }

    private void addSchedule(){
        final String content=etInputContent.getText().toString();
        setCurrentSelectDate(mCurrentSelectYear, mCurrentSelectMonth,mCurrentSelectDay);
        final String datecalendar=String.valueOf(mCurrentSelectYear)+"-"+String.valueOf(mCurrentSelectMonth)+"-"+
                String.valueOf(mCurrentSelectDay);
        if(selHour==0){
            Toast.makeText(mActivity,"請選擇時間(分秒)",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(content)){
            Toast.makeText(mActivity,"內容不可為空",Toast.LENGTH_SHORT).show();
        }
        else {
            String minute=String.format("00",selMinutes);
            String time=String.valueOf(selHour)+":"+minute;

            closeSoftInput(mActivity,etInputContent);
            EventSet eventSet = new EventSet(datecalendar,content,emailuser,time);
            myRef.child(datecalendar).push().setValue(eventSet);
            Toast.makeText(mActivity,"資料已經上傳...",Toast.LENGTH_SHORT).show();
            etInputContent.setText("");
            rLNoTask.setVisibility(View.GONE);
        }
    }
    private void  closeSoftInput(Context context, EditText editText){
        //InputMethodManager对象后就可以通过调用其成员方法来对软键盘进行操作
        etInputContent.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }



    @Override
    public void onTaskFinished(List<Schedule> data) {
        rLNoTask.setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);
    }
    private void updateTaskHintUi(int size) {
        if (size == 0) {
            slSchedule.removeTaskHint(mCurrentSelectDay);
        } else {
            slSchedule.addTaskHint(mCurrentSelectDay);
        }
    }


    public int getCurrentCalendarPosition() {
        return slSchedule.getMonthCalendar().getCurrentItem();
    }



    public static class Show_CalendarEvent_Viewholder extends RecyclerView.ViewHolder{
        private final TextView tvScheduleTitle,tvScheduleTime,tvScheduleState;
        private LinearLayout.LayoutParams params;
        private LinearLayout layout;

        public Show_CalendarEvent_Viewholder(final View itemView){
            super(itemView);
            tvScheduleTitle=(TextView)itemView.findViewById(R.id.tvScheduleTitle);
            tvScheduleTime=(TextView)itemView.findViewById(R.id.tvScheduleTime);
            tvScheduleState=(TextView)itemView.findViewById(R.id.tvScheduleState);
            layout=(LinearLayout)itemView.findViewById(R.id.itemschedule);
            params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        private void Schedule_Title(String title){tvScheduleTitle.setText(title);}
        private void Schedule_Date(String time){tvScheduleTime.setText(time);}
        private void Layout_hide(){
            params.height=0;
            layout.setLayoutParams(params);
        }
    }














}
