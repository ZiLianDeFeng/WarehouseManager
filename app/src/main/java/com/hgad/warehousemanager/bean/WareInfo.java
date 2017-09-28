package com.hgad.warehousemanager.bean;

import android.text.TextUtils;

import com.hgad.warehousemanager.bean.response.InWareListResponse;
import com.hgad.warehousemanager.bean.response.ProductListResponse;
import com.hgad.warehousemanager.bean.response.SearchResponse;
import com.hgad.warehousemanager.bean.response.WareInfoResponse;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/27.
 */
@DatabaseTable(tableName = WareInfo.TABLE_NAME)
public class WareInfo implements Serializable {

    public static final String TABLE_NAME = "t_ware_info";
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "markNum")
    private String markNum;
    @DatabaseField(columnName = "address")
    private String address;
    @DatabaseField(columnName = "spec")
    private String spec;
    @DatabaseField(columnName = "netWeight")
    private String netWeight;
    @DatabaseField(columnName = "state")
    private String state;
    @DatabaseField(columnName = "orderNum")
    private String orderNum;
    @DatabaseField(columnName = "proName")
    private String proName;
    @DatabaseField(columnName = "orderItem")
    private String orderItem;
    @DatabaseField(columnName = "steelGrade")
    private String steelGrade;
    private String outPlateNumber;
    private String curOutNunber;
    private boolean isCheck;
    @DatabaseField(columnName = "haveCommit")
    private boolean haveCommit;

    public boolean isHaveCommit() {
        return haveCommit;
    }

    public void setHaveCommit(boolean haveCommit) {
        this.haveCommit = haveCommit;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public WareInfo() {
    }

    public WareInfo(String markNum, String address, String spec, String netWeight, String orderItem, String proName, String steelGrade, boolean haveCommit) {
        this.markNum = markNum;
        this.address = address;
        this.spec = spec;
        this.netWeight = netWeight;
        this.orderItem = orderItem;
        this.proName = proName;
        this.steelGrade = steelGrade;
        this.haveCommit = haveCommit;
    }

    @Override
    public String toString() {
        return "WareInfo{" +
                "steelGrade='" + steelGrade + '\'' +
                ", orderItem='" + orderItem + '\'' +
                ", proName='" + proName + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", state='" + state + '\'' +
                ", netWeight='" + netWeight + '\'' +
                ", spec='" + spec + '\'' +
                ", address='" + address + '\'' +
                ", markNum='" + markNum + '\'' +
                ", id=" + id +
                '}';
    }

    public WareInfo(int id, String markNum, String address, String spec, String netWeight, String state, String orderNum, String proName) {
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
        this.outPlateNumber = entity.getOutPlateNumber();
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
        this.orderItem = listEntity.getOrderItem();
        this.steelGrade = listEntity.getSteelGrade();
    }

    public void setData(SearchResponse.DataEntity.ListEntity listEntity) {
        this.id = listEntity.getId();
        this.markNum = listEntity.getIdentification();
        this.address = listEntity.getPositionCode();
        this.proName = listEntity.getProName();
        this.netWeight = listEntity.getNetWgt();
        this.orderNum = listEntity.getOrderNo();
        this.state = listEntity.getStatus();
        this.spec = listEntity.getSpecification();
        this.orderItem = listEntity.getOrderItem();
        this.steelGrade = listEntity.getSteelGrade();
    }

    public String getCurOutNunber() {
        return curOutNunber;
    }

    public void setCurOutNunber(String curOutNunber) {
        this.curOutNunber = curOutNunber;
    }

    public String getOutPlateNumber() {
        return outPlateNumber;
    }

    public void setOutPlateNumber(String outPlateNumber) {
        this.outPlateNumber = outPlateNumber;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
