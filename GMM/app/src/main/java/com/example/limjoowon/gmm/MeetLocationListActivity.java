package com.example.limjoowon.gmm;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.limjoowon.gmm.module.TimeManager;
import com.example.limjoowon.gmm.widget.MeetLocationItemAdapter;
import com.example.limjoowon.gmm.widget.MeetLocationItemData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 조모임 장소 추천 목록 Activity
 * Created by kijong on 2016-12-05.
 */

public class MeetLocationListActivity extends AppCompatActivity {

    private TextView mTimeText;
    private ListView mListView;
    private MeetLocationItemAdapter mAdapter;
    private List<MeetLocationItemData> mDataList;
    private String mTime = "21061205-1900-2100";
    private String mBuilding = "N1";
    private String mUri = "http://143.248.49.55:8080/reserve_info/{building}/{date}";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_location);
        showProgressDialog();
        getBaseInfo();
        initUI();
    }

    /**
     * Intent를 통해서 기본적인 정보를 받아옴
     */
    private void getBaseInfo() {
        mBuilding = getIntent().getExtras().getString("LocationOfBuilding");
        mUri = mUri.replace("{building}", mBuilding);
        getTimeInfo();
    }

    private void initUI() {
        setTitle("조모임 장소 추천");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTimeText = (TextView) findViewById(R.id.time_for_meet_text);
        mListView = (ListView) findViewById(R.id.meet_location_list);

        mDataList = new ArrayList<MeetLocationItemData>();

        mAdapter = new MeetLocationItemAdapter(this, mDataList);
        mListView.setAdapter(mAdapter);
    }

    private void getTimeInfo() {
        TimeManager.getMixedTime("abcd", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    JSONObject object = new JSONObject(result);
                    String time_info = object.getString("time");
                    processTime(time_info);
                } catch (Exception e) {
                }
            }
        });
    }

    private void processTime(String time_info) {
        String timeText;
        Boolean isFound = false;
        int datePlus = 0, timePlus = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 15; j++) {
                int first = j * 7 + i;
                int second = (j + 1) * 7 + i;
                if (time_info.charAt(first) == '3' &&
                        time_info.charAt(second) == '3') {
                    datePlus = i;
                    timePlus = j;
                    isFound = true;
                    break;
                }
            }
            if (isFound) break;
        }
        String finalStr;
        String dayStr;

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
        String strCurDay = CurDayFormat.format(date);

        if (isFound) {
            int day = Integer.parseInt(strCurDay) + datePlus;
            int timeSt = 10 + timePlus;
            int timeEnd = 10 + timePlus + 2;
            if (timeEnd > 24) {
                timeEnd -= 24;
            }

            dayStr = (day < 10) ? "0" + Integer.toString(day) : Integer.toString(day);
            String timeStStr = Integer.toString(timeSt) + "00";
            String timeEndStr = Integer.toString(timeEnd) + "00";

            finalStr = "201612" + dayStr + "-" + timeStStr + "-" + timeEndStr;
            timeText = "2106-12-" + dayStr + " " + Integer.toString(timeSt) + ":00 ~ " + Integer.toString(timeEnd) + ":00";
        } else {
            finalStr = "201612" + strCurDay + "-2000-2200";
            timeText = "2016-12-" + strCurDay + " 20:00 ~ 22:00";
        }
        mUri = mUri.replace("{date}", finalStr);

        final String timeTextToSet = timeText;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTimeText.setText(timeTextToSet);
            }
        });

        getLocationList();
    }

    private void getLocationList() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(mUri).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    JSONArray ary = new JSONArray(result);
                    int LAST = ary.length();
                    if (LAST > 4) {
                        LAST = 4;
                    }
                    for (int i =0; i <LAST; i++)  {
                        JSONObject obj = (JSONObject)ary.get(i);
                        String name = obj.getString("name");
                        mDataList.add(new MeetLocationItemData(name, mBuilding));
                    }

                } catch(Exception e) {
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setItems(mDataList);
                            mAdapter.notifyDataSetChanged();
                            hideProgressDialog();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("추천 장소 정보를 가져오는 중입니다");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

}
