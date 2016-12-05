package com.example.limjoowon.gmm.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by kijong on 2016-12-05.
 */

public class MeetLocationItemAdapter extends BaseAdapter {
    private List<MeetLocationItemData> mItems;
    private Context mContext;

    public MeetLocationItemAdapter(Context context, List<MeetLocationItemData> items) {
        super();
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public MeetLocationItemData getItem(int pos) {
        // TODO Auto-generated method stub
        return mItems.get(pos);
    }

    public void setItems(List<MeetLocationItemData> items) {
        mItems = items;
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int pos, View coView, ViewGroup parent) {
        MeetLocationItemLayout itemLayout;
        MeetLocationItemData item = mItems.get(pos);
        if( coView == null ) {
            itemLayout = new MeetLocationItemLayout(mContext, item);
        } else {
            itemLayout = (MeetLocationItemLayout) coView;
            itemLayout.setModel(item);
        }
        return itemLayout;
    }
}
