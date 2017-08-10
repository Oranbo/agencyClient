package com.app.blackorange.agencyapp.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.controllers.AddressBookController;
import com.app.blackorange.agencyapp.controllers.HouseController;
import com.app.blackorange.agencyapp.controllers.UserController;
import com.app.blackorange.agencyapp.models.AddressBook;
import com.app.blackorange.agencyapp.models.HouseInfo;
import com.app.blackorange.agencyapp.models.Usr;
import com.app.blackorange.agencyapp.views.adapter.HorizontalListViewAdapter;
import com.app.blackorange.agencyapp.views.adapter.HouseContentAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BlackOrange on 2017/5/14.
 */

public class HouseContentFragment extends Fragment implements TextWatcher{
    private static int TAG =0;
    private HorizontalListViewAdapter hla;

    private HorizontalListView imgListView;
    private TextView addressText;
    private TextView floorText;
    private TextView decoText;
    private TextView layoutText;
    private TextView sizeText;
    private TextView hostText;
    private TextView priceText;
    private TextView phoneText;
    private TextView typeText;
    private TextView rTypeText;
    private TextView rrrrText;

    private TextView topMenu;
    private TextView titleView;
    private ImageButton orderButton;

    private ArrayList<String> imgList = new ArrayList<>();
    private HouseInfo houseInfo;

    private String address;
    private String floor;
    private String decoration;
    private String layout;
    private String size;
    private String host;
    private String price;
    private AddressBook houseHost;
    private AddressBook empployee;
    private Usr user;
    private String type;
    private String rType;

    public static HouseContentFragment newInstance(HouseInfo houseInfo, Usr user) {

        Bundle args = new Bundle();

        HouseContentFragment fragment = new HouseContentFragment();
        args.putSerializable("Usr",user);
        ArrayList list = new ArrayList();
        if (houseInfo!=null){
            list.add(houseInfo);
            args.putParcelableArrayList("houseInfo",list);
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
        View v= inflater.inflate(R.layout.house_content,null);
        imgListView = (HorizontalListView) v.findViewById(R.id.h_img_view);
        priceText = (TextView) v.findViewById(R.id.h_contenttextView);
        addressText = (TextView) v.findViewById(R.id.h_content_address);
        floorText = (TextView) v.findViewById(R.id.h_content_floor);
        decoText = (TextView) v.findViewById(R.id.h_content_decoration);
        layoutText  = (TextView) v.findViewById(R.id.h_content_layout);
        sizeText = (TextView) v.findViewById(R.id.h_content_size);
        hostText = (TextView) v.findViewById(R.id.h_content_host);
        phoneText = (TextView) v.findViewById(R.id.h_content_phone);
        typeText = (TextView) v.findViewById(R.id.h_content_type);
        rTypeText = (TextView) v.findViewById(R.id.h_content_rtype);
        titleView = (TextView) v.findViewById(R.id.title_textView);
        topMenu = (TextView) v.findViewById(R.id.top_menu);
        rrrrText = (TextView) v.findViewById(R.id.h_content_rrrr);

        initView();
        initEvents();
        hla = new HorizontalListViewAdapter(this.getContext(),imgList);
        imgListView.setAdapter(hla);
        return v;
    }

    private void initEvents() {
        addressText.addTextChangedListener(this);
        floorText.addTextChangedListener(this);
        decoText.addTextChangedListener(this);
        layoutText.addTextChangedListener(this);
        sizeText.addTextChangedListener(this);
        hostText.addTextChangedListener(this);
        phoneText.addTextChangedListener(this);
        typeText.addTextChangedListener(this);
        rTypeText.addTextChangedListener(this);


    }

    private void initView() {
        if (houseInfo!=null){
            titleView.setText("房屋信息");
//            topMenu.setText("...");
//            topMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP,48);
            addressText.setText(houseInfo.getAddress());
            if (houseInfo.getFloor()!=0){
                floorText.setText(houseInfo.getFloor()+"");
            }
            if (houseInfo.getDecoration()!=null){
                decoText.setText(houseInfo.getDecoration());
            }
            layoutText.setText(houseInfo.getLayout());
            sizeText.setText(houseInfo.getSize().toString()+"m²");
            hostText.setText(houseInfo.getAddressBook().getName());
            phoneText.setText(houseInfo.getAddressBook().getPhone_number());
            typeText.setText(houseInfo.getH_type());
            if (houseInfo.getH_type().equals("出租")){
                rTypeText.setText(houseInfo.getRent_type());
            }else {
                rrrrText.setVisibility(View.GONE);
                rTypeText.setVisibility(View.GONE);
            }
            addressText.setEnabled(false);
            floorText.setEnabled(false);
            decoText.setEnabled(false);
            layoutText.setEnabled(false);
            sizeText.setEnabled(false);
            hostText.setEnabled(false);
            phoneText.setEnabled(false);
            typeText.setEnabled(false);
            rTypeText.setEnabled(false);
            topMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu();
                }
            });
        }else {
            titleView.setText("添加房屋");
            topMenu.setText("确定");
            priceText.setText("价格:");
            phoneText.setHint("价格");
            topMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            topMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!address.isEmpty()&&!floor.isEmpty()&&!decoration.isEmpty()
                            &&!layout.isEmpty()&&!size.isEmpty()&&!host.isEmpty()&&!type.isEmpty()){
                        if (type.equals("出租")&&rType.isEmpty()){
                            rTypeText.setError("请输入出租类型");
                        }if (!type.equals("出租")&&!type.equals("出售")){
                            typeText.setError("请输入正确的房屋类型");
                        }if (!rType.equals("整租")&&!rType.equals("单间")&&!rType.equals("合租")){
                            rTypeText.setError("请输入正确的出租方式");
                        }if (!decoration.equals("精装")&&!decoration.equals("平装")&&!decoration.equals("毛坯")){
                            decoText.setError("请输入正确的装修类型");
                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    AddressBookController acon = new AddressBookController();
                                    HouseController hcon = new HouseController();
                                    ObjectMapper mapper = new ObjectMapper();
                                    int status=1;
                                    Message msg = Message.obtain();
                                    Bundle data = new Bundle();
                                    HouseInfo houseInfo = new HouseInfo();
                                    houseInfo.setH_id(1);
                                    houseInfo.setAddress(address);
                                    houseInfo.setFloor(Integer.parseInt(floor));
                                    houseInfo.setDecoration(decoration);
                                    houseInfo.setLayout(layout);
                                    houseInfo.setSize(new BigDecimal(Float.parseFloat(size)));
                                    houseInfo.setH_type(type);
                                    houseInfo.setSell_status("上架中");
                                    if (type.equals("出租")){
                                        houseInfo.setRent_type(rType);
                                        houseInfo.setHouse_status("待租");
                                        houseInfo.setR_price(new BigDecimal(Float.parseFloat(price)));
                                        houseInfo.setS_price(new BigDecimal(0));
                                    }
                                    if (type.equals("出售")){
                                        houseInfo.setRent_type(null);
                                        houseInfo.setHouse_status("待售");
                                        houseInfo.setS_price(new BigDecimal(Float.parseFloat(price)));
                                        houseInfo.setR_price(new BigDecimal(0));
                                    }
                                    AddressBook addressBook = null;
                                    try {
                                        addressBook =acon.getSingleFromServer("/getPersonByID.json","id",host);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (addressBook!=null){
                                        data.putSerializable("AddressBook",addressBook);
                                        data.putString("TAG","1");
                                        msg.setData(data);
                                        handler.sendMessage(msg);
                                    }else {
                                        Looper.prepare();
                                        Toast toast=Toast.makeText(getActivity(),"不存在该房东，请添加至通讯录后重试！",Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        Looper.loop();
                                    }
                                    houseInfo.setAddressBook(houseHost);
                                    onResume();
                                    if (TAG==1){
                                        Log.e("TAG--------",TAG+"");
                                        try {
                                            status = hcon.getStatusFromServer("addHouse.json","houseInfo",mapper.writeValueAsString(houseInfo));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if(status==0){
                                        Looper.prepare();
                                        Toast toast=Toast.makeText(getActivity(),"添加成功！",Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        Looper.loop();
                                    }else {
                                        Looper.prepare();
                                        Toast toast=Toast.makeText(getActivity(),"添加失败！请重试",Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        Looper.loop();
                                    }
                                }
                            }).start();
                        }

                    }else {
                        Toast toast=Toast.makeText(getActivity(),"请输入完整信息后重试！",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            });
        }


    }

    private void popupMenu() {
        PopupMenu pop = new PopupMenu(getActivity(),topMenu);
        MenuInflater inflater = pop.getMenuInflater();
        inflater.inflate(R.menu.housemenu,pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.house_del_item:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                HouseController hcon = new HouseController();
                                int status = 1;
                                int number = houseInfo.getH_id();
                                try {
                                    status = hcon.getStatusFromServer("deleteByID.json","id",number+"");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (status==0){
                                    Looper.prepare();
                                    Toast toast = Toast.makeText(getActivity(),"删除成功！",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    Looper.loop();
                                }else {
                                    Looper.prepare();
                                    Toast toast = Toast.makeText(getActivity(),"请重试！",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    Looper.loop();
                                }
                            }
                        }).start();
                        return true;
                    case R.id.hou_order_item:
//                        if (TAG==1){
                            OrderContentFragment ocf = OrderContentFragment.newInstance(houseInfo,user,null);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.content,ocf);
                            transaction.addToBackStack(null);
                            transaction.commit();
//                        }
                        return true;
                    default:
                        return false;
                }

            }
        });
        pop.show();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            AddressBook addressBook = (AddressBook) data.getSerializable("AddressBook");
//            user = (Usr) data.getSerializable("Usr");
////            TAG = Integer.parseInt(data.getString("TAG"));
            TAG = Integer.parseInt(data.getString("TAG"));
            Log.e("TAG",TAG+"");
            houseHost =addressBook;

        }
    };

    private void initData() {
        user = (Usr) getArguments().getSerializable("Usr");
        Log.e("houseUser-----",user.getLogin_name());
        ArrayList list = getArguments().getParcelableArrayList("houseInfo");
        if (list!=null){
            houseInfo = (HouseInfo) list.get(0);
        }
        for (int i =0;i<10;i++){
            imgList.add("1");
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
        address = addressText.getText().toString();
        floor = floorText.getText().toString();
        decoration = decoText.getText().toString();
        layout = layoutText.getText().toString();
        size = sizeText.getText().toString();
        host = hostText.getText().toString();
        price = phoneText.getText().toString();
        type = typeText.getText().toString();
        rType = rTypeText.getText().toString();
        if (type.equals("出售")){
            rrrrText.setVisibility(View.GONE);
            rTypeText.setVisibility(View.GONE);
        }else {
            rrrrText.setVisibility(View.VISIBLE);
            rTypeText.setVisibility(View.VISIBLE);
        }
    }
}
