package com.example.myhelper.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;


/**
 * Created by Administrator on 2019/1/4.
 */

public class Product extends LitePalSupport implements Serializable {

    @Column(unique = true)
    private String name;
    private double costPrice;//成本价
    private double memberPrice;//会员价
    private double vipPrice;//贵宾价
    private double silverPrice;//银钻价
    private double goldPrice;//金钻价
    private double retailPrice;//零售价
//    private int wanrCount;//预警数量
    private String other;//功效

    public double getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(double memberPrice) {
        this.memberPrice = memberPrice;
    }

    public double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public double getSilverPrice() {
        return silverPrice;
    }

    public void setSilverPrice(double silverPrice) {
        this.silverPrice = silverPrice;
    }

    public double getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(double goldPrice) {
        this.goldPrice = goldPrice;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    private String photoPath;//图片路径
    private int count;//库存数量
    private String date;//出入库日期

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }



    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }
}
