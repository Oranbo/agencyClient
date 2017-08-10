package com.app.blackorange.agencyapp.controllers;

import com.app.blackorange.agencyapp.models.BusinessOrder;
import com.app.blackorange.agencyapp.utils.OkHttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by BlackOrange on 2017/5/20.
 */

public class BusinessOrderController {
    public static int getStatusFromServer(String url, String param, String value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = OkHttpUtil.post(url,param,value);
        Map<String,Integer> result = mapper.readValue(jsonResult, new TypeReference<Map<String,Integer>>() { });
        int status = result.get("status");
        return  status;
    }
    public static ArrayList<BusinessOrder> getFromServer(String url, String param, String value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = OkHttpUtil.post(url,param,value);
        ArrayList<BusinessOrder> businessOrders = mapper.readValue(jsonResult, new TypeReference<ArrayList<BusinessOrder>>() { });
        return  businessOrders;
    }
}
