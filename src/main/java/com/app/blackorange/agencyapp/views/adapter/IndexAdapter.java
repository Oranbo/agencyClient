package com.app.blackorange.agencyapp.views.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.blackorange.agencyapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BlackOrange on 2017/5/14.
 */

public class IndexAdapter extends ArrayAdapter<String> {
    private int resource;
    public IndexAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.resource =resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String index = getItem(position);
        View view;
        IndexHoler indexHolder;
        if (convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            indexHolder = new IndexHoler();
            indexHolder.indexText = (TextView) view.findViewById(R.id.person_item);
//            ViewGroup.LayoutParams params = indexHolder.indexText.getLayoutParams();
//            params.width = 15;
//            params.height = 15;
            indexHolder.indexText.setGravity(Gravity.CENTER);
            view.setTag(indexHolder);
        }else {
            view = convertView;
            indexHolder = (IndexHoler) view.getTag();

        }
//                indexHolder.indexText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setBackgroundColor(Color.parseColor("#28ff28"));
//            }
//        });
        indexHolder.indexText.setText(index);
        return view;
    }
    class IndexHoler{
        TextView indexText;
    }

}
