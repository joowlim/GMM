package com.example.limjoowon.gmm;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.limjoowon.gmm.widget.MeetLocationItemAdapter;
import com.example.limjoowon.gmm.widget.MeetLocationItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * 조모임 장소 추천 목록 Activity
 * Created by kijong on 2016-12-05.
 */

public class MeetLocationListActivity extends AppCompatActivity {

    private TextView mTimeText;
    private ListView mListView;
    private MeetLocationItemAdapter mAdapter;
    private List<MeetLocationItemData> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_location);
        initUI();
    }

    private void initUI() {
        setTitle("조모임 장소 추천");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTimeText = (TextView) findViewById(R.id.time_for_meet_text);
        mListView = (ListView) findViewById(R.id.meet_location_list);

        mDataList = new ArrayList<MeetLocationItemData>();
        MeetLocationItemData item1, item2;
        item1 = new MeetLocationItemData("김상열빌딩 1130호","N1");
        item2 = new MeetLocationItemData("정보전자동 제4강의실", "E3");
        mDataList.add(item1);
        mDataList.add(item2);

        mAdapter = new MeetLocationItemAdapter(this, mDataList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
