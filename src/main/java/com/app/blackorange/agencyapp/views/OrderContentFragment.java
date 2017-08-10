package com.app.blackorange.agencyapp.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.controllers.AddressBookController;
import com.app.blackorange.agencyapp.controllers.BusinessOrderController;
import com.app.blackorange.agencyapp.controllers.HouseController;
import com.app.blackorange.agencyapp.controllers.UserController;
import com.app.blackorange.agencyapp.models.AddressBook;
import com.app.blackorange.agencyapp.models.BusinessOrder;
import com.app.blackorange.agencyapp.models.HouseInfo;
import com.app.blackorange.agencyapp.models.Usr;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BlackOrange on 2017/5/18.
 */

public class OrderContentFragment extends Fragment implements TextWatcher{
    private static int TAG=0;
    private TextView titleText;
    private TextView topMenu;
    private TextView empText;
    private View houseV;
    private TextView houseText;
    private TextView guestText;
    private TextView typeText;
    private TextView priceText;
    private View dateV;
    private TextView dateText;

    private BusinessOrder businessOrder;
    private Usr user;
    private HouseInfo houseInfo;
    private AddressBook guest;
    private String guestId;
    private String type;
    private String price;

    public static OrderContentFragment newInstance(HouseInfo houseInfo,Usr user,BusinessOrder businessOrder) {

        Bundle args = new Bundle();

        OrderContentFragment fragment = new OrderContentFragment();
        if (houseInfo!=null&&user!=null){
            args.putSerializable("Usr",user);
            args.putSerializable("HouseInfo",houseInfo);
        }
        if (businessOrder!=null){
            ArrayList list = new ArrayList();
            list.add(businessOrder);
            args.putParcelableArrayList("List",list);
        }
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
        View v= inflater.inflate(R.layout.order_content,null);
        titleText = (TextView) v.findViewById(R.id.title_textView);
        topMenu = (TextView) v.findViewById(R.id.top_menu);
        empText = (TextView) v.findViewById(R.id.order_emp);
        houseText = (TextView) v.findViewById(R.id.order_house);
        houseV = v.findViewById(R.id.o_house);
        dateV = v.findViewById(R.id.o_date_view);
        dateText = (TextView) v.findViewById(R.id.order_date);
        guestText = (TextView) v.findViewById(R.id.guest_id);
        typeText = (TextView) v.findViewById(R.id.order_type);
        priceText = (TextView) v.findViewById(R.id.order_price);
        initEvents();
        return v;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            TAG = Integer.parseInt(data.getString("TAG"));
            Log.e("OrderTAG---",TAG+"");
            if (TAG==1) {
                user = (Usr) data.getSerializable("Usr");
            }
            if (TAG==2){
                guest = (AddressBook) data.getSerializable("AddressBook");
            }
            Log.e("orderUser",user.getLogin_name());
            initView();
        }
    };
    private void initData() {
        houseInfo = (HouseInfo) getArguments().getSerializable("HouseInfo");
        Usr u = (Usr) getArguments().getSerializable("Usr");
        ArrayList list = getArguments().getParcelableArrayList("List");
        if (list!=null){
            businessOrder = (BusinessOrder) list.get(0);
        }
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
                data.putString("TAG","1");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }).start();
        initView();
    }

    private void initView() {
        if (user!=null&&businessOrder==null){
            empText.setText(user.getId()+"");
            houseV.setVisibility(View.INVISIBLE);
            dateV.setVisibility(View.INVISIBLE);
        }if (user!=null&&businessOrder!=null){
            titleText.setText("订单详情");
            topMenu.setVisibility(View.GONE);
            empText.setText(user.getId()+"");
            houseText.setText(businessOrder.getHouseInfo().getH_id()+"");
            guestText.setText(businessOrder.getAddressBook2().getA_id()+"");
            typeText.setText(businessOrder.getB_type());
            priceText.setText(businessOrder.getReal_price()+"");
            dateText.setText(businessOrder.getB_date()+"");

            empText.setEnabled(false);
            houseText.setEnabled(false);
            guestText.setEnabled(false);
            typeText.setEnabled(false);
            priceText.setEnabled(false);
            dateText.setEnabled(false);

        }
    }

    private void initEvents() {
        guestText.addTextChangedListener(this);
        typeText.addTextChangedListener(this);
        priceText.addTextChangedListener(this);
        if (businessOrder==null){
            titleText.setText("新增订单");
            topMenu.setText("确定");
            empText.setEnabled(false);
            topMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            topMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!type.isEmpty()&&!price.isEmpty()&&!guestId.isEmpty()){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = Message.obtain();
                                Bundle data = new Bundle();
                                ObjectMapper mapper = new ObjectMapper();
                                BusinessOrder order =new BusinessOrder();
                                AddressBookController abc = new AddressBookController();
                                HouseController hcon = new HouseController();
                                AddressBook a = null;
                                order.setB_id(1);
                                order.setHouseInfo(houseInfo);
                                order.setB_type(type);
                                order.setReal_price(new BigDecimal(Float.parseFloat(price)));
                                order.setEmployee(user);
                                int status=-1;
                                int sta = -1;
                                try {
                                    a = abc.getSingleFromServer("getPersonByID.json","id",guestId+"");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (a!=null){
                                    data.putSerializable("AddressBook",a);
                                    data.putString("TAG","2");
                                    msg.setData(data);
                                    handler.sendMessage(msg);
                                }else {
                                    Looper.prepare();
                                    Toast toast = Toast.makeText(getActivity(),"该客户不存在！请添加后重试",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    Looper.loop();
                                }
                                order.setAddressBook2(guest);
                                onResume();
                                if (TAG==2){
                                    try {

                                        status = BusinessOrderController.getStatusFromServer("addOrder.json","order",mapper.writeValueAsString(order));

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (status==0){
                                    houseInfo.setSell_status("已下架");
                                    if (type.equals("出租")) {
                                        houseInfo.setHouse_status("租出");
                                    }else {
                                        houseInfo.setHouse_status("售出");
                                    }
                                    try {
                                        sta =hcon.getStatusFromServer("updateHouseInfo.json","houseInfo",mapper.writeValueAsString(houseInfo));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("updateHouse",sta+"");
                                    Looper.prepare();
                                    Toast toast = Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    Looper.loop();
                                }else {
                                    Looper.prepare();
                                    Toast toast = Toast.makeText(getActivity(),"添加失败，请重试！",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    Looper.loop();
                                }
                            }
                        }).start();
                    }else {
                        Toast toast = Toast.makeText(getActivity(),"请输入完整信息后重试！",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        type = typeText.getText().toString();
        price = priceText.getText().toString();
        guestId = guestText.getText().toString();
    }
}
