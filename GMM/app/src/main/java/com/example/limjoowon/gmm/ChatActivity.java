package com.example.limjoowon.gmm;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by LimJoowon on 2016. 11. 3..
 */
public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initUI();
    }

    /**
     * 채팅방 Activity의 UI를 초기화 한다.
     */
    private void initUI() {
        setTitle("채팅방1");
    }
}
