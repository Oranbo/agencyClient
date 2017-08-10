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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.controllers.UserController;
import com.app.blackorange.agencyapp.models.Usr;
import com.app.blackorange.agencyapp.utils.OkHttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by BlackOrange on 2017/5/22.
 */

public class ChangePwFragment extends Fragment implements TextWatcher{
    private static int TAG=0;
    private TextView titleView;
    private TextView topMenu;
    private TextView oldPwView;
    private TextView newPwView;
    private TextView sureView;
    private Button sureButton;
    private Usr user;
    private String oldPw;
    private String newPw;
    private String rPw;

    public static ChangePwFragment newInstance(Usr user) {

        Bundle args = new Bundle();

        ChangePwFragment fragment = new ChangePwFragment();
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
        View view = inflater.inflate(R.layout.cha_pw_layout, null);
        titleView = (TextView) view.findViewById(R.id.title_textView);
        topMenu = (TextView) view.findViewById(R.id.top_menu);
        oldPwView = (TextView) view.findViewById(R.id.old_pw);
        newPwView = (TextView) view.findViewById(R.id.new_pw);
        sureView = (TextView) view.findViewById(R.id.sure_pw);
        sureButton = (Button) view.findViewById(R.id.change_sure_button);
        initView();
        initEvents();
        return view;
    }

    private void initEvents() {
        oldPwView.addTextChangedListener(this);
        newPwView.addTextChangedListener(this);
        sureView.addTextChangedListener(this);
        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldPassword = user.getPasswd();
                Log.e("newPassqord",newPw.toString());
                if (!oldPw.isEmpty()&&!newPw.isEmpty()&&!rPw.isEmpty()){
                    if (newPw.equals(rPw)&&oldPw.equals(oldPassword)){

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = Message.obtain();
                                Bundle data = new Bundle();
                                UserController ucon = new UserController();
                                user.setPasswd(newPw);
                                ObjectMapper mapper = new ObjectMapper();
                                int status=-1;
                                Log.e("password",oldPassword+"");
                                try {
                                    status = ucon.post("/updateUsrInfo.json","user",mapper.writeValueAsString(user));
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
                    } else if (!newPw.equals(rPw)){
                        Toast toast = Toast.makeText(getActivity(),"两次密码不一致！",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                        Toast toast = Toast.makeText(getActivity(),"密码错误！",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }else {

//                    Toast toast = Toast.makeText(getActivity(),"请输入完整信息！",Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                    onResume();
                    if (oldPw.isEmpty()){
                        oldPwView.setError("请输入密码");
                    }if (newPw.isEmpty()){
                        newPwView.setError("请输入新密码");
                    }if (rPw.isEmpty()){
                        sureView.setError("请重复输入密码");
                    }
                }

            }
        });

    }

    private void initData() {
        user = (Usr) getArguments().getSerializable("Usr");
    }

    private void initView() {
        titleView.setText("修改密码");
        topMenu.setVisibility(View.GONE);
        if (TAG==1){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            SelfInfoFragment sf = SelfInfoFragment.newInstance(user);
            transaction.replace(R.id.content,sf);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            TAG = Integer.parseInt(data.getString("TAG"));
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
        oldPw = oldPwView.getText().toString();
        newPw = newPwView.getText().toString();
        rPw = sureView.getText().toString();
    }
}
