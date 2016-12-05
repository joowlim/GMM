package com.example.limjoowon.gmm.config;

import okhttp3.MediaType;

/**
 * Created by LimJoowon on 2016. 11. 24..
 */
public class LocationConfig {
    /**
     * 우리 장소 서버 HOST URI
     */
    public static final String HOST_URI = "http://143.248.49.55:5000";

    /**
     * 미디어타입 JSON HTTP HEAER
     */
    public static final MediaType MEDIATYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");
    /**
     * JSON 에서 방을 분류하는 Key값
     */
    public static String KEY_ROOM= "ROOM_ID";

    /**
     * JSON 에서 사용자를 분류하는 Key값
     */
    public static String KEY_USER = "USER";

    /**
     * JSON 에서 위도를 나타내는 Key값
     */
    public static String KEY_POS_LAT = "POS_LAT";

    /**
     * JSON 에서 경도를 담고 있는 Key값
     */
    public static String KEY_POS_LON = "POS_LON";

    /**
     * 조원들 모두의 장소 얻기 api
     */
    public static String GET_ALL_LOCATION_INFO = "/position_info/";

    /**
     * 한 명의 장소 보내기 api
     */
    public static String SEND_LOCATION_API = "/position_info";

    public static String getAllLocationInfoAPIUri() {return HOST_URI + GET_ALL_LOCATION_INFO;}

    public static String sendLocationInfoAPIUri() {return HOST_URI + SEND_LOCATION_API;}

}
