package com.example.limjoowon.gmm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.limjoowon.gmm.module.TimeManager;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by LimJoowon on 2016. 11. 20..
 */
public class myMeetingTime extends Activity{
    GridView GridSchedule;
    TimeAdapter timeAdapter;
    int[][] timeTable=new int[16][7];

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
        findViewById(R.id.save_time_info).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //timeTable=timeAdapter.getTimeInfo();
                        String time_info = timeInfoToString(timeTable);
                        TimeManager.sendTimeInfo("abcd", "3", time_info, onSendTimeResponse);
                    }
                }
        );

        GridSchedule = (GridView) findViewById(R.id.my_timetable);
        timeAdapter = new TimeAdapter(this, 1);

        for (int i=0; i<7*16; i++){
            timeAdapter.setTimeInfo(timeTable[i/7][i%7], i/7, i%7);
        }
        GridSchedule.setAdapter(timeAdapter);

        GridSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int indexX = (position - 8) / 8;
                int indexY = position % 8 - 1;
                if (timeAdapter.getTimeInfo()[indexX][indexY] == 1){
                    timeTable[indexX][indexY] = 0;
                    timeAdapter.setTimeInfo(0, indexX, indexY);

                }
                else{
                    timeTable[indexX][indexY] = 1;
                    timeAdapter.setTimeInfo(1, indexX, indexY);
                }
                timeAdapter.notifyDataSetChanged();
            }
        });


    }
    private Callback onSendTimeResponse = new Callback() {
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
                final String success = object.getString("success");


                if (success == "false") {
                } else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(myMeetingTime.this, "내 시간을 업데이트 하였습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
            } catch(Exception e) {
            }
        }
    };
    public String timeInfoToString(int[][] timeInfo){
        String result="";
        for (int i=0;i<16;i++){
            for (int j=0;j<7;j++){
                result=result+timeInfo[i][j];
            }
        }
        return result;
    }

}

