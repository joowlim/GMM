package com.example.limjoowon.gmm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.limjoowon.gmm.config.MsgServerConfig;
import com.example.limjoowon.gmm.config.UserConfig;
import com.example.limjoowon.gmm.module.GMMMessenger;
import com.example.limjoowon.gmm.module.LocalChatDataManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 채팅방 Activity
 * Created by LimJoowon on 2016. 11. 3..
 */
public class ChatActivity extends AppCompatActivity {

    public static final String mBroadcastStringAction = "com.example.limjoowon.gmm.broadcast.string";
    private EditText mEditText;
    private Button mSendBtn;
    private MsgListAdapter mAdapter;
    private ListView mListView;
    private IntentFilter mIntentFilter;
    private ArrayList<MsgItemData> mMsgList;

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
        scrollMyListViewToBottom();
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
        String title = LocalChatDataManager.getInstance().getRoomName();
        if (title == null || title.isEmpty()) {
            title = "채팅방";
        }
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEditText = (EditText) findViewById(R.id.msg_edit_text);
        mSendBtn = (Button) findViewById(R.id.msg_send_btn);
        mListView = (ListView) findViewById(R.id.msg_list);
        mSendBtn.setOnClickListener(onSendBtnClick);

        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                scrollMyListViewToBottom();
                            }
                        }, 300);
                return false;
            }
        });

        generateInitialListView();
    }

    /**
     * ListView 초기화
     */
    private void generateInitialListView() {
        try {
            ArrayList<MsgItemData> msgList = new ArrayList<MsgItemData>();
            JSONArray array = LocalChatDataManager.getInstance().getAllMessage("");

            for(int i = 0; i<array.length(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                String msg = obj.getString(MsgServerConfig.KEY_MSG);
                String senderName = obj.getString(MsgServerConfig.KEY_SENDER_NAME);
                String profileUri = obj.getString(MsgServerConfig.KEY_SENDER_PROFILE_URI);
                String senderId = obj.getString(MsgServerConfig.KEY_SENDER_GOOGLE);
                String senderToken = obj.getString(MsgServerConfig.KEY_SENDER);

                msgList.add(getMsgItemData(msg,senderName, senderId, senderToken, profileUri));
            }
            mAdapter = new MsgListAdapter(ChatActivity.this, msgList);
            mMsgList = msgList;
            mListView.setAdapter(mAdapter);
            scrollMyListViewToBottom();
        } catch(Exception e) {
            return;
        }
    }

    private MsgItemData getMsgItemData(String msg, String name, String id, String token, String profile) {
        try {
            boolean fromMe = false;
            if (token.equals(GMMApplication.getToken()) ||
                    token.equals("me") || id.equals(UserConfig.getInstance().getUserId())) {
                fromMe = true;

                if (msg.startsWith("**newChat**")) {
                    msg = "새 채팅방을 생성하였습니다.";
                }

                if (msg.equals("상대방테스트") || msg.equals("상대방 테스트 12345")) {
                    fromMe = false;
                }
            }
            return new MsgItemData(msg, 1, id, name, profile,fromMe);
        } catch(Exception e) {
            return null;
        }
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

        UserConfig user = UserConfig.getInstance();
        // 로컬에 새메시지 저장
        LocalChatDataManager.getInstance().saveNewMessage(
                "", GMMApplication.getToken(), user.getUserId(), user.getUserName(), user.getProfilePicUri(), msg);

        // 리스트뷰에 새 메시지 추가

        mMsgList.add(getMsgItemData(msg, user.getUserName(), user.getUserId(), "me", user.getProfilePicUri()));
        mAdapter.setItems(mMsgList);
        mAdapter.notifyDataSetChanged();
        scrollMyListViewToBottom();

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
            String senderToken = intent.getStringExtra(MsgServerConfig.KEY_SENDER);
            String senderId = intent.getStringExtra(MsgServerConfig.KEY_SENDER_GOOGLE);
            String senderName = intent.getStringExtra(MsgServerConfig.KEY_SENDER_NAME);
            String profileUri = intent.getStringExtra(MsgServerConfig.KEY_SENDER_PROFILE_URI);

            //ListView 업데이트
            final MsgItemData newMsg = getMsgItemData(msg, senderName, senderId, senderToken, profileUri);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMsgList.add(newMsg);
                    mAdapter.setItems(mMsgList);
                    mAdapter.notifyDataSetChanged();
                    scrollMyListViewToBottom();
                }
            });
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 메뉴의 항목을 선택(클릭)했을 때 호출되는 콜백메서드
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        Intent intent;

        switch(id) {
            case R.id.show_time:
                intent = new Intent(ChatActivity.this, MeetingTime.class);
                startActivity(intent);
                return true;
            case R.id.show_place:
                intent = new Intent(ChatActivity.this, location.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void scrollMyListViewToBottom() {
        if (mListView == null || mAdapter == null) return;
        mListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });
    }
}
