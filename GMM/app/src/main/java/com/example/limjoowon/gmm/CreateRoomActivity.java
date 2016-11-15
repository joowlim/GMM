package com.example.limjoowon.gmm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 새 채팅방을 생성하는 Activity
 * Created by kijong on 2016-11-15.
 */
public class CreateRoomActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
    }

    private void initializeUI() {
        setContentView(R.layout.activity_create_room);
        setTitle("새 채팅방 만들기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
