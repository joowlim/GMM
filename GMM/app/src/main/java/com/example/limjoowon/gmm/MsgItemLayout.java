package com.example.limjoowon.gmm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.limjoowon.gmm.config.MsgServerConfig;
import com.example.limjoowon.gmm.config.UserConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.net.URL;

/**
 * Created by kijong on 2016-11-30.
 */

public class MsgItemLayout extends LinearLayout {
    private MsgItemData mModel;
    private ImageView mProfile;
    private TextView mSenderName, mSenderMsg, mMyMsg;
    private LinearLayout mYouLayout, mMyLayout;

    public MsgItemLayout(Context context, MsgItemData model) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.msg_list_item, this, true);
        mModel = model;
        initializeControls();
        updateView();
    }

    public void setModel(MsgItemData model) {
        if(mModel != model) {
            mModel = model;
            updateView();
        }
    }

    private void initializeControls() {
        mProfile = (ImageView) findViewById(R.id.item_profile);
        mSenderName = (TextView) findViewById(R.id.item_sender_name);
        mSenderMsg = (TextView) findViewById(R.id.item_sender_msg);
        mMyMsg = (TextView) findViewById(R.id.item_sender_msg_me);

        mYouLayout = (LinearLayout) findViewById(R.id.you_layout);
        mMyLayout = (LinearLayout) findViewById(R.id.myLayout);
    }

    private void updateView() {
        try {
            if (mModel.senderProfile.equals(UserConfig.DEFAULT_PROFILE_URI)) {
                mProfile.setImageDrawable(getResources().getDrawable(R.mipmap.default_profile));
            } else {
                ImageLoader.getInstance().displayImage(mModel.senderProfile, mProfile);
            }
            mSenderName.setText(mModel.senderName);
            mSenderMsg.setText(mModel.msg);
            mMyMsg.setText(mModel.msg);

            if (mModel.fromMe) {
                mYouLayout.setVisibility(View.GONE);
                mMyLayout.setVisibility(View.VISIBLE);
            } else {
                mYouLayout.setVisibility(View.VISIBLE);
                mMyLayout.setVisibility(View.GONE);
            }

        } catch(Exception e) {
        }
    }
}
