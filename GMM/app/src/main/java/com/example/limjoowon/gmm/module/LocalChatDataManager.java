package com.example.limjoowon.gmm.module;

import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.example.limjoowon.gmm.GMMApplication;
import com.example.limjoowon.gmm.config.MsgServerConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;

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

    private static String KEY_ROOM_NAME = "room_name";

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
    public boolean saveNewMessage(String chatRoomId, String sender, String googleId, String name, String profile, String msg, long time, long time_get) {
        //TODO: 지금은 하드코딩. 추후에 실제 chatRoomId로 변경
        chatRoomId = MsgServerConfig.CHAT_ROOM_ID;

        try {
            SharedPreferences pref = GMMApplication.getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);
            String str = pref.getString(chatRoomId, "");
            JSONArray array = new JSONArray();
            if (!str.isEmpty()) {
                array = new JSONArray(str);
            }

            JSONObject obj = new JSONObject();
            obj.put(MsgServerConfig.KEY_MSG, msg);
            obj.put(MsgServerConfig.KEY_SENDER, sender);
            obj.put(MsgServerConfig.KEY_SENDER_GOOGLE, googleId);
            obj.put(MsgServerConfig.KEY_SENDER_NAME, name);
            obj.put(MsgServerConfig.KEY_SENDER_PROFILE_URI, profile);
            obj.put(MsgServerConfig.KEY_MSG_TIME, time);
            obj.put(MsgServerConfig.KEY_MSG_GET_TIME, time_get);
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
     * 채팅방 Clear
     */
    public void clearChat() {
        SharedPreferences pref = GMMApplication.getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(MsgServerConfig.CHAT_ROOM_ID,"");
        editor.commit();
    }

    /**
     * 해당 채팅방의 모든 메시지를 얻어온다.
     * @param chatRoomId 채팅방 ID
     * @return JSONArray 형태이다. 없을 경우 빈 Array를 반환한다.
     */
    public JSONArray getAllMessage(String chatRoomId) {
        //TODO: 지금은 하드코딩. 추후에 실제 chatRoomId로 변경
        chatRoomId = MsgServerConfig.CHAT_ROOM_ID;

        SharedPreferences pref = GMMApplication.getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);
        String str = pref.getString(chatRoomId, "");
        JSONArray array = new JSONArray();

        try {
            if (!str.isEmpty()) {
                array = new JSONArray(str);

                JSONObject [] newarray  = new JSONObject[array.length()];
                int length = newarray.length;
                for(int i = 0; i<length; i++) {
                    newarray[i] = (JSONObject)array.get(i);
                }
                for(int i = 0; i<length-1; i++) {
                    int index = 0;
                    long min = -1;
                    for(int j=i; j<length; j++) {
                        long time = newarray[j].getLong(MsgServerConfig.KEY_MSG_TIME);
                        if (min == -1 || time < min) {
                            min = time;
                            index = j;
                        }
                    }
                    if ( i != index ) {
                        JSONObject temp = newarray[index];
                        newarray[index] = newarray[i];
                        newarray[i] = temp;
                    }
                }
                array = new JSONArray();
                for(int i=0;i<length;i++) {
                    array.put(newarray[i]);
                }
                printLog(array);
            }
        } catch(Exception e) {
        } finally {
            return array;
        }
    }

    public void printLog(JSONArray array) {
        String dirPath = Environment.getExternalStorageDirectory().getPath() + "/GMM";
        File dir = new File(dirPath);
        boolean result;
        if (!dir.exists()) {
            result = dir.mkdirs();
        }

        String path = dirPath + "/log.txt";

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(path));
            for(int i = 0; i < array.length(); i++) {
                String s = "";
                JSONObject obj = (JSONObject)array.get(i);
                s = String.format("msg,%s,received_time,%s",
                        obj.getString(MsgServerConfig.KEY_MSG),
                        Long.toString(obj.getLong(MsgServerConfig.KEY_MSG_GET_TIME)));
                Log.i("[ClientMsg]",s);
                out.write(s);
                out.newLine();
            }
            out.close();
        } catch (Exception e) {
            int debug;
            debug = 1;
        }
    }

    public String getRoomName() {
        String roomName = "";
        SharedPreferences pref = GMMApplication.getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);
        String str = pref.getString(KEY_ROOM_NAME, "");
        return str;
    }

    public void setRoomName(String roomName) {
        if(roomName == null || roomName == "") return;
        SharedPreferences pref = GMMApplication.getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_ROOM_NAME,roomName);
        editor.commit();

        clearChat();
    }
}
