package com.example.limjoowon.gmm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limjoowon.gmm.module.GMMServerCommunicator;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 새 채팅방을 생성하는 Activity
 * Created by kijong on 2016-11-15.
 */
public class CreateRoomActivity extends AppCompatActivity {

    private Button mSearchUserBtn;
    private Button mCreateRoomDoneBtn;
    private EditText mUserIdEdit;
    private int mTotalUser = 0;
    private TextView mUser1,mUser2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
    }

    //UI 초기화
    private void initializeUI() {
        setContentView(R.layout.activity_create_room);
        setTitle("새 채팅방 만들기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchUserBtn = (Button) findViewById(R.id.btn_search_user);
        mSearchUserBtn.setOnClickListener(onSearchUserClick);
        mCreateRoomDoneBtn = (Button) findViewById(R.id.btn_create_room_done);
        mCreateRoomDoneBtn.setOnClickListener(onDoneBtnClick);

        mUserIdEdit = (EditText) findViewById(R.id.user_id_edit);
        mUser1 = (TextView) findViewById(R.id.user1);
        mUser2 = (TextView) findViewById(R.id.user2);
    }

    // 사용자 검색 버튼이 눌리었을 때 사용자 검색 API 요청을 한다.
    private View.OnClickListener onSearchUserClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userId = mUserIdEdit.getText().toString();
            if( userId == null || userId.isEmpty()) return;
            GMMServerCommunicator communicator = new GMMServerCommunicator();
            communicator.searchUser(userId, onSearchUserResponse);
        }
    };

    // 완료 버튼이 눌리었을 때 채팅방으로 이동
    private View.OnClickListener onDoneBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CreateRoomActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();
        }
    };

    // 사용자 검색 API 요청에 대한 결과
    private Callback onSearchUserResponse = new Callback() {
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
                final String userId = object.getString("user_id");
                final String userName = object.getString("name");
                if (userId == null || userId.isEmpty())
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CreateRoomActivity.this, "해당 아이디를 가진 사용자는 없습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTotalUser++;
                            String userTxt = userId + "(" + userName + ")";
                            Toast.makeText(CreateRoomActivity.this, "사용자 " + userTxt + "님을 추가하셨습니다.", Toast.LENGTH_LONG).show();
                            TextView userTextView = getCurrUserText();
                            userTextView.setText(userTxt);
                            userTextView.setVisibility(View.VISIBLE);
                            mUserIdEdit.setText("");
                        }
                    });
            } catch(Exception e) {
            }
        }
    };

    private TextView getCurrUserText() {
        if (mTotalUser == 1)
            return mUser1;
        if (mTotalUser == 2)
            return mUser2;
        return null;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
