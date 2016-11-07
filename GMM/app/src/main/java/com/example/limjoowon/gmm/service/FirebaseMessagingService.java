package com.example.limjoowon.gmm.service;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Google FCM 메시지를 받는 서비스 클래스
 * Created by kijong on 2016-11-07.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String message = remoteMessage.getData().get("message");
    }

}
