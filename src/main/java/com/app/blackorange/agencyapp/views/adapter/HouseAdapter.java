package com.app.blackorange.agencyapp.views.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.models.HouseInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BlackOrange on 2017/5/12.
 */

public class HouseAdapter extends ArrayAdapter<HouseInfo> {
    private int resource;
    public HouseAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<HouseInfo> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position,  View convertView, @NonNull ViewGroup parent) {
        final HouseInfo houseInfo = getItem(position);
        View view;
        HouseViewHolder viewHolder;
        if (convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            viewHolder = new HouseViewHolder();
            viewHolder.houseImgs = (ImageView) view.findViewById(R.id.house_imgs);
            viewHolder.address = (TextView) view.findViewById(R.id.address);
            viewHolder.houseInfo = (TextView) view.findViewById(R.id.address);
            viewHolder.houseType = (TextView) view.findViewById(R.id.house_type);
            viewHolder.houseSize = (TextView) view.findViewById(R.id.house_size);
            viewHolder.houseLayout = (TextView) view.findViewById(R.id.house_layout);
            viewHolder.price = (TextView) view.findViewById(R.id.price);
            view.setTag(viewHolder);

        }else {
            view = convertView;
            viewHolder = (HouseViewHolder) view.getTag();
        }
        viewHolder.houseImgs.setImageResource(R.mipmap.home);
        viewHolder.houseInfo.setText("房屋简讯");
        viewHolder.houseLayout.setText(houseInfo.getLayout());
        viewHolder.houseSize.setText(houseInfo.getSize().toString()+"㎡");
        if (houseInfo.getH_type().equals("出售")){
            viewHolder.houseType.setText(houseInfo.getH_type());
            Log.e("price",houseInfo.getS_price().toString());
            String[] str = houseInfo.getS_price().toString().split("\\.");
            String price = str[0];
            String simplePrice = price.substring(0,price.length()-4);
            viewHolder.price.setText(simplePrice+"万元");
        }else if (houseInfo.getH_type().equals("出租")){
//            viewHolder.houseType.setText(houseInfo.getRent_type());
            viewHolder.houseType.setText(houseInfo.getH_type());
            String[] str = houseInfo.getR_price().toString().split("\\.");
            String price = str[0];
            viewHolder.price.setText(price+"元/月");
        }
        viewHolder.address.setText(houseInfo.getAddress());

        return view;
    }


}
