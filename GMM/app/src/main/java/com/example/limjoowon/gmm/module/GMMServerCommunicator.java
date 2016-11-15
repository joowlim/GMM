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
 * 우리 GMM 서버와 통신하는 모듈. 여러 API를 대신 호출해준다.
 * Created by kijong on 2016-11-15.
 */
public class GMMServerCommunicator {

    /**
     * /user/register 호출
     * 사용자 정보를 서버에 등록한다. 이미 DB에 있는 사용자면 추가등록 안하는 것은 서버에서 처리.
     */
    public void registerUserToServer(Callback callback) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = MsgServerConfig.getRegisterUserAPIUri();

            UserConfig userConfig = UserConfig.getInstance();
            JSONObject obj = new JSONObject();
            obj.put("user_id", userConfig.getUserId());
            obj.put("name", userConfig.getUserName());
            obj.put("profile_pic", userConfig.getProfilePicUri());
            obj.put("fcm_token", GMMApplication.getToken());

            String json = obj.toString();
            RequestBody body = RequestBody.create(MsgServerConfig.MEDIATYPE_JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            // 사용자가 지정한 callback이 없으면 기본 Callback으로 설정
            if (callback == null) {
                callback = onMessageCallback;
            }
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
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
