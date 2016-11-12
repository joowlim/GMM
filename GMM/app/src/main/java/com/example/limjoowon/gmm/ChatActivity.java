package com.example.limjoowon.gmm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.limjoowon.gmm.config.MsgServerConfig;
import com.example.limjoowon.gmm.module.GMMMessenger;
import com.example.limjoowon.gmm.module.LocalChatDataManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 채팅방 Activity
 * Created by LimJoowon on 2016. 11. 3..
 */
public class ChatActivity extends AppCompatActivity {

    public static final String mBroadcastStringAction = "com.example.limjoowon.gmm.broadcast.string";
    private EditText mEditText;
    private Button mSendBtn;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastStringAction);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GMMApplication.setChatRoomInForeground(true);
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        GMMApplication.setChatRoomInForeground(false);
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    /**
     * 채팅방 Activity의 UI를 초기화 한다.
     */
    private void initUI() {
        setTitle("채팅방1");
        mEditText = (EditText) findViewById(R.id.msg_edit_text);
        mSendBtn = (Button) findViewById(R.id.msg_send_btn);
        mListView = (ListView) findViewById(R.id.msg_list);
        mSendBtn.setOnClickListener(onSendBtnClick);

        generateInitialListView();
    }

    /**
     * ListView 초기화
     */
    private void generateInitialListView() {
        try {
            ArrayList<String> msgList = new ArrayList<String>();
            JSONArray array = LocalChatDataManager.getInstance().getAllMessage("");

            for(int i = 0; i<array.length(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                String msg = generateMsg(
                        obj.getString(LocalChatDataManager.KEY_SENDER),
                        obj.getString(LocalChatDataManager.KEY_MSG));
                msgList.add(msg);
            }

            mAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.chat_list_item, R.id.chat_list_itm_txt, msgList);
            mListView.setAdapter(mAdapter);
        } catch(Exception e) {
            return;
        }
    }

    /**
     * 임시 메소드
     */
    private String generateMsg(String sender, String msg) {
        if (sender.equals(GMMApplication.getToken()) ||
                sender.equals("me")) {
            sender = "나";
        } else {
            sender = "사용자" + sender.substring(20,23).toLowerCase();
        }
        return "[" + sender + "]  :  " + msg;
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

        mEditText.setText("");

        // 로컬에 새메시지 저장
        LocalChatDataManager.getInstance().saveNewMessage("", GMMApplication.getToken(), msg);

        // 리스트뷰에 새 메시지 추가
        mAdapter.add(generateMsg("me", msg));
        mAdapter.notifyDataSetChanged();

        // 서버로 메시지 전송
        GMMMessenger messenger = new GMMMessenger();
        messenger.sendTextMessage(MsgServerConfig.CHAT_ROOM_ID, msg);
    }

    /**
     * FirebaseMessage Service로 부터 메시지를 받는 BroadCastReceiver
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(MsgServerConfig.KEY_MSG);
            String senderId = intent.getStringExtra(MsgServerConfig.KEY_SENDER);

            //ListView 업데이트
            final String newMsg = generateMsg(senderId, msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.add(newMsg);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}
