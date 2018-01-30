package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.GroupInfo;
import com.hgad.warehousemanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/12/22.
 */
public class GroupAdapter extends BaseAdapter {
    private List<GroupInfo> data;
    private Context context;

    public GroupAdapter(List<GroupInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupInfo groupInfo = data.get(position);
        CommonViewHolder viewHolder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_group);
        TextView tv_group_name = (TextView) viewHolder.getView(R.id.tv_group_name);
        TextView tv_group_member = (TextView) viewHolder.getView(R.id.tv_group_member);
        TextView tv_total_weight = (TextView) viewHolder.getView(R.id.tv_total_weight);
        TextView tv_group_state = (TextView) viewHolder.getView(R.id.tv_group_state);
        tv_group_name.setText(groupInfo.getTitle());
        tv_group_member.setText(groupInfo.getMembers());
        tv_total_weight.setText(groupInfo.getTotalWeight());
        tv_group_state.setText("0".equals(groupInfo.getState()) ? "(活动)" : "(已解散)");
        tv_group_state.setTextColor("0".equals(groupInfo.getState()) ? Color.BLUE : Color.GRAY);
        return viewHolder.convertView;
    }
}
