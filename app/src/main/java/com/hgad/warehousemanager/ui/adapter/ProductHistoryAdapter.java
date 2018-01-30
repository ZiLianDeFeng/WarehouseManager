package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.HistoryInfo;
import com.hgad.warehousemanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */
public class ProductHistoryAdapter extends BaseAdapter {
    private List<HistoryInfo> data;
    private Context context;

    public ProductHistoryAdapter(List<HistoryInfo> data, Context context) {
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
        HistoryInfo historyInfo = data.get(position);
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_product_history);
        TextView tv_opera_type = (TextView) holder.getView(R.id.tv_opera_type);
        TextView tv_old_address = (TextView) holder.getView(R.id.tv_old_address);
        TextView tv_now_address = (TextView) holder.getView(R.id.tv_now_address);
        TextView tv_opera_people = (TextView) holder.getView(R.id.tv_opera_people);
        TextView tv_opera_time = (TextView) holder.getView(R.id.tv_opera_time);
        String operaType = historyInfo.getOperaType();
        tv_opera_type.setText(operaType.equals("0") ? "入库" : operaType.equals("1") ? "出库" : operaType.equals("2") ? "移位" : operaType.equals("3") ? "盘点" : "");
        tv_old_address.setText(historyInfo.getOldAddress());
        tv_now_address.setText(historyInfo.getNowAddress());
        tv_opera_people.setText(historyInfo.getOperaPeople());
        tv_opera_time.setText(historyInfo.getOperaTime());
        return holder.convertView;
    }
}
