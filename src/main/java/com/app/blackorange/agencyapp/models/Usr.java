package com.app.blackorange.agencyapp.models;

import java.io.Serializable;

/**
 * Created by BlackOrange on 2017/4/18.
 */

public class Usr implements Serializable {
    private int id;
    private String login_name;
    private String passwd;
    private AddressBook addressBook;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public AddressBook getAddressBook() {
        return addressBook;
    }

    public void setAddressBook(AddressBook addressBook) {
        this.addressBook = addressBook;
    }
}
