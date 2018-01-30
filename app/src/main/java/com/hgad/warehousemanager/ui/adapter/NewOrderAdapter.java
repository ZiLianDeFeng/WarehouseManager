package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.bean.request.ForUpRequest;
import com.hgad.warehousemanager.bean.response.ErrorResponseInfo;
import com.hgad.warehousemanager.bean.response.ForUpResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.Callback;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 */
public class NewOrderAdapter extends BaseAdapter implements Callback {

    private List<OrderInfo> data;
    private Context context;
    private CallFreshListener callFreshListener;
    private GroupStateListener groupStateListener;
    private OnActionListener onActionListener;

    public NewOrderAdapter(List<OrderInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }


    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public void setGroupStateListener(GroupStateListener groupStateListener) {
        this.groupStateListener = groupStateListener;
    }

    public void setCallFreshListener(CallFreshListener callFreshListener) {
        this.callFreshListener = callFreshListener;
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
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_new_order);
        final OrderInfo orderInfo = data.get(position);
        TextView tv_order_num = (TextView) holder.getView(R.id.tv_order_num);
        TextView tv_total = (TextView) holder.getView(R.id.tv_total);
        TextView tv_time = (TextView) holder.getView(R.id.tv_time);
        TextView tv_take_people = (TextView) holder.getView(R.id.tv_take_people);
        TextView tv_state = (TextView) holder.getView(R.id.tv_state);
        TextView tv_out_ware = (TextView) holder.getView(R.id.tv_out_ware);
        TextView tv_out_review = (TextView) holder.getView(R.id.tv_out_review);
        TextView tv_pro_type = (TextView) holder.getView(R.id.tv_pro_type);
        TextView tv_total_weight = (TextView) holder.getView(R.id.tv_total_weight);
        TextView tv_out_count = (TextView) holder.getView(R.id.tv_out_count);
        if ("1".equals(orderInfo.getState())) {
            tv_state.setText("出库中");
            tv_out_ware.setTextColor(context.getResources().getColor(R.color.blue));
            tv_out_ware.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                if ("1".equals(orderInfo.getState()) || "5".equals(orderInfo.getState())) {
//                    if (groupStateListener != null) {
//                        boolean inGroup = groupStateListener.inGroupOrNot();
//                        if (!inGroup) {
//                            return;
//                        }
//                    }
                    if (onActionListener!=null){
                        onActionListener.onOutWare(orderInfo);
                    }
//                    Intent intent = new Intent(context, ProductListActivity.class);
//                    intent.putExtra(Constants.TYPE, Constants.OUT_WARE);
//                    intent.putExtra(Constants.ORDER_INFO, orderInfo);
//                    intent.putExtra(Constants.PRO_TYPE, orderInfo.getProductType());
//                    context.startActivity(intent);
//                } else {
//                    CommonUtils.showToast(context, "当前订单不是出库状态，无法操作");
//                }
                }
            });
            tv_out_review.setTextColor(context.getResources().getColor(R.color.red));
            tv_out_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                if ("1".equals(orderInfo.getState())) {
//                    new AlertView("提示", "订单货品未全部扫描完，\n是否部分货品提前复核", "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(Object o, int position) {
//                            switch (position) {
//                                case 0:
//                                    ForUpRequest forUpRequest = new ForUpRequest(orderInfo.getTaskId(), "2", null);
//                                    NetUtil.sendRequest(forUpRequest, ForUpResponse.class, NewOrderAdapter.this);
//                                    break;
//                            }
//                        }
//                    }).setCancelable(false).show();
//                } else if ("2".equals(orderInfo.getState())) {
//                    if (groupStateListener != null) {
//                        boolean inGroup = groupStateListener.inGroupOrNot();
//                        if (!inGroup) {
//                            return;
//                        }
//                    }
                    if (onActionListener!=null){
                        onActionListener.onReviewWare(orderInfo);
                    }
//                    Intent intent = new Intent(context, ProductListActivity.class);
//                    intent.putExtra(Constants.TYPE, Constants.REVIEW_TYPE);
//                    intent.putExtra(Constants.ORDER_INFO, orderInfo);
//                    intent.putExtra(Constants.PRO_TYPE, orderInfo.getProductType());
//                    context.startActivity(intent);
//                }
                }
            });
        }
        if ("2".equals(orderInfo.getState())) {
            tv_state.setText("已出库");
            tv_out_ware.setTextColor(context.getResources().getColor(R.color.gray));
            tv_out_ware.setOnClickListener(null);
            tv_out_review.setTextColor(context.getResources().getColor(R.color.gray));
            tv_out_review.setOnClickListener(null);
        }
        Double aWeight = Double.valueOf(orderInfo.getTotalWeight());
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.000");
        String format = df.format(aWeight);
        tv_total_weight.setText(format);
        tv_total.setText(orderInfo.getProductCount());
        tv_out_count.setText(orderInfo.getInWareCount());
        tv_order_num.setText(orderInfo.getOrderNum());
        String createDate = orderInfo.getCreateDate();
        tv_time.setText(createDate);
        tv_take_people.setText(orderInfo.getProcessPerson());
        tv_pro_type.setText("1".equals(orderInfo.getProductType()) ? "钢板" : "0".equals(orderInfo.getProductType()) ? "钢卷" : "其它");
        return holder.convertView;
    }

    @Override
    public void onSuccess(BaseRequest request, Object response) {
        if (request instanceof ForUpRequest) {
            ForUpResponse forUpResponse = (ForUpResponse) response;
            if (forUpResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(context, forUpResponse.getErrorMsg());
                if (Constants.REQUEST_SUCCESS.equals(forUpResponse.getErrorMsg())) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AlertView("提示", "订单进入复核状态，\n请货品上车后进行复核", null, new String[]{"确认"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    switch (position) {
                                        case 0:
                                            if (callFreshListener != null) {
                                                callFreshListener.callFresh();
                                            }
                                            break;
                                    }
                                }
                            }).setCancelable(false).show();
                        }
                    }, 1000);
                }
            }
        }
    }

    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {
    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }

    public interface OnActionListener{
        void onOutWare(OrderInfo orderInfo);
        void onReviewWare(OrderInfo orderInfo);
    }

    public interface CallFreshListener {
        void callFresh();
    }

    public interface GroupStateListener {
        boolean inGroupOrNot();
    }
}
