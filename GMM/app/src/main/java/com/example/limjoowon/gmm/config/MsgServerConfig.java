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
     * JSON 에서 Sender를 분류하는 Key값 TOKEN
     */
    public static String KEY_SENDER = "sender_id";

    /**
     * JSON에서 Sender의 Google ID
     */
    public static String KEY_SENDER_GOOGLE = "sender_google";

    /**
     * JSON에서 Sender의 Google 이름
     */
    public static String KEY_SENDER_NAME = "sender_name";

    /**
     * JSON에서 Sender의 PorfileUri
     */
    public static String KEY_SENDER_PROFILE_URI = "sender_profile";

    /**
     * JSON에서 메시지가 온 ChatRoomId KEY
     */
    public static String KEY_CHAT_ROOM_ID = "key_chat_room_id";

    /**
     * 메시지가 생성된 시간
     */
    public static String KEY_MSG_TIME = "key_msg_time";

    /**
     * 메시지를 받은 시간
     */
    public static String KEY_MSG_GET_TIME = "key_msg_get_time";

    /**
     * TODO: 하드코딩 CHAT_ROOM_ID 추후 제거
     */
    public static String CHAT_ROOM_ID = "chat_room_id";

    /**
     * 메시지 전송 API
     */
    public static String SEND_MESSAGE_API = "/message";

    /**
     * 사용자 등록 API
     */
    public static String REGISTER_USER_API = "/user/register";

    /**
     * 사용자 검색 API
     */
    public static String SEARCH_USER_API =  "/user/search";

    /**
     * 채팅방 생성 API
     */
    public static String CREATE_CHAT_ROOM_API = "/chat/create";

    /**
     * 메시지 전송 URI를 얻어온다.
     */
    public static String getSendMessageAPIUri() {
        return HOST_URI + SEND_MESSAGE_API;
    }

    /**
     * 사용자 등록 API URI를 얻어온다.
     */
    public static String getRegisterUserAPIUri() {
        return HOST_URI + REGISTER_USER_API;
    }

    /**
     * 사용자 검색 API URI를 얻어온다.
     */
    public static String getSearchUserAPIUri() {
        return HOST_URI + SEARCH_USER_API;
    }

    /**
     * 채팅방 생성 API URI를 얻어온다.
     */
    public static String getCreateChatRoomAPIUri() {
        return HOST_URI + CREATE_CHAT_ROOM_API;
    }
}
