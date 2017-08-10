package com.app.blackorange.agencyapp.views.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.models.HouseInfo;

import java.util.List;

/**
 * Created by BlackOrange on 2017/5/14.
 */

public class HouseContentAdapter extends ArrayAdapter<String> {
    private int resource;
    public HouseContentAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //房屋图片
        final String img = getItem(position);
        View view;
        HouseContentHolder houseContentHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            houseContentHolder = new HouseContentHolder();
            houseContentHolder.houseImgView = (TextView) view.findViewById(R.id.person_item);
            view.setTag(houseContentHolder);
        }else {
            view = convertView;
            houseContentHolder = (HouseContentHolder) view.getTag();
        }
//        houseContentHolder.houseImgView.setImageResource(R.mipmap.house_img_pressed);
        houseContentHolder.houseImgView.setBackgroundColor(Color.parseColor("#000000"));
        return view;
    }
    class HouseContentHolder{
        TextView houseImgView;
    }
}
