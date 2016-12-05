package com.example.limjoowon.gmm;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.limjoowon.gmm.config.MsgServerConfig;
import com.example.limjoowon.gmm.module.GMMServerCommunicator;
import com.example.limjoowon.gmm.module.LocalChatDataManager;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity{
    private ListView m_ListView;
    private ChatroomListAdapter m_Adapter;
    private GoogleApiClient mGoogleApiClient;
    private FloatingActionButton mNewChatBtn;
    private static int GO_SETTING = 123;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 사용자 정보를 서버에 등록.
        registerUserToServer();

        // UI 초기화
        initializeUI();

        // Android에서 제공하는 string 문자열 하나를 출력 가능한 layout으로 어댑터 생성
        m_Adapter = new ChatroomListAdapter() ;

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) findViewById(R.id.chatroomlist);

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);

        // ListView 아이템 터치 시 이벤트 추가
        m_ListView.setOnItemClickListener(onClickListItem);

        // ListView에 아이템 추가
        res = getResources();
        Drawable image = res.getDrawable(R.mipmap.ceaser);
        String chatName = LocalChatDataManager.getInstance().getRoomName();
        String lastMessage = ":)";
        try {
            JSONArray array = LocalChatDataManager.getInstance().getAllMessage("");
            JSONObject obj = (JSONObject) array.get(array.length()-1);
            lastMessage = obj.getString(MsgServerConfig.KEY_MSG);
        }catch (Exception e){
        }
        if (chatName.isEmpty()) {
            chatName = "채팅방";
        }
        m_Adapter.addItem(image, chatName, lastMessage);
    }
    @Override
    protected void onResume() {
        super.onResume();
        m_Adapter.removeItem(0);

        Drawable image = res.getDrawable(R.mipmap.ceaser);
        String chatName = LocalChatDataManager.getInstance().getRoomName();
        String lastMessage = ":)";
        try {
            JSONArray array = LocalChatDataManager.getInstance().getAllMessage("");
            if (array == null || array.length() == 0) {
                lastMessage = "아래의 추가 버튼을 클릭하여 새 채팅방을 만들어주세요.";
            } else {
                JSONObject obj = (JSONObject) array.get(array.length() - 1);
                lastMessage = obj.getString(MsgServerConfig.KEY_MSG);
            }
        }catch (Exception e){
        }
        if (chatName.isEmpty()) {
            chatName = " ";
        }
        m_Adapter.addItem(image, chatName, lastMessage);
        m_Adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch( item.getItemId() ) {
            case R.id.setting_btn:
                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(i,GO_SETTING);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GO_SETTING && resultCode == RESULT_OK) {
            Bundle extraBundle = data.getExtras();
            boolean isLogout = extraBundle.getBoolean("isLogout",false);
            if (isLogout) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }
    }


    // 아이템 터치 이벤트
    public AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
        }
    };


    /**
     * UI를 초기화 한다.
     */
    private void initializeUI() {
        mNewChatBtn = (FloatingActionButton) findViewById(R.id.floating_btn);
        setTitle("조모임메신저");
        // 새채팅방생성으로 이동
        mNewChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateRoomActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 사용자 정보를 서버에 등록한다.
     */
    private void registerUserToServer() {
        GMMServerCommunicator communicator = new GMMServerCommunicator();
        communicator.registerUserToServer(null);
    }
}
