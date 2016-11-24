package com.example.limjoowon.gmm.config;

import okhttp3.MediaType;

/**
 * Created by LimJoowon on 2016. 11. 24..
 */
public class TimeConfig {
    /**
     * 우리 메시징 서버 HOST URI
     */
    public static final String HOST_URI = "http://143.248.49.55:8888";

    /**
     * 미디어타입 JSON HTTP HEAER
     */
    public static final MediaType MEDIATYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");
    /**
     * JSON 에서 세션을 분류하는 Key값
     */
    public static String KEY_SESSION= "session_id";

    /**
     * JSON 에서 사용자를 분류하는 Key값
     */
    public static String KEY_USER = "user_id";

    /**
     * JSON 에서 참여자의 수를 나타내는 Key값
     */
    public static String KEY_NUM_PEOPLE = "num_participant";

    /**
     * JSON 에서 시간 정보를 담고 있는 Key값
     */
    public static String KEY_TIME = "time";

    /**
     * 현재 조모임 가능 시간 결과 얻기 api
     */
    public static String GET_ALL_TIME_INFO = "/time/result";

    /**
     * 한 명의 조모임 가능 시간 결과 보내기 api
     */
    public static String SEND_MESSAGE_API = "/time/update";

    public static String getAllTimeInfoAPIUri() {return HOST_URI + GET_ALL_TIME_INFO;}

    public static String sendTimeInfoAPIUri() {return HOST_URI + SEND_MESSAGE_API;}
}
