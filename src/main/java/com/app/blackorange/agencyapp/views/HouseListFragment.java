package com.app.blackorange.agencyapp.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.controllers.HouseController;
import com.app.blackorange.agencyapp.controllers.UserController;
import com.app.blackorange.agencyapp.models.HouseInfo;
import com.app.blackorange.agencyapp.models.Usr;
import com.app.blackorange.agencyapp.views.adapter.HouseAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BlackOrange on 2017/5/12.
 */

public class HouseListFragment extends Fragment implements TextWatcher {

    private HouseAdapter houseAdapter;
    private ArrayList<HouseInfo> houseInfoList = new ArrayList<>();
    private ArrayList<HouseInfo> searchResult = new ArrayList<>();
    private ListView houseListView;
    private EditText searchText;
    private TextView titleView;
    private TextView topMenu;
    private ImageButton searchButton;
    private HouseController houseController;
    private Usr user;
    private String searchIndex;

    public static HouseListFragment newInstance(Usr user) {

        Bundle args = new Bundle();
        Log.e("HouseListuser----",user.getLogin_name());
        HouseListFragment fragment = new HouseListFragment();
        ArrayList list = new ArrayList();
        list.add(user);
        args.putParcelableArrayList("user",list);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        View v= inflater.inflate(R.layout.house_list_fragment,null);
        houseListView = (ListView) v.findViewById(R.id.house_list);
        searchText = (EditText) v.findViewById(R.id.search_text);
        searchButton = (ImageButton) v.findViewById(R.id.search_button);
        titleView = (TextView) v.findViewById(R.id.title_textView);
        topMenu = (TextView) v.findViewById(R.id.top_menu);
        searchText.addTextChangedListener(this);
        initEvents();
        houseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HouseInfo houseInfo = (HouseInfo) parent.getItemAtPosition(position);
                HouseContentFragment hcf = HouseContentFragment.newInstance(houseInfo,user);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content,hcf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return v;
    }


    private void initView() {
        titleView.setText("房屋信息列表");
        houseAdapter = new HouseAdapter(getActivity(),R.layout.house_list_item,houseInfoList);
        houseListView.setAdapter(houseAdapter);
    }

    private void initEvents() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchIndex==null){
                    Toast toast = Toast.makeText(getActivity(),"请输入查询内容！",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(getActivity(),searchIndex+"查询中……",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            Bundle data = new Bundle();
                            HouseController con = new HouseController();
                            ArrayList list = new ArrayList();
                            ArrayList<HouseInfo> houseList = null;
                            try {
                                houseList = con.getFromServer("/getByAddress.json","address",searchIndex);
                            } catch (IOException e) {
                                Toast toast = Toast.makeText(getActivity(), "出错！请重试", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            list.add(houseList);
                            data.putParcelableArrayList("houseList",list);
                            msg.setData(data);
                            handler.sendMessage(msg);
                            houseInfoList.clear();
                        }
                    }).start();
                }

               HouseListFragment.super.onResume();
            }
        });
        topMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu();
            }
        });
    }

    private void popupMenu() {
        PopupMenu pop = new PopupMenu(getActivity(),topMenu);
        MenuInflater inflater = pop.getMenuInflater();
        inflater.inflate(R.menu.popupmenu,pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_item:
                        HouseContentFragment hcf = HouseContentFragment.newInstance(null,user);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.content,hcf);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
//                    case R.id.filter_item:
//                        return true;
                    default:
                        return false;
                }
            }
        });

        pop.show();
    }

    private void initData() {
        List list = getArguments().getParcelableArrayList("user");
        user = (Usr) list.get(0);
        new Thread(networkTask).start();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            ArrayList list = data.getParcelableArrayList("houseList");
            ArrayList<HouseInfo> houseInfo = (ArrayList<HouseInfo>) list.get(0);
            houseInfoList = houseInfo;
            initView();

        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            Message msg = new Message();
            Bundle data = new Bundle();
            ArrayList list = new ArrayList();
            ArrayList<HouseInfo>  houseInfos=null;
            houseController = new HouseController();
            try {
              houseInfos = houseController.getFromServer("getHousesBySellStatus.json","sell_status","上架中");
            } catch (IOException e) {
                e.printStackTrace();
            }
            list.add(houseInfos);
            data.putParcelableArrayList("houseList",list);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        searchIndex = searchText.getText().toString();
    }
}
