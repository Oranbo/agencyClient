package com.app.blackorange.agencyapp.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by BlackOrange on 2017/4/18.
 */

public class HouseInfo implements Serializable {
    private int h_id;
    private AddressBook addressBook;
    private BigDecimal size;
    private String address;
    private String layout;
    private String h_type;
    private String house_status;
    private BigDecimal r_price;
    private BigDecimal s_price;
    private String h_pictures;
    private int floor;
    private String decoration;
    private String rent_type;
    private String sell_status;

    public int getH_id() {
        return h_id;
    }

    public void setH_id(int h_id) {
        this.h_id = h_id;
    }

    public AddressBook getAddressBook() {
        return addressBook;
    }

    public void setAddressBook(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getH_type() {
        return h_type;
    }

    public void setH_type(String h_type) {
        this.h_type = h_type;
    }

    public String getHouse_status() {
        return house_status;
    }

    public void setHouse_status(String house_status) {
        this.house_status = house_status;
    }

    public BigDecimal getR_price() {
        return r_price;
    }

    public void setR_price(BigDecimal r_price) {
        this.r_price = r_price;
    }

    public BigDecimal getS_price() {
        return s_price;
    }

    public void setS_price(BigDecimal s_price) {
        this.s_price = s_price;
    }

    public String getH_pictures() {
        return h_pictures;
    }

    public void setH_pictures(String h_pictures) {
        this.h_pictures = h_pictures;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration;
    }

    public String getRent_type() {
        return rent_type;
    }

    public void setRent_type(String rent_type) {
        this.rent_type = rent_type;
    }

    public String getSell_status() {
        return sell_status;
    }

    public void setSell_status(String sell_status) {
        this.sell_status = sell_status;
    }
}
