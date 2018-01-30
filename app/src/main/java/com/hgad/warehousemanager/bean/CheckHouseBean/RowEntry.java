package com.hgad.warehousemanager.bean.CheckHouseBean;

import com.baozi.treerecyclerview.base.BaseItemData;

import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */
public class RowEntry extends BaseItemData {
    private String rowName;
    private List<ColEntry> subItems;

    public RowEntry() {
    }

    public RowEntry(String rowName, List<ColEntry> subItems) {
        this.rowName = rowName;
        this.subItems = subItems;
    }

    public String getRowName() {
        return rowName;
    }

    public List<ColEntry> getSubItems() {
        return subItems;
    }
}
