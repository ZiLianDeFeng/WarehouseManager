package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.CheckFloorInfo;
import com.hgad.warehousemanager.bean.request.CheckFloorDetailRequest;
import com.hgad.warehousemanager.bean.request.CheckRequest;
import com.hgad.warehousemanager.bean.request.CheckStartRequest;
import com.hgad.warehousemanager.bean.response.CheckFloorDetailResponse;
import com.hgad.warehousemanager.bean.response.CheckResponse;
import com.hgad.warehousemanager.bean.response.CheckStartResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.CheckFloorAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;
import com.hgad.warehousemanager.zxing.activity.QrScanActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */
public class CheckFloorActivity extends BaseActivity {
    private static final int SCAN = 230;
    private static final int SCAN_RESULT = 240;
    String[] floors = new String[27];
    private List<CheckFloorInfo> floorList = new ArrayList<>();
    private RecyclerView rl;
    private CheckFloorAdapter checkFloorAdapter;
    private String positionCode;
    private String type = Constants.CHECK;
    private int taskId;
    private String address;
    private PopupWindow popupWindow;
    private int screenHeight;
    private LinearLayout ll_popup;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler();
    private String realName;
    private boolean isConnect;
    private LinearLayout ll_wrong;
    private CheckFloorInfo checkFloorInfo;
    private int measuredHeight;


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_check_floor);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 27; i++) {
            if (i < 9) {
                floors[i] = "0" + (i + 1);
            } else if (i >= 9 && i < 18) {
                floors[i] = i + 2 + "";
            } else if (i >= 18) {
                floors[i] = i + 3 + "";
            }
        }
        realName = SPUtils.getString(this, SPConstants.REAL_NAME);
        Intent intent = getIntent();
        positionCode = intent.getStringExtra(Constants.POSITION_CODE);
        taskId = intent.getIntExtra(Constants.TASK_ID, 0);
        String title = CommonUtils.formatAddress(positionCode);
        initHeader(title);
        for (String floor : floors) {
            floorList.add(new CheckFloorInfo(positionCode + floor, "0"));
        }
        checkFloorAdapter.notifyDataSetChanged();
        if (Constants.DEBUG) {
            for (int i = 0; i < floorList.size(); i++) {
                floorList.get(i).setState((i % 4) + "");
            }
        } else {
            showProgressDialog(getString(R.string.info_check));
            callRefresh();
        }
    }

    @Override
    protected void initView() {
        rl = (RecyclerView) findViewById(R.id.rl_floor);
        rl.setLayoutManager(new GridLayoutManager(this, 3));
        rl.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (view.getLayoutParams() instanceof GridLayoutManager.LayoutParams) {
                    GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                    if (layoutParams.getSpanIndex() >= layoutParams.getSpanSize() - 1) {
                        outRect.top = 10;
                    }
                    int spanIndex = layoutParams.getSpanIndex();//在一行中所在的角标，第几列
                    if (spanIndex != ((GridLayoutManager) parent.getLayoutManager()).getSpanCount() - 1) {
                        outRect.right = 10;
                    }

                }
            }
        });
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        checkFloorAdapter = new CheckFloorAdapter(floorList, this);
        checkFloorAdapter.setOnItemClickListener(new CheckFloorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                checkFloorInfo = floorList.get(position);
                address = checkFloorInfo.getActPositionCode();
                if ("2".equals(checkFloorInfo.getState())) {
                    ll_wrong.setVisibility(View.VISIBLE);
                } else {
                    ll_wrong.setVisibility(View.GONE);
                }
                if (view.getY() + view.getHeight() < screenHeight / 2) {
                    popupWindow.showAsDropDown(view);
                    ll_popup.setBackgroundResource(R.drawable.popup_down_black);
                } else {
                    ll_popup.setBackgroundResource(R.drawable.popup_up_black);
                    if (ll_wrong.getVisibility() == View.VISIBLE) {
                        popupWindow.showAsDropDown(view, 0, -(view.getHeight() + measuredHeight));
                    } else {
                        popupWindow.showAsDropDown(view, 0, -(view.getHeight() + measuredHeight * 2 / 3));
                    }
                }
            }
        });
        rl.setAdapter(checkFloorAdapter);
        TextView tv_confirm = (TextView) findViewById(R.id.btn_confirm);
        tv_confirm.setVisibility(View.INVISIBLE);
        tv_confirm.setText("一键空位");
        tv_confirm.setOnClickListener(this);
        initPopupWindow();
    }

    private void initPopupWindow() {
        View contentView = View.inflate(this, R.layout.popup_floor_ope, null);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        measuredHeight = contentView.getMeasuredHeight();
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000"))); //设置背景
        popupWindow.setFocusable(true); //设置获取焦点
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.setOutsideTouchable(true);
        LinearLayout ll_scan = (LinearLayout) contentView.findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);
        LinearLayout ll_empty = (LinearLayout) contentView.findViewById(R.id.ll_empty);
        ll_empty.setOnClickListener(this);
        ll_popup = (LinearLayout) contentView.findViewById(R.id.ll_popup);
        ll_wrong = (LinearLayout) contentView.findViewById(R.id.ll_wrong);
        ll_wrong.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString(Constants.RESULT);
                Intent intent = new Intent(this, ScanResultActivity.class);
                String codeType = bundle.getString(Constants.CODE_TYPE);
                String scanState = bundle.getString(Constants.SCAN_STATE);
                intent.putExtra(Constants.CODE_TYPE, codeType);
                intent.putExtra(Constants.SCAN_RESULT, result);
                intent.putExtra(Constants.TYPE, type);
                intent.putExtra(Constants.SCAN_STATE, scanState);
                intent.putExtra(Constants.ADDRESS, address);
                intent.putExtra(Constants.TASK_ID, taskId + "");
                startActivityForResult(intent, SCAN_RESULT);
            }
        }
        if (resultCode == Constants.RESULT_OK && requestCode == SCAN_RESULT) {
            showProgressDialog(getString(R.string.info_check));
            callRefresh();
        }
        if (resultCode == Constants.HAND && requestCode == SCAN) {
            Intent intent = new Intent(this, InWareByHandActivity.class);
            intent.putExtra(Constants.ADDRESS, address);
            intent.putExtra(Constants.TYPE, type);
            intent.putExtra(Constants.TASK_ID, taskId + "");
            startActivityForResult(intent, SCAN_RESULT);
        }
    }

    private void callRefresh() {
        CheckFloorDetailRequest checkFloorDetailRequest = new CheckFloorDetailRequest(taskId + "", positionCode);
        sendRequest(checkFloorDetailRequest, CheckFloorDetailResponse.class);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (request instanceof CheckStartRequest) {
            CheckStartResponse checkStartResponse = (CheckStartResponse) response;
            if (checkStartResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(checkStartResponse.getErrorMsg())) {
                    CommonUtils.showToast(CheckFloorActivity.this, "提交完成");
                    callRefresh();
                } else {
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                    }
                    isConnect = true;
                    CommonUtils.showToast(this, checkStartResponse.getErrorMsg());
                }
            }
        } else if (request instanceof CheckFloorDetailRequest) {
            if (customProgressDialog != null) {
                customProgressDialog.dismiss();
            }
            isConnect = true;
            CheckFloorDetailResponse checkFloorDetailResponse = (CheckFloorDetailResponse) response;
            if (checkFloorDetailResponse.getResponseCode().getCode() == 200) {
                List<CheckFloorDetailResponse.DataEntity> data = checkFloorDetailResponse.getData();
                for (int i = 0; i < data.size(); i++) {
                    for (int j = 0; j < floorList.size(); j++) {
                        CheckFloorDetailResponse.DataEntity dataEntity = data.get(i);
                        CheckFloorInfo checkFloorInfo = floorList.get(j);
                        if (dataEntity.getPositionCode().equals(checkFloorInfo.getActPositionCode())) {
                            checkFloorInfo.setIdentification(dataEntity.getRealIdenification());
                            checkFloorInfo.setDbIdenfication(dataEntity.getDbIdentification());
                            if (dataEntity.getRealIdenification() == null && dataEntity.getDbIdentification() == null) {
                                checkFloorInfo.setState("3");
                            } else if (dataEntity.getRealIdenification() != null && dataEntity.getDbIdentification() != null && dataEntity.getRealIdenification().equals(dataEntity.getDbIdentification())) {
                                checkFloorInfo.setState("1");
                            } else {
                                checkFloorInfo.setState("2");
                            }
                        }
                    }
                }
                checkFloorAdapter.notifyDataSetChanged();
            }
        } else if (request instanceof CheckRequest) {
            CheckResponse checkResponse = (CheckResponse) response;
            if (checkResponse.getResponseCode().getCode() == 200) {
//                CheckStartRequest checkStartRequest = new CheckStartRequest(address, realName, Constants.MODEL, taskId + "");
//                sendRequest(checkStartRequest, CheckStartResponse.class);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_scan:
                popupWindow.dismiss();
                Intent intent = new Intent(CheckFloorActivity.this, QrScanActivity.class);
                intent.putExtra(Constants.TYPE, type);
                intent.putExtra(Constants.ADDRESS, address);
                startActivityForResult(intent, SCAN);
                break;
            case R.id.ll_empty:
                popupWindow.dismiss();
                showProgressDialog(getString(R.string.commit_data));
//                CheckRequest checkRequest = new CheckRequest(null, address, null);
//                sendRequest(checkRequest, CheckResponse.class);
                CheckStartRequest checkStartRequest = new CheckStartRequest(address, realName, taskId + "", null, "0");
                sendRequest(checkStartRequest, CheckStartResponse.class);
                break;
            case R.id.ll_wrong:
                popupWindow.dismiss();
                if (checkFloorInfo != null) {
                    ViewGroup detailView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alert_detail, null);
                    TextView tv_act_mark = (TextView) detailView.findViewById(R.id.tv_act_mark);
                    TextView tv_house_mark = (TextView) detailView.findViewById(R.id.tv_house_mark);
                    tv_act_mark.setText(checkFloorInfo.getIdentification());
                    tv_house_mark.setText(checkFloorInfo.getDbIdenfication());
                    AlertView alertView = new AlertView(checkFloorInfo.getActPositionCode(), null, null, new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            switch (position) {
                                case 0:
                                    break;
                            }
                        }
                    });
                    alertView.addExtView(detailView);
                    alertView.show();
                }
                break;
            case R.id.btn_confirm:
                for (CheckFloorInfo floorInfo : floorList) {
                    String address = floorInfo.getActPositionCode();
                    CheckStartRequest checkStartQuickRequest = new CheckStartRequest(address, realName, taskId + "", null, "0");
                    sendRequest(checkStartQuickRequest, CheckStartResponse.class);
                }
                break;
        }
    }

    private void showProgressDialog(String content) {
        customProgressDialog = new CustomProgressDialog(this, content);
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
                        CommonUtils.showToast(CheckFloorActivity.this, getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }
}
