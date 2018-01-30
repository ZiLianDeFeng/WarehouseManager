package com.hgad.warehousemanager.bean.CheckHouseBean;

import android.content.Intent;
import android.view.View;

import com.baozi.treerecyclerview.adpater.TreeRecyclerAdapter;
import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.item.TreeItem;
import com.baozi.treerecyclerview.manager.ItemManager;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.ui.activity.CheckFloorActivity;

/**
 * Created by Administrator on 2017/11/8.
 */
public class ColItem extends TreeItem<ColEntry> {
    @Override
    protected int initLayoutId() {
        return R.layout.item_col;
    }

    @Override
    public int getSpanSize() {
        return 2;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_col, data.getColName() + "垛");
        viewHolder.setOnClickListener(R.id.tv_col, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemManager itemManager = getItemManager();
                TreeRecyclerAdapter adapter = (TreeRecyclerAdapter) itemManager.getAdapter();
                String positionCode = data.getPositionCode();
                Intent intent = new Intent(adapter.getContext(), CheckFloorActivity.class);
                intent.putExtra(Constants.POSITION_CODE, positionCode);
                intent.putExtra(Constants.TASK_ID, data.getTaskId());
                adapter.getContext().startActivity(intent);
            }
        });
    }
}
