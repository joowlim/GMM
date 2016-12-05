package com.example.limjoowon.gmm.module;

import com.example.limjoowon.gmm.config.LocationConfig;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by LimJoowon on 2016. 12. 5..
 */
public class LocationManager {
      /*
        Location 정보를 보낸다
     */
    public static void sendLocationInfo(String room_id, String user_id, double pos_lat, double pos_lon, Callback callback){
        try {
            OkHttpClient client = new OkHttpClient();
            String url = LocationConfig.sendLocationInfoAPIUri();
            JSONObject obj = new JSONObject();
            obj.put(LocationConfig.KEY_ROOM, room_id);
            obj.put(LocationConfig.KEY_USER, user_id);
            obj.put(LocationConfig.KEY_POS_LAT, pos_lat);
            obj.put(LocationConfig.KEY_POS_LON, pos_lon);

            String json = obj.toString();
            RequestBody body = RequestBody.create(LocationConfig.MEDIATYPE_JSON, json);
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
        모든 Location 정보를 받는다
     */
    public static void getAllLocationInfo(String room_id, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = LocationConfig.getAllLocationInfoAPIUri() + "abcd";
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
