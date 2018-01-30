package com.hgad.warehousemanager.bean;

import com.hgad.warehousemanager.bean.response.OutOrderListResponse;

/**
 * Created by Administrator on 2017/8/14.
 */
public class ReviewInfo {
    private int orderId;
    private String orderNum;
    private String applyReason;
    private String applyTime;
    private String applyPeople;
    private String urgency;
    private String checkPeople;
    private String totalWeight;
    private String result;
    private String state;

    public ReviewInfo() {
    }

    public ReviewInfo(String orderNum, String applyReason, String applyTime, String applyPeople, String urgency, String checkPeople, String totalWeight, String result) {
        this.orderNum = orderNum;
        this.applyReason = applyReason;
        this.applyTime = applyTime;
        this.applyPeople = applyPeople;
        this.urgency = urgency;
        this.checkPeople = checkPeople;
        this.totalWeight = totalWeight;
        this.result = result;
    }

    public void setData(OutOrderListResponse.DataEntity.ListEntity listEntity) {
        this.orderId = listEntity.getId();
        this.orderNum = listEntity.getOrderNo();
        this.state = listEntity.getStatus();
        this.applyTime = listEntity.getCreateDate();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyPeople() {
        return applyPeople;
    }

    public void setApplyPeople(String applyPeople) {
        this.applyPeople = applyPeople;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getCheckPeople() {
        return checkPeople;
    }

    public void setCheckPeople(String checkPeople) {
        this.checkPeople = checkPeople;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }
}
