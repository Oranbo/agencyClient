package com.app.blackorange.agencyapp.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by BlackOrange on 2017/4/18.
 */

public class BusinessOrder implements Serializable {

    private int b_id;
    private Usr employee;
    private AddressBook addressBook2;
    private HouseInfo houseInfo;
    private String b_date;
    private String b_type;
    private BigDecimal real_price;

    public int getB_id() {
        return b_id;
    }

    public void setB_id(int b_id) {
        this.b_id = b_id;
    }

    public Usr getEmployee() {
        return employee;
    }

    public void setEmployee(Usr employee) {
        this.employee = employee;
    }

    public AddressBook getAddressBook2() {
        return addressBook2;
    }

    public void setAddressBook2(AddressBook addressBook2) {
        this.addressBook2 = addressBook2;
    }

    public HouseInfo getHouseInfo() {
        return houseInfo;
    }

    public void setHouseInfo(HouseInfo houseInfo) {
        this.houseInfo = houseInfo;
    }

    public String getB_date() {
        return b_date;
    }

    public void setB_date(String b_date) {
        this.b_date = b_date;
    }

    public String getB_type() {
        return b_type;
    }

    public void setB_type(String b_type) {
        this.b_type = b_type;
    }

    public BigDecimal getReal_price() {
        return real_price;
    }

    public void setReal_price(BigDecimal real_price) {
        this.real_price = real_price;
    }
}
