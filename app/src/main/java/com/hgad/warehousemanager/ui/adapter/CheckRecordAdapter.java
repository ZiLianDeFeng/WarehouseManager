package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.CheckTaskInfo;
import com.hgad.warehousemanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
public class CheckRecordAdapter extends BaseAdapter {
    //    private List<SectionBean> data;
    private Context context;
    private List<CheckTaskInfo> data;

    public CheckRecordAdapter(List<CheckTaskInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CheckTaskInfo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CommonViewHolder holder = null;
        CheckTaskInfo checkTaskInfo = getItem(position);
//        CheckTaskInfo checkTaskInfo = sectionBean.getCheckTaskInfo();
//        if (getItemViewType(position) == SectionBean.SECTION) {
//            holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_title);
//            TextView tv_time = ((TextView) holder.getView(R.id.tv_time));
////            TextView tv_count = ((TextView) holder.getView(R.id.tv_count));
//            tv_time.setText(checkTaskInfo.getStartDate());
//        } else if (getItemViewType(position) == SectionBean.ITEM) {

        holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_check_record);
        TextView tv_name = (TextView) holder.getView(R.id.tv_name);
        TextView tv_total = (TextView) holder.getView(R.id.tv_total);
        TextView tv_check = (TextView) holder.getView(R.id.tv_check);
        TextView tv_opera_people = (TextView) holder.getView(R.id.tv_opera_people);
        TextView tv_ware = (TextView) holder.getView(R.id.tv_ware);
        TextView tv_state = (TextView) holder.getView(R.id.tv_state);
        tv_state.setText("0".equals(checkTaskInfo.getState()) ? "盘点中" : "已关闭");
        tv_state.setTextColor("0".equals(checkTaskInfo.getState()) ? context.getResources().getColor(R.color.blue) : context.getResources().getColor(R.color.light_black));
//        TextView tv_node = (TextView) holder.getView(R.id.tv_node);
//        tv_node.setText(checkTaskInfo.getNode());
        tv_ware.setText(checkTaskInfo.getWareNo());
        tv_opera_people.setText(checkTaskInfo.getCheckMan());
        tv_name.setText(checkTaskInfo.getName());
        tv_total.setText(checkTaskInfo.getTotal());
        tv_check.setText(checkTaskInfo.getCheckCount());
//        }
        return holder.convertView;
    }
}
