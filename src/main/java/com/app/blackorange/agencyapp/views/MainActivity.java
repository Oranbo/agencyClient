package com.app.blackorange.agencyapp.views;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.models.Usr;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //tab
    private LinearLayout tabHouse;
    private LinearLayout tabAddressBook;
    private LinearLayout tabPersonal;

    //imageButton
    private ImageButton houseButton;
    private ImageButton abButton;
    private ImageButton perButton;

    //fragment
    private Fragment houseTab;
    private Fragment abTab;
    private Fragment perTab;
    private Fragment houseContent;

    private Usr user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        user = (Usr) intent.getSerializableExtra("user");
        Log.e("user------------",user.getLogin_name());
        initView();
        initEvent();

    }

    private void initEvent() {
        tabHouse.setOnClickListener(this);
        tabAddressBook.setOnClickListener(this);
        tabPersonal.setOnClickListener(this);


    }


    private void initView() {

        tabHouse = (LinearLayout) findViewById(R.id.tab_house);
        tabAddressBook = (LinearLayout) findViewById(R.id.tab_addressBook);
        tabPersonal = (LinearLayout) findViewById(R.id.tab_personal);

        houseButton = (ImageButton) findViewById(R.id.tab_house_img);
        abButton = (ImageButton) findViewById(R.id.tab_addressBook_img);
        perButton = (ImageButton) findViewById(R.id.tab_personal_img);

//       getSupportFragmentManager().beginTransaction().replace(R.id.content,houseTab);
        setSelected(0);
    }

    @Override
    public void onClick(View v) {

       resetImg();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId())
        {
            case R.id.tab_house:
                setSelected(0);
                houseButton.setImageResource(R.mipmap.house_img);
                break;
            case R.id.tab_addressBook:
                setSelected(1);
                abButton.setImageResource(R.mipmap.address_book);
                break;
            case R.id.tab_personal:
//                setSelected(2);
                SelfInfoFragment sf = SelfInfoFragment.newInstance(user);
                transaction.replace(R.id.content,sf);
                transaction.addToBackStack(null);
                transaction.commit();
                perButton.setImageResource(R.mipmap.self_img);
                break;
            default:
                break;
        }

    }

    private void resetImg() {
        houseButton.setImageResource(R.mipmap.house_img_pressed);
        abButton.setImageResource(R.mipmap.address_book_pressed);
        perButton.setImageResource(R.mipmap.self_img_pressed);
    }


    public void setSelected(int selected) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
//        HouseListFragment hlf=null;
        switch(selected)
        {
            case 0:
                if (houseTab==null) {
                    houseTab = HouseListFragment.newInstance(user);
                    transaction.add(R.id.content,houseTab);
                }else {
//                    transaction.show(houseTab);
                }
                transaction.show(houseTab);
                transaction.replace(R.id.content,houseTab);
                transaction.addToBackStack(null);
                break;
            case 1:
                if (abTab == null){
                    abTab = ABookListFragment.newInstance();
                    transaction.add(R.id.content,abTab);
                }else {
//                    transaction.show(abTab);
                }
                transaction.show(abTab);
                transaction.replace(R.id.content,abTab);
                break;
            case 2:
//                if (perTab==null){
//                    perTab = new SelfInfoFragment();
//                    transaction.add(R.id.content,perTab);
//                }else {
////                    transaction.show(perTab);
//                }
//                transaction.show(perTab);
//                transaction.replace(R.id.content,perTab);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {

        if (houseTab != null){
            transaction.hide(houseTab);
        }
        if (abTab != null){
            transaction.hide(abTab);
        }
        if (perTab != null){
            transaction.hide(perTab);
        }
    }
}
