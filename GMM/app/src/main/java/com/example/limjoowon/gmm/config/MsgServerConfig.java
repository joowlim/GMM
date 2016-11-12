package com.example.limjoowon.gmm.config;

import okhttp3.MediaType;

/**
 * 메시징 서버와 관련된 설정값들을 정의
 * Created by kijong on 2016-11-07.
 */

public class MsgServerConfig {

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
     * JSON 에서 메시지를 분류하는 Key값
     */
    public static String KEY_MSG = "msg";

    /**
     * JSON 에서 Sender를 분류하는 Key값
     */
    public static String KEY_SENDER = "sender_id";

    /**
     * TODO: 하드코딩 CHAT_ROOM_ID 추후 제거
     */
    public static String CHAT_ROOM_ID = "chat_room_id";
}
