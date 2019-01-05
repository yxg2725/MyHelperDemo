package com.example.myhelper.entity;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Administrator on 2019/1/4.
 */

public class MyOrder extends LitePalSupport {
    private String productName;
    private String customerName;
    private int number;
    private String time;
    private double totalCost;
    private double totalPrice;
    private String orderNo;
    private int payState;//订单状态
    private String sendState;//发货状态
    private int state;//出库0 入库1

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getProductName() {
        return productName;

    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public String getSendState() {
        return sendState;
    }

    public void setSendState(String sendState) {
        this.sendState = sendState;
    }



    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
