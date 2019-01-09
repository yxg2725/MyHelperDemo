package com.example.myhelper.entity;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/1/4.
 */

public class MyOrder extends LitePalSupport implements Serializable{
    private String productDetail;//所有产品信息
    private String customerName;
    private int number;
    private String time;
    private double totalCost;
    private double totalPrice;//当前订单总金额
    private String orderNo;
    private int orderState;//订单状态   0表示已完成  1表示未支付  2表示未全部发货
    private String sendState;//发货状态
    private double actualPayment;//实际支付总金额
    private int state;//出库0 入库1



    public double getActualPayment() {
        return actualPayment;
    }

    public void setActualPayment(double actualPayment) {
        this.actualPayment = actualPayment;
    }

    public int getOrderState() {

        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }



    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
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
