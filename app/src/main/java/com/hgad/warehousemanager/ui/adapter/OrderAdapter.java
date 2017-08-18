package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.bean.request.UpdateRequest;
import com.hgad.warehousemanager.bean.response.ErrorResponseInfo;
import com.hgad.warehousemanager.bean.response.UpdateResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.Callback;
import com.hgad.warehousemanager.ui.activity.ApplyOutActivity;
import com.hgad.warehousemanager.util.CommonUtils;
import com.max.pinnedsectionrefreshlistviewdemo.TimeManagement;

import java.text.ParseException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/7/17.
 */
public class OrderAdapter extends BaseSwipeAdapter implements Callback {
    private List<OrderInfo> data;
    private Context context;
    private CallFreshListener callFreshListener;
    private String type;
    private boolean isSwipe;

    public boolean isSwipe() {
        return isSwipe;
    }

    public void setCallFreshListener(CallFreshListener callFreshListener) {
        this.callFreshListener = callFreshListener;
    }

    public OrderAdapter(List<OrderInfo> data, Context context, String type) {
        this.data = data;
        this.context = context;
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
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_order, null);
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        final OrderInfo orderInfo = data.get(position);
        TextView tv_order_num = (TextView) convertView.findViewById(R.id.tv_order_num);
        TextView tv_total = (TextView) convertView.findViewById(R.id.tv_total);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        TextView tv_take_people = (TextView) convertView.findViewById(R.id.tv_take_people);
        TextView tv_state = (TextView) convertView.findViewById(R.id.tv_state);
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe_layout);
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                super.onClose(layout);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isSwipe = false;
                    }
                }, 500);
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                super.onStartClose(layout);
                isSwipe = true;
            }
        });
        if (Constants.REVIEW_TYPE.equals(type)) {
            swipeLayout.setSwipeEnabled(true);
        } else {
            swipeLayout.setSwipeEnabled(false);
        }
        convertView.findViewById(R.id.ll_ask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeLayout.close(true);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, ApplyOutActivity.class);
                        intent.putExtra(Constants.ORDER_INFO, orderInfo);
                        context.startActivity(intent);
                    }
                }, 200);
            }
        });
//        if (orderInfo.getState().equals("3")) {
//            tv_in_ware.setTextColor(context.getResources().getColor(R.color.black));
//        } else {
//            tv_in_ware.setTextColor(context.getResources().getColor(R.color.red));
//        }
//        tv_in_ware.setText(orderInfo.getInWareCount() + "");
        tv_total.setText(orderInfo.getProductCount() + "");
        tv_order_num.setText(orderInfo.getOrderNum());
        String createDate = orderInfo.getCreateDate();
        if (!TextUtils.isEmpty(createDate)) {
            try {
                createDate = TimeManagement.exchangeStringDate(createDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        tv_time.setText(createDate);
        tv_take_people.setText(orderInfo.getProcessPerson());
//        if (Constants.OUT_TYPE.equals(orderInfo.s())) {
//            switch (orderInfo.getState()) {
//                case "0":
//                    tv_state.setText("已出库");
//                    break;
//                case "2":
//                    tv_state.setText("出库中");
//                    break;
//                default:
//                    tv_state.setText("待出库");
//                    break;
//            }
//        } else if (Constants.REVIEW_TYPE.equals(orderInfo.getType())) {
//            switch (orderInfo.getState()) {
//                case "2":
//                    tv_state.setText("审核中");
//                    break;
//                case "3":
//                    tv_state.setText("已审核");
//                    break;
//            }
//        }
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_order);
//        TextView tv_order_num = (TextView) holder.getView(R.id.tv_order_num);
//        TextView tv_in_ware = (TextView) holder.getView(R.id.tv_in_ware);
//        TextView tv_total = (TextView) holder.getView(R.id.tv_total);
//        TextView tv_state = (TextView) holder.getView(R.id.tv_state);
////        Button btn_accept = (Button) holder.getView(R.id.btn_accept);
//        SwipeLayout swipeLayout = (SwipeLayout) holder.getView(R.id.swipe_layout);
//        final OrderInfo orderInfo = data.get(position);
//        if (orderInfo.getState().equals("3")) {
//            tv_in_ware.setTextColor(context.getResources().getColor(R.color.black));
//        } else {
//            tv_in_ware.setTextColor(context.getResources().getColor(R.color.red));
//        }
//        tv_in_ware.setText(orderInfo.getInWareCount() + "");
//        tv_total.setText(orderInfo.getProductCount() + "");
//        tv_order_num.setText(orderInfo.getOrderNum());
//        if (Constants.IN_TYPE.equals(orderInfo.getType())) {
//            switch (orderInfo.getState()) {
//                case "3":
//                    tv_state.setText("已入库");
//                    break;
//                case "2":
//                    tv_state.setText("入库中");
//                    break;
//                default:
//                    tv_state.setText("待入库");
//                    break;
//            }
//        } else if (Constants.OUT_TYPE.equals(orderInfo.getType())) {
//            switch (orderInfo.getState()) {
//                case "3":
//                    tv_state.setText("已出库");
//                    break;
//                case "2":
//                    tv_state.setText("出库中");
//                    break;
//                default:
//                    tv_state.setText("待出库");
//                    break;
//            }
//        } else if (Constants.REVIEW_TYPE.equals(orderInfo.getType())) {
//            switch (orderInfo.getState()) {
//                case "2":
//                    tv_state.setText("审核中");
//                    break;
//                case "3":
//                    tv_state.setText("已审核");
//                    break;
//            }
//        }
//        return holder.convertView;
//    }

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
