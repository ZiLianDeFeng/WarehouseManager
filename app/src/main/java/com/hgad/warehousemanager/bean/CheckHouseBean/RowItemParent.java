package com.hgad.warehousemanager.bean.CheckHouseBean;

import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.factory.ItemHelperFactory;
import com.baozi.treerecyclerview.item.TreeItem;
import com.baozi.treerecyclerview.item.TreeItemGroup;
import com.hgad.warehousemanager.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */
public class RowItemParent extends TreeItemGroup<RowEntry> {
    @Override
    protected List<TreeItem> initChildsList(RowEntry data) {
        return ItemHelperFactory.createTreeItemList(data.getSubItems(), ColItem.class, this);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.item_row;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_row, data.getRowName() + "排");
        if (isExpand()) {
            viewHolder.setImageResource(R.id.iv_row, R.mipmap.arrow_up);
        } else {
            viewHolder.setImageResource(R.id.iv_row, R.mipmap.arrow_down);
        }
    }
}
