package com.example.myhelper.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by Administrator on 2019/1/4.
 */

public class Customer extends LitePalSupport {
    @Column(unique = true)
    private String name;
    private String wx;
    private String phone;
    private int level;//代理级别
    private String address;//收货地址

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    private String other;//备注

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
