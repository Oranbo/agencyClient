package com.app.blackorange.agencyapp.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.controllers.AddressBookController;
import com.app.blackorange.agencyapp.controllers.UserController;
import com.app.blackorange.agencyapp.models.AddressBook;
import com.app.blackorange.agencyapp.models.Usr;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BlackOrange on 2017/5/15.
 */

public class SelfInfoFragment extends Fragment implements TextWatcher{
    private static int TAG=0;
    private TextView headImg;
    private TextView titleText;
    private TextView topMenu;
    private TextView loginNameView;
    private TextView idView;
    private TextView nameView;
    private TextView sexView;
    private TextView phoneView;
    private TextView addressView;
    private AddressBook nInfo;
    private Usr user;
    private String name;
    private String phone;
    private String sex;
    private String address;

    public static SelfInfoFragment newInstance(Usr usr) {

        Bundle args = new Bundle();

        SelfInfoFragment fragment = new SelfInfoFragment();
        ArrayList list = new ArrayList();
        list.add(usr);
        args.putParcelableArrayList("user",list);
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
        View view = inflater.inflate(R.layout.people_content,null);
        headImg = (TextView) view.findViewById(R.id.self_head_picture);
        titleText = (TextView) view.findViewById(R.id.title_textView);
        topMenu = (TextView) view.findViewById(R.id.top_menu);
        idView = (TextView) view.findViewById(R.id.self_id);
        loginNameView = (TextView) view.findViewById(R.id.self_logname);
        nameView = (TextView) view.findViewById(R.id.self_name);
        sexView = (TextView) view.findViewById(R.id.self_sex);
        phoneView = (TextView) view.findViewById(R.id.self_phone);
        addressView = (TextView) view.findViewById(R.id.self_address);
        initEvents();
        return view;
    }

    private void initEvents() {
        topMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu();
            }
        });
        nameView.addTextChangedListener(this);
        sexView.addTextChangedListener(this);
        phoneView.addTextChangedListener(this);
        addressView.addTextChangedListener(this);
    }

    private void popupMenu() {
        PopupMenu pop = new PopupMenu(getActivity(),topMenu);
        MenuInflater inflater = pop.getMenuInflater();
        inflater.inflate(R.menu.selfmenu,pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.self_cha_item:
                        nameView.setEnabled(true);
                        sexView.setEnabled(true);
                        phoneView.setEnabled(true);
                        addressView.setEnabled(true);
                        topMenu.setText("确定");
                        topMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                        topMenu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("name------",name+"11");
                                if (!name.equals(" ")&&!sex.equals(" ")&&!phone.equals(" ")&&!address.equals(" ")){
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Message msg = Message.obtain();
                                            Bundle data = new Bundle();
                                            AddressBookController acon = new AddressBookController();
                                            ObjectMapper mapper = new ObjectMapper();
                                            AddressBook a = user.getAddressBook();
//                                        a.setA_id(user.getAddressBook().getA_id());
                                            a.setName(name);
                                            a.setSex(sex);
                                            a.setPhone_number(phone);
                                            a.setAddress(address);
                                            int status=-1;
                                            try {
                                                status =acon.getStatusFromServer("updatePersonInfo.json","personInfo",mapper.writeValueAsString(a));
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (status==0){
                                                data.putString("TAG","1");
                                                Looper.prepare();
                                                Toast toast = Toast.makeText(getActivity(),"修改成功！",Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                                Looper.loop();
                                            }

                                        }
                                    }).start();
                                }else {
                                    Log.e("name------",name);
                                    Toast toast = Toast.makeText(getActivity(),"请输入完整信息后重试！",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }

                            }
                        });
                        return true;
                    case R.id.self_cpw_item:
                        ChangePwFragment cpf = ChangePwFragment.newInstance(user);
                        transaction.replace(R.id.content,cpf);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
                    case R.id.self_order_item:
                        BOListFragment bolf = BOListFragment.newInstance(user);
                        transaction.replace(R.id.content,bolf);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
                    default:
                        return false;
                }
            }
        });
        pop.show();
    }


    private void initData() {
        ArrayList list = getArguments().getParcelableArrayList("user");
        Usr u = (Usr) list.get(0);
        final String name = u.getLogin_name();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                Bundle data = new Bundle();
                UserController con = new UserController();
                Usr usr1 = null;
                try {
                    usr1 = con.getFromServer("getByLogName.json","name",name);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                data.putSerializable("Usr",usr1);
                data.putString("Tag","1");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            user = (Usr) data.getSerializable("Usr");
            TAG = Integer.parseInt(data.getString("Tag"));
            if (TAG==1){
                initView();
            }
        }
    };

    private void initView() {
        titleText.setText("个人中心");
        if(user!=null){
            loginNameView.setText(user.getLogin_name());
            idView.setText(user.getId()+"");
            if (user.getAddressBook()!=null){
                nameView.setText(user.getAddressBook().getName());
                sexView.setText(user.getAddressBook().getSex());
                phoneView.setText(user.getAddressBook().getPhone_number());
                addressView.setText(user.getAddressBook().getAddress());
            }
        }
        loginNameView.setEnabled(false);
        idView.setEnabled(false);
        nameView.setEnabled(false);
        sexView.setEnabled(false);
        phoneView.setEnabled(false);
        addressView.setEnabled(false);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        name = nameView.getText().toString();
//        sex = sexView.getText().toString();
//        phone = phoneView.getText().toString();
//        address = addressView.getText().toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        name = nameView.getText().toString();
        sex = sexView.getText().toString();
        phone = phoneView.getText().toString();
        address = addressView.getText().toString();
    }
}
