package com.app.blackorange.agencyapp.controllers;

import com.app.blackorange.agencyapp.models.AddressBook;
import com.app.blackorange.agencyapp.utils.OkHttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by BlackOrange on 2017/5/14.
 */

public class AddressBookController {
    public ArrayList<AddressBook> getAllPeople() throws IOException {
        String url = "getAllPeople.json";
        String jsonResult = OkHttpUtil.getStringFromServer(url);
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<AddressBook> addressBooks = mapper.readValue(jsonResult,new TypeReference<ArrayList<AddressBook>>() {
        });
        return addressBooks;
    }
    public ArrayList<AddressBook> getFromServer(String url,String param,String value) throws IOException {
        String builder = OkHttpUtil.attachHttpGetParam(url, param, value);
        String jsonResult = OkHttpUtil.getStringFromServer(builder);
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<AddressBook> addressBooks= mapper.readValue(jsonResult, new TypeReference<ArrayList<AddressBook>>() {
        });
        return addressBooks;
    }
    public AddressBook getSingleFromServer(String url,String param,String value) throws IOException {
        String builder = OkHttpUtil.attachHttpGetParam(url, param, value);
        String jsonResult = OkHttpUtil.getStringFromServer(builder);
        ObjectMapper mapper = new ObjectMapper();
        AddressBook addressBook= mapper.readValue(jsonResult, new TypeReference<AddressBook>() {
        });
        return addressBook;
    }
    public int getStatusFromServer(String url, String param, String value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = OkHttpUtil.post(url,param,value);
        Map<String,Integer> result = mapper.readValue(jsonResult, new TypeReference<Map<String,Integer>>() { });
        int status = result.get("status");
        return  status;
    }
}
