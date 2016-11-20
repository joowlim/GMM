package com.example.limjoowon.gmm;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by LimJoowon on 2016. 11. 20..
 */
public class TimeAdapter extends BaseAdapter {
    Context mContext;
    int count = (7+1)*(16+1);
    String[] mWeekTitleIds={"월", "화", "수", "목", "금", "토", "일"};
    String[] hours = { "10-11", "11-12", "12-13", "13-14", "14-15", "15-16", "16-17", "17-18",
            "18-19", "19-20", "20-21", "21-22", "22-23", "23-0", "0-1", "1-2"};
    public int[][] timeInfo;


    public TimeAdapter(Context context){
        mContext = context;
        timeInfo = new int[16][7];
    }
    public int[][] getTimeInfo(){
        return timeInfo;
    }
    public void setTimeInfo(int value, int x, int y){
        timeInfo[x][y]=value;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return count;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public View getView(int position, View oldView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v=null;
        if(oldView == null)
        {
            v = new TextView(mContext);
            //view size(40 / 60)
            v.setLayoutParams(new GridView.LayoutParams(168 , 106));
        }
        else if (position == 0){
            v = new TextView(mContext);
            ((TextView)v).setGravity(Gravity.CENTER);
            v.setBackgroundColor(Color.WHITE);
            v.setLayoutParams(new GridView.LayoutParams(168 , 100));
        }
        else if (position % 8 == 0 ){
            v = new TextView(mContext);
            ((TextView)v).setGravity(Gravity.CENTER);
            ((TextView)v).setText(hours[position/8-1]);
            v.setBackgroundColor(Color.WHITE);
            v.setLayoutParams(new GridView.LayoutParams(168 , 106));
        }
        else if (position < 8) {
            v = new TextView(mContext);
            ((TextView)v).setGravity(Gravity.CENTER);
            ((TextView)v).setText(mWeekTitleIds[position-1]);
            v.setBackgroundColor(Color.WHITE);
            v.setLayoutParams(new GridView.LayoutParams(168 , 100));

        }
        else if (position >= 8 && position < count) {
            int indexX = (position-8)/8;
            int indexY = position%8-1;
            if (timeInfo[indexX][indexY]==0) {
                v = new TextView(mContext);
                ((TextView) v).setGravity(Gravity.CENTER);
                v.setBackgroundColor(Color.WHITE);
                v.setLayoutParams(new GridView.LayoutParams(168, 106));
            }
            else{
                v = new TextView(mContext);
                ((TextView) v).setGravity(Gravity.CENTER);
                v.setBackgroundColor(Color.RED);
                v.setLayoutParams(new GridView.LayoutParams(168, 106));

            }
        }
        else {
            v = oldView;
        }

        return v;

    }
}
