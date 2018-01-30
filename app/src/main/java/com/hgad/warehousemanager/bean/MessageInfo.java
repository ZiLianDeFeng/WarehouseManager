package com.hgad.warehousemanager.bean;

import com.hgad.warehousemanager.bean.response.TaskResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/14.
 */
public class MessageInfo implements Serializable{
    private String content;
    private String startTime;
    private String type;
    private int id;
    private String state;
    private String orderNum;

    public MessageInfo() {
    }

    public MessageInfo(String content, String startTime, String type, int id, String state, String orderNum) {
        this.content = content;
        this.startTime = startTime;
        this.type = type;
        this.id = id;
        this.state = state;
        this.orderNum = orderNum;
    }

    public void setData(TaskResponse.DataEntity.ListEntity entity){
        this.content = entity.getTitle();
        this.startTime = entity.getStartTime();
        this.type = entity.getType();
        this.id = entity.getId();
        this.state = entity.getStatus();
        this.orderNum = entity.getOrderNo();
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
