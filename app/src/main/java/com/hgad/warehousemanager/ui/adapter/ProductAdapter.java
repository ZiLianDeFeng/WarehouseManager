package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
public class ProductAdapter extends BaseAdapter {
    private List<WareInfo> data;
    private Context context;

    public ProductAdapter(List<WareInfo> data, Context context) {
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
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_product);
        TextView tv_address = (TextView) holder.getView(R.id.tv_addressWare);
        TextView tv_order_num = (TextView) holder.getView(R.id.tv_order_num);
        TextView tv_weight = (TextView) holder.getView(R.id.tv_weight);
        TextView tv_type = (TextView) holder.getView(R.id.tv_type);
        WareInfo wareInfo = data.get(position);
        String address = wareInfo.getAddress();
        if (TextUtils.isEmpty(address)) {
            tv_address.setText("无");
        } else {
            address = CommonUtils.formatAddress(address);
            tv_address.setText(address);
        }
        tv_order_num.setText(wareInfo.getMarkNum());
        tv_weight.setText(wareInfo.getGrossWeight() + "kg");
        if ("0".equals(wareInfo.getType())) {
            tv_type.setText("0".equals(wareInfo.getState()) ? "待入库" : "已入库");
        } else if ("1".equals(wareInfo.getType())) {
            tv_type.setText("0".equals(wareInfo.getState()) ? "待出库" : "已出库");
        }
        return holder.convertView;
    }
}
