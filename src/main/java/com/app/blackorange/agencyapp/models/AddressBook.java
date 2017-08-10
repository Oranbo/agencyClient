package com.app.blackorange.agencyapp.models;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * Created by BlackOrange on 2017/4/18.
 */

public class AddressBook implements Serializable {

    private int a_id;
    private String a_type;
    private String name;
    private String sex;
    private String phone_number;
    private String address;
    private byte[] head_pictures;


    public void setA_id(int a_id) {
        this.a_id = a_id;
    }


    public void setA_type(String a_type) {
        this.a_type = a_type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHead_pictures(byte[] head_pictures) {
        this.head_pictures = head_pictures;
    }

    public int getA_id() {
        return a_id;
    }

    public String getA_type() {
        return a_type;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getAddress() {
        return address;
    }

    public byte[] getHead_pictures() {
        return head_pictures;
    }
}
