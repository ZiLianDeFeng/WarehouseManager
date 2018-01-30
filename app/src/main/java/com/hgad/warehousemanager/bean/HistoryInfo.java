package com.hgad.warehousemanager.bean;

/**
 * Created by Administrator on 2017/8/21.
 */
public class HistoryInfo {
    private String operaType;
    private String oldAddress;
    private String nowAddress;
    private String operaTime;
    private String operaPeople;

    public HistoryInfo(String operaType, String oldAddress, String nowAddress, String operaTime, String operaPeople) {
        this.operaType = operaType;
        this.oldAddress = oldAddress;
        this.nowAddress = nowAddress;
        this.operaTime = operaTime;
        this.operaPeople = operaPeople;
    }

    public String getOperaType() {
        return operaType;
    }

    public void setOperaType(String operaType) {
        this.operaType = operaType;
    }

    public String getOldAddress() {
        return oldAddress;
    }

    public void setOldAddress(String oldAddress) {
        this.oldAddress = oldAddress;
    }

    public String getNowAddress() {
        return nowAddress;
    }

    public void setNowAddress(String nowAddress) {
        this.nowAddress = nowAddress;
    }

    public String getOperaTime() {
        return operaTime;
    }

    public void setOperaTime(String operaTime) {
        this.operaTime = operaTime;
    }

    public String getOperaPeople() {
        return operaPeople;
    }

    public void setOperaPeople(String operaPeople) {
        this.operaPeople = operaPeople;
    }
}
