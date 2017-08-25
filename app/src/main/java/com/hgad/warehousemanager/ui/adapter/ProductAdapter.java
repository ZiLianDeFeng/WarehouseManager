package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
public class ProductAdapter extends BaseAdapter {
    private List<WareInfo> data;
    private Context context;
    private String type;

    public ProductAdapter(List<WareInfo> data, Context context, String type) {
        this.data = data;
        this.context = context;
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
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
        TextView tv_state = (TextView) holder.getView(R.id.tv_state);
        ImageView iv_operate = (ImageView) holder.getView(R.id.iv_operate);
        WareInfo wareInfo = data.get(position);
        String address = wareInfo.getAddress();
        if (TextUtils.isEmpty(address.trim())) {
            tv_address.setText("无");
        } else {
            address = CommonUtils.formatAddress(address);
            tv_address.setText(address);
        }
        tv_order_num.setText(wareInfo.getMarkNum());
        tv_weight.setText(wareInfo.getNetWeight() + "吨");
        String state = wareInfo.getState();
        if (Constants.IN_TYPE.equals(type)) {
            if ("0".equals(state)) {
                tv_state.setText("待审核");
            } else if ("1".equals(state)) {
                tv_state.setText("已审核");
            }
        } else if (Constants.OUT_TYPE.equals(type)) {
            if ("0".equals(state)) {
                tv_state.setText("待扫描");
            } else if ("1".equals(state)) {
                tv_state.setText("复核中");
            } else if ("2".equals(state)) {
                tv_state.setText("待复核");
            } else if ("3".equals(state)) {
                tv_state.setText("已复核");
            }
        } else if (Constants.REVIEW_TYPE.equals(type)) {
            if ("2".equals(state)) {
                tv_state.setText("复核待扫描");
            } else if ("3".equals(state)) {
                tv_state.setText("复核已扫描");
            }
        }
        if (!Constants.OUT_TYPE.equals(type)) {
            iv_operate.setVisibility(View.INVISIBLE);
        }
        return holder.convertView;
    }
}
