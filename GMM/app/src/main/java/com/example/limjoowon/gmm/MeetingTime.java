package com.example.limjoowon.gmm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;

/**
 * Created by LimJoowon on 2016. 11. 15..
 */
public class MeetingTime extends Activity {
    GridView GridSchedule;
    TimeAdapter timeAdapter;
    int[][] fakeTimeTable={{1, 1, 0, 1, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 0}, {1, 1, 1, 0, 1, 1, 0}, {1, 1, 0, 1, 1, 1, 0},
            {1, 1, 0, 0, 1, 1, 0}, {1, 1, 0, 0, 1, 1, 0}, {1, 1, 0, 1, 0, 0, 0}, {1, 1, 0, 0, 1, 1, 0},
            {1, 1, 0, 0, 1, 1, 0}, {1, 0, 1, 0, 1, 1, 0}, {1, 1, 0, 0, 1, 1, 0}, {1, 0, 1, 0, 1, 1, 0},
            {0, 1, 0, 0, 1, 1, 0}, {1, 1, 0, 1, 1, 1, 0}, {1, 1, 0, 0, 1, 1, 0}, {1, 1, 0, 1, 0, 0, 1}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_time);

        findViewById(R.id.remove_time_popup).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        GridSchedule = (GridView)findViewById(R.id.timetable);
        timeAdapter = new TimeAdapter(this);

        for (int i=0; i<7*16; i++){
            timeAdapter.setTimeInfo(fakeTimeTable[i/7][i%7], i/7, i%7);
        }
        GridSchedule.setAdapter(timeAdapter);


    }

    public void personal_time(View view){
        Intent intent = new Intent(MeetingTime.this, myMeetingTime.class);
        startActivity(intent);
    }

}
