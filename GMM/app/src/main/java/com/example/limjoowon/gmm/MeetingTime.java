package com.example.limjoowon.gmm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;

import com.example.limjoowon.gmm.module.TimeManager;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by LimJoowon on 2016. 11. 15..
 */
public class MeetingTime extends Activity {
    GridView GridSchedule;
    TimeAdapter timeAdapter;
    int number_participant;
    int[][] fakeTimeTable;

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

        TimeManager.getMixedTime("abcd", onGetMixedTimeResponse);

    }
    @Override
    protected void onStart(){
        super.onStart();

        TimeManager.getMixedTime("abcd", onGetMixedTimeResponse);

    }

    //개인 설정 창으로 이동
    public void personal_time(View view){
        Intent intent = new Intent(MeetingTime.this, myMeetingTime.class);
        startActivity(intent);
    }

    private Callback onGetMixedTimeResponse = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            int debug;
            debug = 1;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String result = response.body().string();
                JSONObject object = new JSONObject(result);
                final String sessionId = object.getString("session_id");
                final String number = object.getString("num_participant");
                final String time_info = object.getString("time");

                number_participant=Integer.parseInt(number);
                fakeTimeTable = changeAllTimeInfo(time_info);

                if (sessionId == null) {
                } else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeAdapter = new TimeAdapter(MeetingTime.this, number_participant);

                            for (int i=0; i<7*16; i++){
                                timeAdapter.setTimeInfo(fakeTimeTable[i/7][i%7], i/7, i%7);
                            }
                            GridSchedule.setAdapter(timeAdapter);
                        }
                    });
            } catch(Exception e) {
            }
        }
    };
    public int[][] changeAllTimeInfo(String time_info){
        int index=0;
        int[][] result = new int[16][7];
        for (int i=0;i<16;i++){
            for (int j=0;j<7;j++){
                result[i][j]=Integer.parseInt(time_info.substring(index, index+1));
                index++;
            }
        }
        return result;
    }
}
