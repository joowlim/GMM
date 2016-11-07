package com.example.limjoowon.gmm;

import android.app.Application;
import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * 공용으로 사용되는 어플리케이션 클래스.
 * Application Context, FCM 을 위한 token 등 App System과 관련된 공용 정보를 관리한다.
 * Created by kijong on 2016-11-07.
 */

public class GMMApplication extends Application {

    /**
     * FCM 에 사용되는 해당 기기의 Token
     */
    private static String mToken;

    /**
     * Application Context
     */
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mToken = FirebaseInstanceId.getInstance().getToken();
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getToken() {
        return mToken;
    }

    public static void setToken(String token) {
        mToken = token;
    }
}
