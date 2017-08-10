package com.app.blackorange.agencyapp.views;

import android.graphics.Color;
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
import com.app.blackorange.agencyapp.controllers.AddressBookController;
import com.app.blackorange.agencyapp.models.AddressBook;
import com.app.blackorange.agencyapp.utils.SortUtil;
import com.app.blackorange.agencyapp.views.adapter.AddressBookAdapter;
import com.app.blackorange.agencyapp.views.adapter.IndexAdapter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by BlackOrange on 2017/5/14.
 */

public class ABookListFragment extends Fragment {
    private AddressBookAdapter addressBookAdapter;
    private IndexAdapter indexAdapter;
    private ArrayList<AddressBook> bookArrayList = new ArrayList<>(15);
    private ArrayList<String> indexList = new ArrayList<>();
    private EditText searchText;
    private ImageButton searchButton;
    private ListView personListView;
    private ListView indexListView;
    private String searchIndex;
    private TextView titleView;
    private TextView topMenu;
    private ArrayList<String> pinyinIndex = new ArrayList<>();

    public static ABookListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ABookListFragment fragment = new ABookListFragment();
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
        View v= inflater.inflate(R.layout.addressbook_list,null);
        titleView = (TextView) v.findViewById(R.id.title_textView);
        topMenu = (TextView) v.findViewById(R.id.top_menu);
        searchText = (EditText) v.findViewById(R.id.search_person_text);
        searchButton = (ImageButton) v.findViewById(R.id.search_person_button);
        personListView = (ListView) v.findViewById(R.id.person_list);
        indexListView = (ListView) v.findViewById(R.id.index_list);
        indexListView.setBackgroundColor(Color.parseColor("#ffaad5"));
        initEvents();
        indexAdapter = new IndexAdapter(getActivity(),R.layout.simple_item,indexList);
        indexListView.setAdapter(indexAdapter);
        return v;
    }

    private void initView() {
        titleView.setText("通讯录列表");
        addressBookAdapter = new AddressBookAdapter(getActivity(),R.layout.simple_item,bookArrayList);
        personListView.setAdapter(addressBookAdapter);
    }

    private void initEvents() {
        topMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
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
        });
        personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressBook addressBook = (AddressBook) parent.getItemAtPosition(position);
                PersonInfoFragment pif = PersonInfoFragment.newInstance(addressBook);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content,pif);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        indexListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String str = (String) parent.getItemAtPosition(position);
                for (AddressBook a:bookArrayList
                     ) {
                    if (str.equals(SortUtil.converterToFirstSpell(a.getName()))){
                        int pos = bookArrayList.indexOf(a);
                        personListView.setSelection(pos);
                        personListView.getChildAt(pos).setBackgroundColor(Color.parseColor("#ffaad5"));
                    }
                }
            }

        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchIndex==null){
                    Toast toast = Toast.makeText(getActivity(),"请输入查找内容！",Toast.LENGTH_SHORT);
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
                            AddressBookController con = new AddressBookController();
                            ArrayList list = new ArrayList();
                            ArrayList<AddressBook> pList = null;
                            try {
                                pList = con.getFromServer("getPersonByName.json","name",searchIndex);
                            } catch (IOException e) {
                                Toast toast = Toast.makeText(getActivity(),searchIndex+"请重试！",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            if (pList.size()!=0){
                                list.add(pList);
                                data.putParcelableArrayList("bookList",list);
                                msg.setData(data);
                                handler.sendMessage(msg);
                            }else {
                                Looper.prepare();
                                Toast toast = Toast.makeText(getActivity(),"未找到"+searchIndex+"！",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Looper.loop();
                            }
                        }
                    }).start();
                }

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
        inflater.inflate(R.menu.abmenu,pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ab_add_item:
                        PersonInfoFragment pif = PersonInfoFragment.newInstance(null);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.content,pif);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
                    case R.id.ab_all_item:
                        initData();
                        return  true;
                    case R.id.ab_emp_item:
                        bookArrayList.clear();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = Message.obtain();
                                Bundle data = new Bundle();
                                AddressBookController con = new AddressBookController();
                                ArrayList list = new ArrayList();
                                ArrayList<AddressBook> pList = null;
                                try {
                                    pList = con.getFromServer("getPersonByType.json","type","员工");
                                } catch (IOException e) {
                                    Toast toast = Toast.makeText(getActivity(),"请重试！",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                                if (pList.size()!=0){
                                    list.add(pList);
                                    data.putParcelableArrayList("bookList",list);
                                    msg.setData(data);
                                    handler.sendMessage(msg);
                                }else {
//                                    Looper.prepare();
//                                    Toast toast = Toast.makeText(getActivity(),"未找到"+searchIndex+"！",Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                    Looper.loop();
                                }
                            }
                        }).start();
                        addressBookAdapter.notifyDataSetChanged();
                        onResume();
                        return true;
                    case R.id.ab_guest_item:
                        bookArrayList.clear();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = Message.obtain();
                                Bundle data = new Bundle();
                                AddressBookController con = new AddressBookController();
                                ArrayList list = new ArrayList();
                                ArrayList<AddressBook> pList = null;
                                try {
                                    pList = con.getFromServer("getPersonByType.json","type","客户");
                                } catch (IOException e) {
                                    Toast toast = Toast.makeText(getActivity(),"请重试！",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                                if (pList.size()!=0){
                                    list.add(pList);
                                    data.putParcelableArrayList("bookList",list);
                                    msg.setData(data);
                                    handler.sendMessage(msg);
                                }else {
                                }
                            }
                        }).start();
                        addressBookAdapter.notifyDataSetChanged();
                        onResume();
                        return true;
                    default:
                        return false;
                }
            }
        });

        pop.show();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            ArrayList list = data.getParcelableArrayList("bookList");
            bookArrayList = (ArrayList<AddressBook>) list.get(0);
            initView();
            indexList = indexArray();
        }
    };
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                ArrayList list = new ArrayList();
                ArrayList<AddressBook> addressBooks = null;
                AddressBookController con = new AddressBookController();
                try {
                    addressBooks = con.getAllPeople();
                } catch (IOException e) {

                }
                list.add(addressBooks);
                data.putParcelableArrayList("bookList",list);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }).start();
    }
    private ArrayList<String> indexArray() {
        ArrayList<String> index = new ArrayList<>();
        for (char i=97;i<123;i++){
            index.add(String.valueOf(i));
        }
        index.add("#");
        return index;
    }
}
