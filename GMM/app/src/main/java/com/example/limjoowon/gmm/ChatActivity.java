package com.example.limjoowon.gmm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.limjoowon.gmm.config.MsgServerConfig;
import com.example.limjoowon.gmm.module.GMMMessenger;

/**
 * 채팅방 Activity
 * Created by LimJoowon on 2016. 11. 3..
 */
public class ChatActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mSendBtn;

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
        mEditText = (EditText) findViewById(R.id.msg_edit_text);
        mSendBtn = (Button) findViewById(R.id.msg_send_btn);

        mSendBtn.setOnClickListener(onSendBtnClick);
    }

    /**
     * 전송 버튼을 눌렀을 때
     */
    private View.OnClickListener onSendBtnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            sendTextMessage(mEditText.getText().toString());
        }
    };

    /**
     * 텍스트 메시지 전송
     */
    private void sendTextMessage(String msg) {
        // 빈 메세지면 전송하지 않음
        if (msg==null || msg.isEmpty()) return;
        GMMMessenger messenger = new GMMMessenger();
        messenger.sendTextMessage(MsgServerConfig.CHAT_ROOM_ID, msg);
    }
}
