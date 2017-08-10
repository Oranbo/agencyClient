package com.app.blackorange.agencyapp.views.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.models.BusinessOrder;

import java.util.List;

/**
 * Created by BlackOrange on 2017/5/21.
 */

public class OrderAdapter extends ArrayAdapter<BusinessOrder> {
    private int resource;
    public OrderAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<BusinessOrder> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final BusinessOrder businessOrder = getItem(position);
        View view;
        BusinessHoder businessHoder;
        if (convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            businessHoder = new BusinessHoder();
            businessHoder.orderIdView = (TextView) view.findViewById(R.id.order_id);
            businessHoder.orerHview = (TextView) view.findViewById(R.id.order_hid);
            businessHoder.orderGView = (TextView) view.findViewById(R.id.order_gid);
            view.setTag(businessHoder);
        }else {
            view =convertView;
            businessHoder = (BusinessHoder) view.getTag();
        }
        businessHoder.orderIdView.setText("单号："+businessOrder.getB_id()+"");
        businessHoder.orerHview.setText("房屋编号："+businessOrder.getHouseInfo().getH_id()+"");
        businessHoder.orderGView.setText("客户编号："+businessOrder.getAddressBook2().getA_id()+"");
        return view;
    }
    class BusinessHoder{
        TextView orderIdView;
        TextView orerHview;
        TextView orderGView;
    }
}
