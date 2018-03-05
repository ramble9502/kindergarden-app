package com.example.monic.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.monic.myapplication.R;
import com.example.monic.myapplication.base.BaseFragment;
import com.example.monic.myapplication.bean.Schedule;
import com.example.monic.myapplication.widget.schedule.CalendarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by monic on 2017/11/10.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int SCHEDULE_TYPE=1;
    private int SCHEDULE_CENTER=2;
    private int SCHEDULE_FINISH_TYPE=3;
    private int SCHEDULE_BOTTOM=4;

    private Context mContext;
    private BaseFragment mBaseFragment;
    private List<Schedule> mSchedules;
    private List<Schedule> mFinishSchedules;

    private boolean mIsShowFinishTask=false;

    public ScheduleAdapter(Context context,BaseFragment baseFragment){
        mContext=context;
        mBaseFragment=baseFragment;
        initData();
    }
    private void initData(){
        mSchedules=new ArrayList<>();
        mFinishSchedules=new ArrayList<>();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(viewType == SCHEDULE_TYPE){
            return new ScheduleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_schedule,parent,false));
        }else if(viewType == SCHEDULE_FINISH_TYPE){
            return new ScheduleFinishViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_schedule_finish,parent,false));
        }else {
            return new ScheduleBottomViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_schedule_bottom,parent,false));
        }
    }

    @Override
    public int getItemCount() {
        return mSchedules.size() + mFinishSchedules.size() + 2;
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position){

    }


    protected class ScheduleViewHolder extends RecyclerView.ViewHolder{

        protected View vScheduleHintBlock;
        protected TextView tvScheduleState;
        protected TextView tvScheduleTitle;
        protected TextView tvScheduleTime;

        public ScheduleViewHolder(View itemView){
            super(itemView);
            vScheduleHintBlock = itemView.findViewById(R.id.vScheduleHintBlock);
            tvScheduleState = (TextView) itemView.findViewById(R.id.tvScheduleState);
            tvScheduleTitle = (TextView) itemView.findViewById(R.id.tvScheduleTitle);
            tvScheduleTime = (TextView) itemView.findViewById(R.id.tvScheduleTime);
        }


    }

    protected class ScheduleFinishViewHolder extends RecyclerView.ViewHolder{
        protected TextView tvScheduleTitle;
        protected TextView tvScheduleTime;

        public ScheduleFinishViewHolder(View itemView) {
            super(itemView);
            tvScheduleTitle = (TextView) itemView.findViewById(R.id.tvScheduleTitle);
            tvScheduleTime = (TextView) itemView.findViewById(R.id.tvScheduleTime);
        }
    }

    protected class ScheduleBottomViewHolder extends RecyclerView.ViewHolder{
        public ScheduleBottomViewHolder(View itemView) {
            super(itemView);
        }
    }
}
