package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.CheckFloorInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */
public class CheckFloorAdapter extends RecyclerView.Adapter<CheckFloorAdapter.FloorViewHolder> implements View.OnClickListener {

    private List<CheckFloorInfo> dataInfos;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public CheckFloorAdapter(List<CheckFloorInfo> dataInfos, Context context) {
        this.dataInfos = dataInfos;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public FloorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_data, parent, false);
        view.setOnClickListener(this);
        return new FloorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FloorViewHolder holder, int position) {
        CheckFloorInfo dataInfo = dataInfos.get(position);
        holder.tv_floor.setText(dataInfo.getActPositionCode().substring(6));
        if (dataInfo.getState().equals("0")) {
            holder.ll_item.setBackgroundResource(R.drawable.floor_nomal_selector);
//            holder.tv_floor.setBackgroundColor(context.getResources().getColor(R.color.no_net));
            holder.iv_tip.setImageResource(0);
        } else if (dataInfo.getState().equals("1")) {
            holder.ll_item.setBackgroundResource(R.drawable.floor_error_selector);
//            holder.tv_floor.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.iv_tip.setImageResource(0);
        } else if (dataInfo.getState().equals("2")) {
            holder.ll_item.setBackgroundResource(R.drawable.floor_error_selector);
//            holder.tv_floor.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.iv_tip.setImageResource(R.mipmap.tip_wrong);
        } else if (dataInfo.getState().equals("3")) {
            holder.ll_item.setBackgroundResource(R.drawable.floor_ok_selector);
//            holder.tv_floor.setBackgroundColor(context.getResources().getColor(R.color.gray));
            holder.iv_tip.setImageResource(R.mipmap.tip_null);
        }
        holder.itemView.setTag(position);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onItemClick(view, position);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return dataInfos.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            //注意这里使用getTag方法获取position
            onItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public class FloorViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView tv_floor;
        private ImageView iv_tip;
        private LinearLayout ll_item;

        public FloorViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_floor = (TextView) itemView.findViewById(R.id.tv_floor);
            iv_tip = (ImageView) itemView.findViewById(R.id.iv_tip);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_floor_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
