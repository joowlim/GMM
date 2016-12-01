package com.example.limjoowon.gmm.module;

import com.example.limjoowon.gmm.config.TimeConfig;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by LimJoowon on 2016. 11. 24..
 */
public class TimeManager {
    /*
        Time 정보를 보낸다
     */

    public static void sendTimeInfo(String session_id, String user_id, String time_info, Callback callback){
        try {
            OkHttpClient client = new OkHttpClient();
            String url = TimeConfig.sendTimeInfoAPIUri();
            JSONObject obj = new JSONObject();
            obj.put(TimeConfig.KEY_SESSION, session_id);
            obj.put(TimeConfig.KEY_USER, user_id);
            obj.put(TimeConfig.KEY_TIME, time_info);

            String json = obj.toString();
            RequestBody body = RequestBody.create(TimeConfig.MEDIATYPE_JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            if (callback == null) {
                callback = onMessageCallback;
            }
            client.newCall(request).enqueue(callback);
        } catch(Exception e) {
        }
    }
    /*
        모든 Time 정보를 받는다
     */
    public static void getMixedTime(String session_id, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = TimeConfig.getAllTimeInfoAPIUri() + "?session_id=" + session_id;
        Request request = new Request.Builder().url(url).build();
        if (callback == null) {
            callback = onMessageCallback;
        }
        client.newCall(request).enqueue(callback);
    }

    /**
     * 메시지 전송 결과에 대한 Callback
     */

    private static Callback onMessageCallback = new Callback() {
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
