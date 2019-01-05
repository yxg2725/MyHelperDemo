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
    private double price2;//二级代理价
    private double price1;//一级代理价
    private double retailPrice;//零售价
    private int wanrCount;//预警数量
    private String effect;//功效
    private String photoPath;//图片路径
    private int count;//库存数量

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWanrCount() {
        return wanrCount;
    }

    public void setWanrCount(int wanrCount) {
        this.wanrCount = wanrCount;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
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

    public double getPrice2() {
        return price2;
    }

    public void setPrice2(double price2) {
        this.price2 = price2;
    }

    public double getPrice1() {
        return price1;
    }

    public void setPrice1(double price1) {
        this.price1 = price1;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }
}
