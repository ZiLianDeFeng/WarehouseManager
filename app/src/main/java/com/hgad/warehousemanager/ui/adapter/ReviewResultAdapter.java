package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.ReviewInfo;
import com.hgad.warehousemanager.util.CommonViewHolder;
import com.max.pinnedsectionrefreshlistviewdemo.TimeManagement;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */
public class ReviewResultAdapter extends BaseAdapter {

    private List<ReviewInfo> data;
    private Context context;

    public ReviewResultAdapter(List<ReviewInfo> data, Context context) {
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
        ReviewInfo reviewInfo = data.get(position);
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_review_result);
        TextView tv_order_num = (TextView) holder.getView(R.id.tv_order_num);
        TextView tv_apply_time = (TextView) holder.getView(R.id.tv_apply_time);
        TextView tv_review_result = (TextView) holder.getView(R.id.tv_review_result);
        TextView tv_net_weight = (TextView) holder.getView(R.id.tv_net_weight);
        tv_order_num.setText(reviewInfo.getOrderNum());
        String applyTime = reviewInfo.getApplyTime();
        try {
            applyTime = TimeManagement.exchangeStringDate(applyTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_apply_time.setText(applyTime);
        tv_net_weight.setText(reviewInfo.getTotalWeight());
        if ("3".equals(reviewInfo.getState())) {
            tv_review_result.setText("未审核");
            tv_review_result.setTextColor(context.getResources().getColor(R.color.light_black));
        } else if ("4".equals(reviewInfo.getState())) {
            tv_review_result.setText("已通过");
            tv_review_result.setTextColor(context.getResources().getColor(R.color.blue));
        }
        return holder.convertView;
    }
}
