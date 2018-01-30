package com.hgad.warehousemanager.ui.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.MessageInfo;
import com.hgad.warehousemanager.bean.request.UpdateRequest;
import com.hgad.warehousemanager.bean.response.ErrorResponseInfo;
import com.hgad.warehousemanager.bean.response.UpdateResponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.Callback;
import com.hgad.warehousemanager.net.NetUtil;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */
public class MessageAdapter extends BaseAdapter implements Callback {
    private List<MessageInfo> listData;
    private Context context;
    private CallFreshListener callFreshListener;

    public void setCallFreshListener(CallFreshListener callFreshListener) {
        this.callFreshListener = callFreshListener;
    }

    public MessageAdapter(List<MessageInfo> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size() == 0 ? 0 : listData.size();
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
    public View getView(int position, final View convertView, ViewGroup parent) {
        final MessageInfo messageInfo = listData.get(position);
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_message);
        ImageView iv_pic = ((ImageView) holder.getView(R.id.iv_pic));
        TextView tv_type = ((TextView) holder.getView(R.id.tv_type));
        TextView tv_content = ((TextView) holder.getView(R.id.tv_content));
        TextView tv_time = ((TextView) holder.getView(R.id.tv_time));
        TextView tv_state = (TextView) holder.getView(R.id.tv_state);
        TextView tv_order_num = (TextView) holder.getView(R.id.tv_order_num);
        tv_order_num.setText(messageInfo.getOrderNum());
        tv_content.setText(messageInfo.getContent());
        tv_time.setText(messageInfo.getStartTime());
        if ("1".equals(messageInfo.getState())) {
            tv_state.setText("接受");
            tv_state.setTextColor(context.getResources().getColor(R.color.white));
            tv_state.setBackgroundResource(R.drawable.shape_bg_red);
            tv_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertView("提示", "确认开始该任务？", "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            switch (position) {
                                case 0:
                                    UpdateRequest updateRequest = new UpdateRequest(messageInfo.getId(), "2");
                                    NetUtil.sendRequest(updateRequest, UpdateResponse.class, MessageAdapter.this);
                                    break;
                            }
                        }
                    }).show();
//                    DialogUtils.showAlert(context, "提示", "确认开始该任务？", "确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            UpdateRequest updateRequest = new UpdateRequest(messageInfo.getId(), "2");
//                            NetUtil.sendRequest(updateRequest, UpdateResponse.class, MessageAdapter.this);
//
//                        }
//                    }, "取消", null, AlertDialog.THEME_HOLO_LIGHT);
                }
            });
        } else {
            tv_state.setText("已接受");
            tv_state.setBackgroundResource(R.drawable.shape_circle);
            tv_state.setTextColor(context.getResources().getColor(R.color.gray));
            tv_state.setOnClickListener(null);
        }
        if ("1".equals(messageInfo.getType())) {
            tv_type.setText("出库任务");
            iv_pic.setImageResource(R.mipmap.search_notification);
        } else if ("0".equals(messageInfo.getType())) {
            tv_type.setText("入库任务");
            iv_pic.setImageResource(R.mipmap.maintenance_notification);
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
