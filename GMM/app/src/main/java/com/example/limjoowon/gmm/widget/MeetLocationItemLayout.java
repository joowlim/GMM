package com.example.limjoowon.gmm.widget;

import android.content.Context;
import android.view.LayoutInflater;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.limjoowon.gmm.R;

/**
 * Created by kijong on 2016-12-05.
 */

public class MeetLocationItemLayout extends LinearLayout {
    private MeetLocationItemData mModel;
    private ImageView mLocationImg;
    private TextView mLocationName;

    public MeetLocationItemLayout(Context context, MeetLocationItemData model) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.location_list_item, this, true);
        mModel = model;
        initializeControls();
        updateView();
    }

    public void setModel(MeetLocationItemData model) {
        if(mModel != model) {
            mModel = model;
            updateView();
        }
    }

    private void initializeControls() {
        mLocationImg = (ImageView) findViewById(R.id.location_item_image);
        mLocationName = (TextView) findViewById(R.id.location_item_name);
    }

    private void updateView() {
        if (mModel.mBuildingNo == "N1") {
            mLocationImg.setImageDrawable(getResources().getDrawable(R.mipmap.n1_building));
        } else {
            mLocationImg.setImageDrawable(getResources().getDrawable(R.mipmap.e3_building));
        }
        mLocationName.setText(mModel.mName);
    }
}
