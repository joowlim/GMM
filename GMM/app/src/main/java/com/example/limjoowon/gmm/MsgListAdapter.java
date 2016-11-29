package com.example.limjoowon.gmm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kijong on 2016-11-30.
 */

public class MsgListAdapter extends BaseAdapter {
    private List<MsgItemData> mItems;
    private Context mContext;

    public MsgListAdapter(Context context, List<MsgItemData> items) {
        super();
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public MsgItemData getItem(int pos) {
        // TODO Auto-generated method stub
        return mItems.get(pos);
    }

    public void setItems(List<MsgItemData> items) {
        mItems = items;
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int pos, View coView, ViewGroup parent) {
        MsgItemLayout itemLayout;
        MsgItemData item = mItems.get(pos);
        if( coView == null ) {
            itemLayout = new MsgItemLayout(mContext, item);
        }else {
            itemLayout = (MsgItemLayout) coView;
            itemLayout.setModel(item);
        }
        return itemLayout;
    }

}
