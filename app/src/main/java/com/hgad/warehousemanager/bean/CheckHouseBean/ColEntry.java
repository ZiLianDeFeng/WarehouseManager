package com.hgad.warehousemanager.bean.CheckHouseBean;

import com.baozi.treerecyclerview.base.BaseItemData;

/**
 * Created by Administrator on 2017/11/8.
 */
public class ColEntry extends BaseItemData {

    private String colName;
    private String positionCode;
    private int taskId;

    public ColEntry(String colName, String positionCode, int taskId) {
        this.colName = colName;
        this.positionCode = positionCode;
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }
}
