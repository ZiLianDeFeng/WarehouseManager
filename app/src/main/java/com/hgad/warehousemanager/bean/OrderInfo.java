package com.hgad.warehousemanager.bean;

import com.hgad.warehousemanager.bean.response.OutOrderListResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/17.
 */
public class OrderInfo implements Serializable {
    private String orderNum;
    private String productCount;
    private String inWareCount;
    private String state;
    private String type;
    private int taskId;
    private String totalWeight;
    private String price;
    private String takePerson;
    private String processPerson;
    private String createDate;
    private String createPerson;
    private String productType;

    public OrderInfo() {
    }

    public OrderInfo(String orderNum, int productCount, String state, int taskId, String price, String takePerson, String processPerson, String createDate, String createPerson) {
        this.orderNum = orderNum;
        this.productCount = productCount + "";
        this.state = state;
        this.taskId = taskId;
        this.price = price;
        this.takePerson = takePerson;
        this.processPerson = processPerson;
        this.createDate = createDate;
        this.createPerson = createPerson;
        this.totalWeight = "1";
    }

    public void setData(OutOrderListResponse.DataEntity.ListEntity listEntity) {
        this.orderNum = listEntity.getOrderNo();
        this.state = listEntity.getStatus();
        this.price = listEntity.getPrice();
        this.taskId = listEntity.getId();
        this.takePerson = listEntity.getTakePerson();
        this.processPerson = listEntity.getProcessPerson();
        this.createDate = listEntity.getCreateDate();
        this.createPerson = listEntity.getCreatePerson();
//        if (listEntity.getProductOutList() != null) {
//            this.productCount = listEntity.getProductOutList().size() + "";
//        }
        this.productCount = listEntity.getTotal();
        this.productType = listEntity.getProductType();
        this.totalWeight = listEntity.getWeight();
        this.inWareCount = listEntity.getAlready();
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getTakePerson() {
        return takePerson;
    }

    public void setTakePerson(String takePerson) {
        this.takePerson = takePerson;
    }

    public String getProcessPerson() {
        return processPerson;
    }

    public void setProcessPerson(String processPerson) {
        this.processPerson = processPerson;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getInWareCount() {
        return inWareCount;
    }

    public void setInWareCount(String inWareCount) {
        this.inWareCount = inWareCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
