package com.example.limjoowon.gmm.module;

import android.content.SharedPreferences;

import com.example.limjoowon.gmm.GMMApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * 채팅 메시지 데이터들을 로컬에 저장/불러오기를 관리하는 클래스
 * SQLlite를 활용하는게 맞겠으니 일단 지금은 귀찮으니 SharedPreference로 한다.
 * TODO : 추후에 문제 생기면 인터페이스는 유지하고 내부는 SQLLite로 변경
 * Created by kijong on 2016-11-07.
 */
public class LocalChatDataManager {

    /**
     * SharedPreference 이름 및 Key 값들
     */
    private static String SP_NAME = "gmm_sp_message";
    public static String KEY_SENDER = "sender_id";
    public static String KEY_MSG = "msg";

    /**
     * Singleton 객체
     */
    private volatile static LocalChatDataManager instance;

    private LocalChatDataManager(){}
    /**
     * Singleton 객체 생성
     */
    public static LocalChatDataManager getInstance() {
        if(instance == null) {
            synchronized( LocalChatDataManager.class ) {
                if ( instance == null )
                    instance = new LocalChatDataManager();
            }
        }
        return instance;
    }

    /**
     * 새로운 메시지를 업데이트 해서 저장한다. (메시지들은 json array로 관리됨!)
     * @param chatRoomId 채팅방 ID
     * @param sender Sender 내가 보낸 것은 'me' 로 하거나 내 Token 이면 알아서 me로 판단.
     * @param msg 메시지
     * @return 실패하면 false 반환
     */
    public boolean saveNewMessage(String chatRoomId, String sender, String msg) {
        //TODO: 지금은 하드코딩. 추후에 실제 chatRoomId로 변경
        chatRoomId = "CHAT_ROOM_ID";

        try {
            SharedPreferences pref = GMMApplication.getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);
            String str = pref.getString(chatRoomId, "");
            JSONArray array = new JSONArray();
            if (!str.isEmpty()) {
                array = new JSONArray(str);
            }

            JSONObject obj = new JSONObject();
            obj.put(KEY_MSG, msg);
            obj.put(KEY_SENDER, sender);
            array.put(obj);

            String newStr = array.toString();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(chatRoomId, newStr);
            editor.commit();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 해당 채팅방의 모든 메시지를 얻어온다.
     * @param chatRoomId 채팅방 ID
     * @return JSONArray 형태이다. 없을 경우 빈 Array를 반환한다.
     */
    public JSONArray getAllMessage(String chatRoomId) {
        //TODO: 지금은 하드코딩. 추후에 실제 chatRoomId로 변경
        chatRoomId = "CHAT_ROOM_ID";

        SharedPreferences pref = GMMApplication.getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);
        String str = pref.getString(chatRoomId, "");
        JSONArray array = new JSONArray();

        try {
            if (!str.isEmpty()) {
                array = new JSONArray(str);
            }
        } catch(Exception e) {
        } finally {
            return array;
        }
    }
}
