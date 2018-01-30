package com.hgad.warehousemanager.bean;

import com.hgad.warehousemanager.bean.response.CheckTaskListResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/13.
 */
public class CheckTaskInfo implements Serializable {
    private int taskId;
    private String planTime;
    private String name;
    private String total;
    private String checkCount;
    private String startDate;
    private String checkMan;
    private String wareNo;
    private String state;
    private String node;

    public CheckTaskInfo() {
    }

    public CheckTaskInfo(int taskId, String planTime, String name, String wareNo, String state) {
        this.taskId = taskId;
        this.planTime = planTime;
        this.name = name;
        this.wareNo = wareNo;
        this.state = state;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(String checkCount) {
        this.checkCount = checkCount;
    }

    public String getCheckMan() {
        return checkMan;
    }

    public void setCheckMan(String checkMan) {
        this.checkMan = checkMan;
    }

    public String getWareNo() {
        return wareNo;
    }

    public void setWareNo(String wareNo) {
        this.wareNo = wareNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setData(CheckTaskListResponse.DataEntity dataEntity) {
        this.taskId = dataEntity.getId();
        this.name = dataEntity.getTitle();
        this.checkMan = dataEntity.getCheckMan();
        this.planTime = dataEntity.getCreateDate();
        this.wareNo = dataEntity.getHouse();
        this.state = dataEntity.getStatus();
//        this.node = dataEntity.getNode();
        this.total = dataEntity.getTotal();
        this.checkCount = dataEntity.getAlready();
    }
}
