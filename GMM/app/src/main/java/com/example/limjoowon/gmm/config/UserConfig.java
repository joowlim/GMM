package com.example.limjoowon.gmm.config;

import android.net.Uri;

/**
 * 사용자 정보 Singletone 클래스
 * Created by kijong on 2016-11-15.
 */
public class UserConfig {

    private volatile static UserConfig instance;

    /**
     * Google 사용자 ID (=Google Id)
     */
    private String mUserId;

    /**
     * Google 사용자 이름(=Google 이름)
     */
    private String mUserName;

    /**
     * Google 사용자 프로필 주소
     */
    private String mProfilePicUri;

    private final String DEFAULT_PROFILE_URI = "http://pic2.pbsrc.com/common/profile_female_large.jpg";

    private UserConfig(){
        mUserId = "";
        mUserName = "";
    }

    /**
     * Singleton 객체 생성 및 반환
     */
    public static UserConfig getInstance() {
        if(instance == null) {
            synchronized( UserConfig.class ) {
                if ( instance == null )
                    instance = new UserConfig();
            }
        }
        return instance;
    }

    /**
     * UserConfig 객체를 초기화 한다. Login Activity 에서 인증 후 호출됨
     */
    public void initialize(String userId, String userName, Uri profileUri) {
        mUserId = userId;
        mUserName = userName;
        mProfilePicUri = (profileUri!=null)? profileUri.toString() : DEFAULT_PROFILE_URI;
    }

    /**
     * 사용자 ID 반환
     */
    public String getUserId() {
        return mUserId;
    }

    /**
     * 사용자 이름 반환
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * 사용자 프로필 사진 주소 반환
     */
    public String getProfilePicUri() {
        return  mProfilePicUri;
    }
}
