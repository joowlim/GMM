package com.example.limjoowon.gmm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

/**
 * Created by LimJoowon on 2016. 11. 20..
 */
public class myMeetingTime extends Activity{
    GridView GridSchedule;
    TimeAdapter timeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_time);

        findViewById(R.id.my_remove_time_popup).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        GridSchedule = (GridView) findViewById(R.id.my_timetable);
        timeAdapter = new TimeAdapter(this);
        GridSchedule.setAdapter(timeAdapter);
        GridSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int indexX = (position - 8) / 8;
                int indexY = position % 8 - 1;
                if (timeAdapter.getTimeInfo()[indexX][indexY] == 1){
                    timeAdapter.setTimeInfo(0, indexX, indexY);
                }
                else{
                    timeAdapter.setTimeInfo(1, indexX, indexY);
                }
                timeAdapter.notifyDataSetChanged();
            }
        });


    }

}

