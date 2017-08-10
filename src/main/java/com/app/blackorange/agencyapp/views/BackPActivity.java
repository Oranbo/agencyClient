package com.app.blackorange.agencyapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.app.blackorange.agencyapp.utils.OkHttpUtil;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by BlackOrange on 2017/5/20.
 */

public class BackPActivity extends AppCompatActivity implements TextWatcher {

    private static int TAG=0;
    private TextView titleText;
    private TextView topMenu;
    private TextView loginNameText;
    private TextView realNameText;
    private TextView phoneText;
    private Button backButton;
    private String loginName;
    private String realName;
    private String phone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        initData();
        setContentView(R.layout.back_password_layout);
        titleText = (TextView) findViewById(R.id.title_textView);
        topMenu = (TextView) findViewById(R.id.top_menu);
        loginNameText = (TextView) findViewById(R.id.back_loginName);
        realNameText = (TextView) findViewById(R.id.back_realName);
        phoneText = (TextView) findViewById(R.id.back_phone);
        backButton = (Button) findViewById(R.id.back_button);
        initView();
        initEvents();
    }


    private void initEvents() {
        loginNameText.addTextChangedListener(this);
        realNameText.addTextChangedListener(this);
        phoneText.addTextChangedListener(this);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loginName.isEmpty()&&!realName.isEmpty()&&!phone.isEmpty()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            Bundle data = new Bundle();
                            int status =-1;
                            UserController ucon = new UserController();
                            try {
                                status = ucon.getBackStatus(loginName,realName,phone);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (status==0){
                                data.putString("TAG","1");
                                msg.setData(data);
                                handler.sendMessage(msg);
                                Looper.prepare();
                                Toast toast=Toast.makeText(getApplicationContext(),"信息验证成功，密码已发送至您的手机！",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Looper.loop();
                            }else {
                                Looper.prepare();
                                Toast toast=Toast.makeText(getApplicationContext(),"请确认输入信息正确后重试",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Looper.loop();
                            }
                        }
                    }).start();
                }else {
                    if (loginName.isEmpty()){
                        loginNameText.setError("请输入用户名");
                    }if (realName.isEmpty()){
                        realNameText.setError("请输入真实姓名");
                    }if (phone.isEmpty()){
                        phoneText.setError("请输入电话号码");
                    }
                }
            }
        });
    }
    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // Activity跳转
            Bundle data = msg.getData();
            TAG=Integer.parseInt(data.getString("TAG"));
            if (TAG==1){
                Intent intent = new Intent(BackPActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }
    };

    private void initView() {
        titleText.setText("密码找回");
        topMenu.setVisibility(View.GONE);
        if (loginName!=null&&!loginName.equals("none")){
            loginNameText.setText(loginName);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        loginName =intent.getStringExtra("name");

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        loginName =loginNameText.getText().toString();
        realName = realNameText.getText().toString();
        phone = phoneText.getText().toString();
    }
}
