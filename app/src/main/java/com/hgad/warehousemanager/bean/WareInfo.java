package com.hgad.warehousemanager.bean;

import android.text.TextUtils;

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
    private int netWeight;
    private int grossWeight;
    private String state;
    private String taskId;
    private String type;
    private String proName;

    public WareInfo() {
    }

    public WareInfo(int id, String markNum, String address, String spec, int netWeight, int grossWeight, String state, String taskId, String type) {
        this.id = id;
        this.markNum = markNum;
        this.address = address;
        this.spec = spec;
        this.netWeight = netWeight;
        this.grossWeight = grossWeight;
        this.state = state;
        this.taskId = taskId;
        this.type = type;
    }

    public void setData(ProductListResponse.DataEntity.ListEntity entity) {
        this.id = entity.getId();
        this.markNum = entity.getIdentification();
        this.state = entity.getStatus();
        this.taskId = entity.getInoutId();
        this.address = entity.getPosition();
        this.type = entity.getType();
    }

    public void setData(WareInfoResponse.DataEntity entity) {
        this.id = entity.getId();
        this.markNum = entity.getIdentification();
        this.address = entity.getPositionCode();
        if (!TextUtils.isEmpty(entity.getGrossWgt()) && !TextUtils.isEmpty(entity.getNetWgt())) {
            this.grossWeight = Integer.parseInt(entity.getGrossWgt().trim());
            this.netWeight = Integer.parseInt(entity.getNetWgt());
        }
        this.spec = entity.getSpecification();
        this.proName = entity.getProName();
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
