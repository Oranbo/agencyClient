package com.app.blackorange.agencyapp.controllers;

import com.app.blackorange.agencyapp.models.HouseInfo;
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
 * Created by BlackOrange on 2017/5/13.
 */

public class HouseController {
    public ArrayList<HouseInfo> getAllHouses() throws IOException {
        final String URL = "/getAllHouses.json";
        String jsonResult = OkHttpUtil.getStringFromServer(URL);
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<HouseInfo> houseInfos= mapper.readValue(jsonResult, new TypeReference<ArrayList<HouseInfo>>() {
        });
        return houseInfos;
    }
    public ArrayList<HouseInfo> getFromServer(String url,String param,String value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String builder = OkHttpUtil.attachHttpGetParam(url, param, value);
        String jsonResult = OkHttpUtil.getStringFromServer(builder);
        ArrayList<HouseInfo> houseInfos= mapper.readValue(jsonResult, new TypeReference<ArrayList<HouseInfo>>() {
        });
        return houseInfos;
    }
    public int getStatusFromServer(String url, String param, String value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = OkHttpUtil.post(url,param,value);
        Map<String,Integer> result = mapper.readValue(jsonResult, new TypeReference<Map<String,Integer>>() { });
        int status = result.get("status");
        return  status;
    }
}
