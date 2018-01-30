package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */
public class ProDataAdapter extends BaseAdapter {
    private List<WareInfo> data;
    private Context context;
    private DataChooseListener listener;

    public ProDataAdapter(List<WareInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setListener(DataChooseListener listener) {
        this.listener = listener;
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
        final WareInfo wareInfo = data.get(position);
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_pro_data);
        TextView tv_markNum = (TextView) holder.getView(R.id.tv_markNum);
        TextView tv_pro_name = (TextView) holder.getView(R.id.tv_pro_name);
        TextView tv_spec = (TextView) holder.getView(R.id.tv_spec);
        CheckBox cb = (CheckBox) holder.getView(R.id.cb);
        tv_markNum.setText(wareInfo.getMarkNum());
        tv_pro_name.setText(wareInfo.getProName());
        tv_spec.setText(wareInfo.getSpec());
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wareInfo.setCheck(isChecked);
                if (listener != null) {
                    listener.notifyDataChooseCount();
                }
            }
        });
        cb.setChecked(wareInfo.isCheck());
        return holder.convertView;
    }

    public interface DataChooseListener {
        void notifyDataChooseCount();
    }
}
