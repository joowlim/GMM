package com.example.limjoowon.gmm.service;

import android.util.Log;

import com.example.limjoowon.gmm.GMMApplication;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Google FCM Token 업데이트 알림을 받는 서비스 클래스
 * Created by kijong on 2016-11-07.
 */

public class FirebaseInstanceIDService  extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        GMMApplication.setToken(token);
    }
}
