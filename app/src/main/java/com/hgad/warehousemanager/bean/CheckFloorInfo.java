package com.hgad.warehousemanager.bean;

/**
 * Created by Administrator on 2017/11/9.
 */
public class CheckFloorInfo {
    private String id;
    private String actPositionCode;
    private String dbIdenfication;
    private String identification;
    private String positionCode;
    private String proName;
    private String state;

    public CheckFloorInfo() {
    }

    public CheckFloorInfo(String actPositionCode, String state) {
        this.actPositionCode = actPositionCode;
        this.state = state;
    }

    public CheckFloorInfo(String id, String actPositionCode, String identification, String positionCode) {
        this.id = id;
        this.actPositionCode = actPositionCode;
        this.identification = identification;
        this.positionCode = positionCode;
    }

    public String getDbIdenfication() {
        return dbIdenfication;
    }

    public void setDbIdenfication(String dbIdenfication) {
        this.dbIdenfication = dbIdenfication;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActPositionCode() {
        return actPositionCode;
    }

    public void setActPositionCode(String actPositionCode) {
        this.actPositionCode = actPositionCode;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }
}
