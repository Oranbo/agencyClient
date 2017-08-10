package com.app.blackorange.agencyapp;

import com.app.blackorange.agencyapp.controllers.AddressBookController;
import com.app.blackorange.agencyapp.controllers.HouseController;
import com.app.blackorange.agencyapp.controllers.UserController;
import com.app.blackorange.agencyapp.models.AddressBook;
import com.app.blackorange.agencyapp.models.HouseInfo;
import com.app.blackorange.agencyapp.utils.OkHttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
//    @Test
//    public void testLogin()  {
//        UserController userController = new UserController();
//        int status= 0;
//        try {
////            status = userController.loginStatus("login.json","aoe","221131");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(status);
//    }
    @Test
    public void testGetAllHouse() throws IOException {
        HouseController houseController = new HouseController();
        ArrayList<HouseInfo> houseInfos = houseController.getFromServer("/getByAddress.json","address","一单元");
        for (HouseInfo h:houseInfos
             ) {
            System.out.println(h.getAddress());
        }
    }
    @Test
    public void testGetAllPeople() throws IOException {
        AddressBookController addressBookController = new AddressBookController();
        ArrayList<AddressBook> addressBooks = addressBookController.getAllPeople();
        for (AddressBook a:addressBooks
                ) {
            System.out.println("name:"+a.getName());
        }

    }
    @Test
    public void testGetByID() throws IOException {
        AddressBookController addressBookController = new AddressBookController();
        AddressBook a = addressBookController.getSingleFromServer("/getPersonByID.json","id","226");
//
//            System.out.println("name:"+a.getName());
        HouseController hcon = new HouseController();
        HouseInfo houseInfo = new HouseInfo();
        houseInfo.setFloor(1);
        houseInfo.setDecoration("精装");
        houseInfo.setAddressBook(a);
        System.out.println("-----------"+a.getName());
        houseInfo.setH_type("出售");
        houseInfo.setH_id(1);
        houseInfo.setSize(new BigDecimal(120));
        houseInfo.setLayout("三室一厅");
        houseInfo.setHouse_status("待售");
        houseInfo.setS_price(new BigDecimal(1400000));
        houseInfo.setR_price(new BigDecimal(0));
        houseInfo.setAddress("重庆大学A区");
        ObjectMapper mapper = new ObjectMapper();
        UserController ucon = new UserController();
        String str1 = new String(mapper.writeValueAsString(houseInfo).getBytes(),"utf-8");
        List params = new LinkedList();
        HashMap<String,Object> map = new HashMap<>();
        map.put("houseInfo",str1);
        OkHttpUtil h = new OkHttpUtil();
        int s=hcon.getStatusFromServer("addHouse.json","houseInfo",str1);

        System.out.println(s);;


    }
}