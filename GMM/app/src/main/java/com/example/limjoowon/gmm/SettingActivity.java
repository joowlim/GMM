package com.example.limjoowon.gmm;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.TextView;

import com.example.limjoowon.gmm.config.UserConfig;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 설정 Activity
 * Created by KiJong Han on 2016. 11. 3..
 */
public class SettingActivity extends AppCompatActivity {
    ImageView mProfile;
    TextView mName, mEmail;
    Button mLogOutBtn;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUI();
    }

    /**
     * 채팅방 Activity의 UI를 초기화 한다.
     */
    private void initUI() {
        setTitle("설정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserConfig user = UserConfig.getInstance();

        mProfile = (ImageView) findViewById(R.id.setting_profile);
        mName = (TextView) findViewById(R.id.setting_name);
        mEmail = (TextView) findViewById(R.id.setting_id);

        ImageLoader.getInstance().displayImage(user.getProfilePicUri(), mProfile);
        mName.setText(user.getUserName());
        mEmail.setText(user.getUserId() + "@gmail.com");
        mLogOutBtn = (Button) findViewById(R.id.setting_logout);

        mLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Intent intent = new Intent();
                                Bundle extraBundle = new Bundle();
                                extraBundle.putBoolean("isLogout", true);
                                intent.putExtras(extraBundle);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        });
            }});
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
