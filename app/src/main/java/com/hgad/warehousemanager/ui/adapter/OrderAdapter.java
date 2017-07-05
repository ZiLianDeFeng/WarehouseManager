package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
public class OrderAdapter extends BaseAdapter {
    private List<WareInfo> data;
    private Context context;

    public OrderAdapter(List<WareInfo> data, Context context) {
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
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_order);
        TextView tv_address = (TextView) holder.getView(R.id.tv_addressWare);
        TextView tv_order_num = (TextView) holder.getView(R.id.tv_order_num);
        TextView tv_weight = (TextView) holder.getView(R.id.tv_weight);
        TextView tv_type = (TextView) holder.getView(R.id.tv_type);
        WareInfo wareInfo = data.get(position);
        tv_address.setText(wareInfo.getAddress());
        tv_order_num.setText(wareInfo.getOrderNum());
        tv_weight.setText(wareInfo.getGrossWeight() + "kg");
        tv_type.setText(wareInfo.getType());
        return holder.convertView;
    }
}
