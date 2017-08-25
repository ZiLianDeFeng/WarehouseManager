package com.hgad.warehousemanager.bean;

import android.text.TextUtils;

import com.hgad.warehousemanager.bean.response.InWareListResponse;
import com.hgad.warehousemanager.bean.response.ProductListResponse;
import com.hgad.warehousemanager.bean.response.WareInfoResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/27.
 */
public class WareInfo implements Serializable {
    private int id;
    private String markNum;
    private String address;
    private String spec;
    private String netWeight;
    private int grossWeight;
    private String state;
    private String orderNum;
    private String proName;
    private String orderItem;
    private String steelGrade;

    public WareInfo() {
    }

    public WareInfo(int id, String markNum, String address, String spec, String netWeight, String state, String orderNum,String proName) {
        this.id = id;
        this.markNum = markNum;
        this.address = address;
        this.spec = spec;
        this.netWeight = netWeight;
        this.state = state;
        this.orderNum = orderNum;
        this.proName = proName;
    }

    public void setData(ProductListResponse.DataEntity.ListEntity entity) {
        this.id = entity.getId();
        this.markNum = entity.getIdentification();
        this.state = entity.getStatus();
        this.orderNum = entity.getOrderNo();
        this.address = entity.getPositionCode();
        this.netWeight = entity.getNetWgt();
        this.proName = entity.getProName();
        this.spec = entity.getSpecification();
    }

    public void setData(WareInfoResponse.DataEntity entity) {
        this.id = entity.getId();
        this.markNum = entity.getIdentification();
        this.address = entity.getPositionCode();
        if (!TextUtils.isEmpty(entity.getNetWgt())) {
            this.netWeight = entity.getNetWgt();
        }
        this.spec = entity.getSpecification();
        this.proName = entity.getProName();
        this.orderNum = entity.getOrderNo();
        this.state = entity.getStatus();
        this.steelGrade = entity.getSteelGrade();
        this.orderItem = entity.getOrderItem();
    }

    public void setData(InWareListResponse.DataEntity.ListEntity listEntity) {
        this.id = listEntity.getId();
        this.markNum = listEntity.getIdentification();
        this.address = listEntity.getPositionCode();
        this.proName = listEntity.getProName();
        this.netWeight = listEntity.getNetWgt();
        this.orderNum = listEntity.getOrderNo();
        this.state = listEntity.getStatus();
        this.spec = listEntity.getSpecification();
        this.orderItem =listEntity.getOrderItem();
        this.steelGrade = listEntity.getSteelGrade();
    }

    public String getSteelGrade() {
        return steelGrade;
    }

    public void setSteelGrade(String steelGrade) {
        this.steelGrade = steelGrade;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarkNum() {
        return markNum;
    }

    public void setMarkNum(String markNum) {
        this.markNum = markNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public int getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(int grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
