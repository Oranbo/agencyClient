package com.app.blackorange.agencyapp.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.controllers.BusinessOrderController;
import com.app.blackorange.agencyapp.models.BusinessOrder;
import com.app.blackorange.agencyapp.models.HouseInfo;
import com.app.blackorange.agencyapp.models.Usr;
import com.app.blackorange.agencyapp.views.adapter.OrderAdapter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by BlackOrange on 2017/5/21.
 */

public class BOListFragment extends Fragment {
    private TextView titleText;
    private TextView topMenu;
    private ListView orderListView;
    private OrderAdapter orderAdapter;
    private ArrayList<BusinessOrder> businessOrders =new ArrayList<>();
    private Usr user;

    private static int TAG=0;

    public static BOListFragment newInstance(Usr user) {

        Bundle args = new Bundle();

        BOListFragment fragment = new BOListFragment();
        args.putSerializable("Usr",user);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        View v= inflater.inflate(R.layout.order_list,null);
        titleText = (TextView) v.findViewById(R.id.title_textView);
        topMenu = (TextView) v.findViewById(R.id.top_menu);
        orderListView = (ListView) v.findViewById(R.id.order_listView);
        initEvents();
        return v;
    }

    private void initEvents() {
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusinessOrder bu = (BusinessOrder) parent.getItemAtPosition(position);
                OrderContentFragment ocf = OrderContentFragment.newInstance(bu.getHouseInfo(),bu.getEmployee(),bu);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content,ocf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void initData() {
        user = (Usr) getArguments().getSerializable("Usr");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<BusinessOrder> b =null;
                ArrayList list = new ArrayList();
                Message msg = new Message();
                Bundle data = new Bundle();
                try {
                    b = BusinessOrderController.getFromServer("getOrdersByEid.json","employee_id",user.getId()+"");
                } catch (IOException e) {
                    e.printStackTrace();
                }if (b!=null){
                    list.add(b);
                    data.putString("TAG","1");
                    data.putParcelableArrayList("List",list);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            TAG = Integer.parseInt(data.getString("TAG"));
            Log.e("orderTag",TAG+"");
            ArrayList list = data.getParcelableArrayList("List");
            ArrayList<BusinessOrder> business = (ArrayList<BusinessOrder>) list.get(0);
            if (TAG==1){
                businessOrders =business;
            }
            initView();

        }
    };

    private void initView() {
        titleText.setText("成交订单列表");
        topMenu.setVisibility(View.INVISIBLE);
        orderAdapter = new OrderAdapter(getActivity(),R.layout.order_item,businessOrders);
        orderListView.setAdapter(orderAdapter);
    }
}
