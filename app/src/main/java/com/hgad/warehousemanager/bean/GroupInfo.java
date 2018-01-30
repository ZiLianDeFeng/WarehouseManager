package com.hgad.warehousemanager.bean;

import com.hgad.warehousemanager.bean.response.GroupListResponse;

/**
 * Created by Administrator on 2017/12/22.
 */
public class GroupInfo {

    private int id;
    private String title;
    private String members;
    private String totalWeight;
    private String state;

    public GroupInfo() {
    }

    public GroupInfo(int id, String title, String members, String totalWeight, String state) {
        this.id = id;
        this.title = title;
        this.members = members;
        this.totalWeight = totalWeight;
        this.state = state;
    }

    public void setData(GroupListResponse.DataEntity.ListEntity entity) {
        this.id = entity.getId();
        this.title = entity.getGroupName();
        this.members = entity.getGroupUsers();
        this.state = entity.getStatus();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
