package com.hgad.warehousemanager.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.bean.request.UpdateRequest;
import com.hgad.warehousemanager.bean.response.ErrorResponseInfo;
import com.hgad.warehousemanager.bean.response.UpdateResponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.Callback;
import com.hgad.warehousemanager.net.NetUtil;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.CommonViewHolder;
import com.hgad.warehousemanager.util.DialogUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */
public class OrderAdapter extends BaseAdapter implements Callback {
    private List<OrderInfo> data;
    private Context context;
    private CallFreshListener callFreshListener;

    public void setCallFreshListener(CallFreshListener callFreshListener) {
        this.callFreshListener = callFreshListener;
    }

    public OrderAdapter(List<OrderInfo> data, Context context) {
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
        TextView tv_order_num = (TextView) holder.getView(R.id.tv_order_num);
        TextView tv_in_ware = (TextView) holder.getView(R.id.tv_in_ware);
        TextView tv_total = (TextView) holder.getView(R.id.tv_total);
        TextView tv_state = (TextView) holder.getView(R.id.tv_state);
        Button btn_accept = (Button) holder.getView(R.id.btn_accept);
        final OrderInfo orderInfo = data.get(position);
        if (orderInfo.getState().equals("3")) {
            tv_in_ware.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            tv_in_ware.setTextColor(context.getResources().getColor(R.color.red));
        }
        tv_in_ware.setText(orderInfo.getInWareCount() + "");
        tv_total.setText(orderInfo.getProductCount() + "");
        tv_order_num.setText(orderInfo.getOrderNum());
        if ("1".equals(orderInfo.getState())) {
            btn_accept.setText("接受");
            btn_accept.setTextColor(context.getResources().getColor(R.color.white));
            btn_accept.setBackgroundResource(R.drawable.shape_bg_red);
            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showAlert(context, "提示", "确认开始该任务？", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UpdateRequest updateRequest = new UpdateRequest(orderInfo.getTaskId(), "2");
                            NetUtil.sendRequest(updateRequest, UpdateResponse.class, OrderAdapter.this);
                        }
                    }, "取消", null, AlertDialog.THEME_HOLO_LIGHT);
                }
            });
        } else {
            btn_accept.setText("已接受");
            btn_accept.setBackgroundResource(R.drawable.shape_circle);
            btn_accept.setTextColor(context.getResources().getColor(R.color.gray));
            btn_accept.setOnClickListener(null);
        }
        if (orderInfo.getType().equals("0")) {
            if (orderInfo.getState().equals("3")) {
                tv_state.setText("已入库");
            } else if (orderInfo.getState().equals("2")) {
                tv_state.setText("入库中");
            } else {
                tv_state.setText("待入库");
            }
        } else if (orderInfo.getType().equals("1")) {
            if (orderInfo.getState().equals("3")) {
                tv_state.setText("已出库");
            } else if (orderInfo.getState().equals("2")) {
                tv_state.setText("出库中");
            } else {
                tv_state.setText("待出库");
            }
        }
        return holder.convertView;
    }

    @Override
    public void onSuccess(BaseRequest request, Object response) {
        if (request instanceof UpdateRequest) {
            UpdateResponse updateResponse = (UpdateResponse) response;
            if (callFreshListener != null) {
                callFreshListener.callFresh();
            }
            if (updateResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(context, updateResponse.getErrorMsg());
            }
        }
    }

    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {

    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }

    public interface CallFreshListener {
        void callFresh();
    }
}
