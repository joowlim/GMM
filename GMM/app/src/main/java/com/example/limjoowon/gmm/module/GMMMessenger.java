package com.example.limjoowon.gmm.module;

import com.example.limjoowon.gmm.GMMApplication;
import com.example.limjoowon.gmm.config.MsgServerConfig;
import com.example.limjoowon.gmm.config.UserConfig;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 실질적으로 메시지를 보내는 일을 담당하는 클래스
 * Created by kijong on 2016-11-07.
 */
public class GMMMessenger {

    /**
     * 텍스트 메시지를 보낸다.
     * @param chatRoomId TODO : 추후에 ChatRoomId 명시
     * @param msg
     */
    public void sendTextMessage(String chatRoomId, String msg) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = MsgServerConfig.getSendMessageAPIUri();

            UserConfig user = UserConfig.getInstance();
            JSONObject obj = new JSONObject();
            obj.put(MsgServerConfig.KEY_MSG, msg);
            // TODO : 추후 google ID로 SENDER를 분류해야 하지 않을까?
            obj.put(MsgServerConfig.KEY_SENDER, GMMApplication.getToken());
            obj.put(MsgServerConfig.KEY_SENDER_GOOGLE, user.getUserId());
            obj.put(MsgServerConfig.KEY_SENDER_NAME, user.getUserName());
            obj.put(MsgServerConfig.KEY_SENDER_PROFILE_URI, user.getProfilePicUri());
            obj.put(MsgServerConfig.KEY_CHAT_ROOM_ID, chatRoomId);
            obj.put(MsgServerConfig.KEY_MSG_TIME, System.currentTimeMillis());

            String json = obj.toString();
            RequestBody body = RequestBody.create(MsgServerConfig.MEDIATYPE_JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(onMessageCallback);
        } catch(Exception e) {
        }
    }

    /**
     * 메시지 전송 결과에 대한 Callback
     */
    private Callback onMessageCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            // TODO : 메시지 전송에 실패했을 때 알맞은 처리를 추후에 한다.
            int debug;
            debug = 1;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            // TODO : 메시지 전송에 성공했을 때 알맞은 처리를 추후에 한다.
            int debug;
            debug = 1;
        }
    };
}
