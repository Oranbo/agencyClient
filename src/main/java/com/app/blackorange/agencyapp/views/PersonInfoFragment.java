package com.app.blackorange.agencyapp.views;

import android.os.Bundle;
import android.os.Looper;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.controllers.AddressBookController;
import com.app.blackorange.agencyapp.models.AddressBook;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.app.blackorange.agencyapp.R.id.num_layout;

/**
 * Created by BlackOrange on 2017/5/15.
 */

public class PersonInfoFragment extends Fragment implements TextWatcher{

    private View numLayout;
    private TextView headImg;
    private TextView typeView;
    private TextView titleText;
    private TextView topMenu;
    private TextView numberText;
    private TextView nameText;
    private TextView sexText;
    private TextView phoneText;
    private TextView addressText;
    private Spinner typeSpinner;
    private AddressBook addressBook;
    private String number;
    private List<String> types = new ArrayList<String>();
    private ArrayAdapter<String> typeAdapter;
    private String name;
    private String type;
    private String sex;
    private String phone;
    private String address;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.self_center_layout, null);
        types.add("员工");
        types.add("客户");
        typeSpinner = (Spinner) view.findViewById(R.id.p_type_spinner);
        typeAdapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_item,types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        numLayout = view.findViewById(R.id.num_layout);
        headImg = (TextView) view.findViewById(R.id.head_picture);
        typeView = (TextView) view.findViewById(R.id.p_type_view);
        numberText = (TextView) view.findViewById(R.id.number_view);
        titleText = (TextView) view.findViewById(R.id.title_textView);
        topMenu = (TextView) view.findViewById(R.id.top_menu);
//        numText = (TextView) view.findViewById(R.id.number_text);
        nameText = (TextView) view.findViewById(R.id.name_view);
        sexText = (TextView) view.findViewById(R.id.sex_view);
        phoneText = (TextView) view.findViewById(R.id.phone_view);
        addressText = (TextView) view.findViewById(R.id.address_view);
        initEvents();
        initView();
        return view;
    }

    private void initEvents() {
        nameText.addTextChangedListener(this);
        sexText.addTextChangedListener(this);
        phoneText.addTextChangedListener(this);
        addressText.addTextChangedListener(this);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = typeAdapter.getItem(position);
                typeView.setText(str);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                parent.setVisibility(View.VISIBLE);

            }
        });
        typeSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        typeSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
    }

    private void initView() {
        if (addressBook!=null){
            titleText.setText("联系人信息");
            typeView.setText(addressBook.getA_type());
            numberText.setText(addressBook.getA_id()+"");
            nameText.setText(addressBook.getName());
            sexText.setText(addressBook.getSex());
            phoneText.setText(addressBook.getPhone_number());
            if (addressBook.getAddress()!=null){
                addressText.setText(addressBook.getAddress());
            }else {
                addressText.setText("未知");
            }
            typeView.setEnabled(false);
            numberText.setEnabled(false);
            nameText.setEnabled(false);
            sexText.setEnabled(false);
            phoneText.setEnabled(false);
            addressText.setEnabled(false);
            typeSpinner.setVisibility(View.GONE);
            topMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu();
                }
            });
        }else {
            titleText.setText("添加联系人");
            topMenu.setText("确定");
            topMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            numLayout.setVisibility(View.GONE);
            typeView.setEnabled(false);
            typeView.setVisibility(View.GONE);
            topMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("type",type);
                    if (!name.isEmpty()&&!type.isEmpty()&&type.equals("客户")&&!sex.isEmpty()&&!phone.isEmpty()){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AddressBookController acon = new AddressBookController();
                                AddressBook people = new AddressBook();
                                ObjectMapper mapper = new ObjectMapper();
                                int status =1;
                                people.setName(name);
                                people.setA_type(type);
                                people.setSex(sex);
                                people.setPhone_number(phone);
                                people.setAddress(address);
                                try {
                                    status =acon.getStatusFromServer("addPerson.json","personInfo",mapper.writeValueAsString(people));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Looper.prepare();
                                Toast toast = Toast.makeText(getActivity(),"添加成功！",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Looper.loop();
                            }
                        }).start();
                    }else {
                        if (name.isEmpty()){
                            nameText.setError("请输入姓名");
                        }if (type.isEmpty()){
                            typeView.setError("请输入类型");
                        }if (type.equals("员工")){
                            Toast toast = Toast.makeText(getActivity(),"您无权添加员工信息！",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            onResume();
                        }if (phone.isEmpty()){
                            phoneText.setError("请输入电话号码");
                        }
                    }

                }
            });
        }
    }

    private void popupMenu() {
        PopupMenu pop = new PopupMenu(getActivity(),topMenu);
        MenuInflater inflater = pop.getMenuInflater();
        inflater.inflate(R.menu.abcmenu,pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.abc_cha_item:
                        if (type.equals("员工")){
                            Toast toast = Toast.makeText(getActivity(),"您无权修改其他员工信息！",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
//                        typeView.setEnabled(true);
                        numberText.setEnabled(true);
                        nameText.setEnabled(true);
                        sexText.setEnabled(true);
                        phoneText.setEnabled(true);
                        addressText.setEnabled(true);
                        topMenu.setText("确定");
                        topMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                        numLayout.setVisibility(View.GONE);
                        topMenu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!name.isEmpty()&&!type.isEmpty()&&type.equals("客户")&&!sex.isEmpty()&&!phone.isEmpty()){
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AddressBookController acon = new AddressBookController();
                                            AddressBook people = new AddressBook();
                                            ObjectMapper mapper = new ObjectMapper();
                                            int status =1;
                                            people.setA_id(addressBook.getA_id());
                                            people.setName(name);
                                            people.setA_type(type);
                                            people.setSex(sex);
                                            people.setPhone_number(phone);
                                            people.setAddress(address);
                                            try {
                                                status =acon.getStatusFromServer("updatePersonInfo.json","personInfo",mapper.writeValueAsString(people));
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (status==0){
                                                Looper.prepare();
                                                Toast toast = Toast.makeText(getActivity(),"修改成功！",Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                                Looper.loop();
                                            }
                                        }
                                    }).start();
                                }else {
                                    if (name.isEmpty()){
                                        nameText.setError("请输入姓名");
                                    }if (type.isEmpty()){
                                        typeView.setError("请输入类型");
                                    }if (phone.isEmpty()){
                                        phoneText.setError("请输入电话号码");
                                    }
                                }

                            }
                        });
                        return true;
                    case R.id.ab_del_item:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AddressBookController acon = new AddressBookController();
                                AddressBook people = new AddressBook();
                                ObjectMapper mapper = new ObjectMapper();
                                int status =1;
                                people.setA_id(addressBook.getA_id());
                                people.setName(name);
                                people.setA_type(type);
                                people.setSex(sex);
                                people.setPhone_number(phone);
                                people.setAddress(address);
                                try {
                                    status =acon.getStatusFromServer("deletePeopleById.json","id",addressBook.getA_id()+"");
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
                                    Toast toast = Toast.makeText(getActivity(),"该客户是房东，仍有房源上架中，无法删除！",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    Looper.loop();
                                }
                            }
                        }).start();
                        return true;
                    default:
                        return false;
                }
            }
        });

        pop.show();
    }


    private void initData() {
        addressBook= (AddressBook) getArguments().getSerializable("AddressBook");
    }

    public static PersonInfoFragment newInstance(AddressBook personInfo) {

        Bundle args = new Bundle();

        PersonInfoFragment fragment = new PersonInfoFragment();
        args.putSerializable("AddressBook",personInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        type =typeView.getText().toString();
        name = nameText.getText().toString();
        sex = sexText.getText().toString();
        phone = phoneText.getText().toString();
        address = addressText.getText().toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        type =typeView.getText().toString();
        name = nameText.getText().toString();
        sex = sexText.getText().toString();
        phone = phoneText.getText().toString();
        address = addressText.getText().toString();
    }
}
