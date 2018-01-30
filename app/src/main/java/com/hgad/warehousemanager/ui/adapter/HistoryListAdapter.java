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
 * Created by Administrator on 2017/8/21.
 */
public class HistoryListAdapter extends BaseAdapter {
    private List<WareInfo> data;
    private Context context;

    public HistoryListAdapter(List<WareInfo> data, Context context) {
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
        CommonViewHolder viewHolder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_history_list);
        TextView tv_markNum = (TextView) viewHolder.getView(R.id.tv_markNum);
        TextView tv_pro_name = (TextView) viewHolder.getView(R.id.tv_pro_name);
        TextView tv_spec = (TextView) viewHolder.getView(R.id.tv_spec);
        TextView tv_address = (TextView) viewHolder.getView(R.id.tv_address);
        TextView tv_net_weight = (TextView) viewHolder.getView(R.id.tv_net_weight);
        WareInfo wareInfo = data.get(position);
        tv_markNum.setText(wareInfo.getMarkNum());
        tv_pro_name.setText(wareInfo.getProName());
        tv_net_weight.setText(wareInfo.getNetWeight() == null ? "" : wareInfo.getNetWeight() + "吨");
        tv_spec.setText(wareInfo.getSpec());
        String address = wareInfo.getAddress();
        if (!TextUtils.isEmpty(address.trim())) {
            address = CommonUtils.formatAddress(address);
            tv_address.setText(address);
        }else {
            tv_address.setText("无");
        }

        return viewHolder.convertView;
    }
}
