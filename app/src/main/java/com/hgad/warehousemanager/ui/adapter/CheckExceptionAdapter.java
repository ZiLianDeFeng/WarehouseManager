package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.CheckExceptionInfo;
import com.hgad.warehousemanager.bean.CheckFloorInfo;
import com.hgad.warehousemanager.bean.request.ExceptionDoneRequest;
import com.hgad.warehousemanager.bean.request.InWareRequest;
import com.hgad.warehousemanager.bean.response.ErrorResponseInfo;
import com.hgad.warehousemanager.bean.response.ExceptionResponse;
import com.hgad.warehousemanager.bean.response.InWareResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.Callback;
import com.hgad.warehousemanager.net.NetUtil;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */
public class CheckExceptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Callback {

    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_HEADER = 2;
    private String model = Constants.MODEL;
    private Context context;
    private List<CheckExceptionInfo> checkExceptionInfos;
    private String userName;
    private OnRefreshListener onRefreshListener;
    private CustomProgressDialog customProgressDialog;
    private boolean isConnect;
    private Handler handler = new Handler();
    private String id;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public CheckExceptionAdapter(Context context, List<CheckExceptionInfo> checkExceptionInfos) {
        this.context = context;
        this.checkExceptionInfos = checkExceptionInfos;
        userName = SPUtils.getString(context, SPConstants.USER_NAME);
    }

    @Override
    public int getItemViewType(int position) {
        CheckExceptionInfo checkExceptionInfo = checkExceptionInfos.get(position);
        return checkExceptionInfo.getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HEADER:
                viewHolder = new HeaderViewHolder(inflater.inflate(R.layout.item_check_head, parent, false));
                break;
            case TYPE_NORMAL:
                viewHolder = new ItemViewHolder(inflater.inflate(R.layout.item_check_content, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CheckFloorInfo checkFloorInfo = checkExceptionInfos.get(position).getCheckFloorInfo();
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.tv_type.setText(checkFloorInfo.getState().equals("0") ? "盘盈" : checkFloorInfo.getState().equals("1") ? "盘亏" : "货位有误");
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.tv_act_position.setText(checkFloorInfo.getActPositionCode());
            itemViewHolder.tv_house_position.setText(checkFloorInfo.getPositionCode());
            itemViewHolder.tv_pro_name.setText(checkFloorInfo.getProName());
            itemViewHolder.tv_markNum.setText(checkFloorInfo.getIdentification());
            final String state = checkFloorInfo.getState();
            if (!"0".equals(state)) {
                itemViewHolder.btn_fix.setVisibility(View.INVISIBLE);
            } else {
                itemViewHolder.btn_fix.setVisibility(View.VISIBLE);
            }
            itemViewHolder.btn_fix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("0".equals(state)) {
                        showDialog(context.getString(R.string.commit_data));
                        id = checkFloorInfo.getId();
                        inWare(checkFloorInfo.getIdentification(), checkFloorInfo.getActPositionCode());
                    } else if ("1".equals(state)) {
                        forDelRecord();
                    } else if ("2".equals(state)) {
                        CommonUtils.showToast(context, "移位");
                    }
                }
            });
        }
    }

    private void forDelRecord() {

    }

    private void inWare(String identification, String actPositionCode) {
//        InWareRequest inWareRequest = new InWareRequest(identification, userName, actPositionCode, "1", "0", model, "0");
//        NetUtil.sendRequest(inWareRequest, InWareResponse.class, this);
        CommonUtils.showToast(context,"请人工处理");
    }

    @Override
    public int getItemCount() {
        return checkExceptionInfos.size();
    }

    @Override
    public void onSuccess(BaseRequest request, Object response) {
        if (request instanceof InWareRequest) {
            InWareResponse inWareResponse = (InWareResponse) response;
            if (inWareResponse.getResponseCode() != null) {
                if (inWareResponse.getResponseCode().getCode() == 200) {
                    if (Constants.REQUEST_SUCCESS.equals(inWareResponse.getErrorMsg())) {
                        CommonUtils.showToast(context, "修复成功");
                        if (customProgressDialog != null) {
                            customProgressDialog.dismiss();
                            isConnect = true;
                        }
                        showDialog(context.getString(R.string.info_check));
                        ExceptionDoneRequest exceptionDoneRequest = new ExceptionDoneRequest(id);
                        NetUtil.sendRequest(exceptionDoneRequest, ExceptionResponse.class, this);
                    } else {
                        if (customProgressDialog != null) {
                            customProgressDialog.dismiss();
                            isConnect = true;
                        }
                        new AlertView("提示", inWareResponse.getErrorMsg(), null, new String[]{"确定"}, null, context, AlertView.Style.Alert, null).setCancelable(false).show();
                    }
                } else {
                    CommonUtils.showToast(context, inWareResponse.getErrorMsg());
                }
            }
        } else if (request instanceof ExceptionDoneRequest) {
            if (customProgressDialog != null) {
                customProgressDialog.dismiss();
                isConnect = true;
            }
            ExceptionResponse exceptionResponse = (ExceptionResponse) response;
            if (exceptionResponse.getResponseCode() != null) {
                if (Constants.REQUEST_SUCCESS.equals(exceptionResponse.getErrorMsg())) {
                    if (onRefreshListener != null) {
                        onRefreshListener.onRefresh();
                    }
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

    private void showDialog(String content) {
        customProgressDialog = new CustomProgressDialog(context, content);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        isConnect = false;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isConnect) {
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                        CommonUtils.showToast(context, context.getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_type;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_pro_name;
        private TextView tv_act_position;
        private TextView tv_house_position;
        private TextView tv_markNum;
        private Button btn_fix;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_pro_name = (TextView) itemView.findViewById(R.id.tv_pro_name);
            tv_act_position = (TextView) itemView.findViewById(R.id.tv_act_position);
            tv_house_position = (TextView) itemView.findViewById(R.id.tv_house_position);
            tv_markNum = (TextView) itemView.findViewById(R.id.tv_markNum);
            btn_fix = (Button) itemView.findViewById(R.id.btn_fix);
        }
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
