package com.app.blackorange.agencyapp.views.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.models.AddressBook;

import java.util.List;

/**
 * Created by BlackOrange on 2017/5/14.
 */

public class AddressBookAdapter extends ArrayAdapter<AddressBook> {
    private int resource;

    public AddressBookAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<AddressBook> objects) {
        super(context, resource, objects);
        this.resource =resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final AddressBook addressBook = getItem(position);
        View view;
        AddressBookHolder addressBookHolder;
        if (convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            addressBookHolder = new AddressBookHolder();
            addressBookHolder.nameView = (TextView) view.findViewById(R.id.person_item);
            addressBookHolder.nameView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
//            addressBookHolder.nameView.setGravity(Gravity.CENTER);
            view.setTag(addressBookHolder);
        }else {
            view = convertView;
            addressBookHolder = (AddressBookHolder) view.getTag();
        }
        addressBookHolder.nameView.setText(addressBook.getName());
        return view;
    }

    class AddressBookHolder{
         TextView nameView;
    }
}
