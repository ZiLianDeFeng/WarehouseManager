package com.hgad.warehousemanager.bean;

/**
 * Created by Administrator on 2017/6/27.
 */
public class WareInfo {
    private int id;
    private String orderNum;
    private String address;
    private int netWeight;
    private int grossWeight;
    private String type;

    public WareInfo(int id, String orderNum, String address, int netWeight, int grossWeight, String type) {
        this.id = id;
        this.orderNum = orderNum;
        this.address = address;
        this.netWeight = netWeight;
        this.grossWeight = grossWeight;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(int netWeight) {
        this.netWeight = netWeight;
    }

    public int getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(int grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
