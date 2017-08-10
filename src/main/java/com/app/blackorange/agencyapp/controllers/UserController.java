package com.app.blackorange.agencyapp.controllers;

import com.app.blackorange.agencyapp.models.Usr;
import com.app.blackorange.agencyapp.utils.OkHttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by BlackOrange on 2017/4/18.
 */

public class UserController {

    public int getStatus(String url,String name,String password) throws IOException {
//        Map<String,Object> temp = new HashMap<>();
        List params = new LinkedList();
        params.add(new BasicNameValuePair("userName",name));
        params.add(new BasicNameValuePair("passWord",password));
        String jsonResult = OkHttpUtil.getStringFromServer(url,params);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Integer> result = mapper.readValue(jsonResult, new TypeReference<Map<String,Integer>>() { });
        int status = result.get("status");
        return  status;
    }

    public int getBackStatus(String loginName,String realName,String phone_number) throws IOException {
//        Map<String,Object> temp = new HashMap<>();
        List params = new LinkedList();
        params.add(new BasicNameValuePair("loginName",loginName));
        params.add(new BasicNameValuePair("realName",realName));
        params.add(new BasicNameValuePair("phone_number",phone_number));
        String jsonResult = OkHttpUtil.getStringFromServer("getBackPassword.json",params);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Integer> result = mapper.readValue(jsonResult, new TypeReference<Map<String,Integer>>() { });
        int status = result.get("status");
        return  status;
    }

    public Usr getFromServer(String url,String param,String value) throws IOException {
        String builder = OkHttpUtil.attachHttpGetParam(url, param, value);
        String jsonResult = OkHttpUtil.getStringFromServer(builder);
        ObjectMapper mapper = new ObjectMapper();
        Usr user= mapper.readValue(jsonResult, new TypeReference<Usr>() {
        });
        return user;
    }
    public int post(String url, String param, String value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = OkHttpUtil.post(url,param,value);
        Map<String,Integer> result = mapper.readValue(jsonResult, new TypeReference<Map<String,Integer>>() { });
        int status = result.get("status");
        return  status;
    }

}
