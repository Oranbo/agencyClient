package com.app.blackorange.agencyapp.views.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.blackorange.agencyapp.R;

import java.util.List;

/**
 * Created by BlackOrange on 2017/5/15.
 */

public class HorizontalListViewAdapter extends BaseAdapter {
    List<String> mData;
    Context mContext;
    public HorizontalListViewAdapter(Context mContext, List<String> mData) {
        this.mData = mData;
        this.mContext=mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    private ViewHolder vh = new ViewHolder();

    private static class ViewHolder {
        private TextView imgView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.h_img_item, null);
            vh.imgView = (TextView) convertView.findViewById(R.id.h_item);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
