package com.hgad.warehousemanager.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */
public class ApplyOutActivity extends BaseActivity {

    private TextView tv_net_weight;
    private List<String> checkPeoples = new ArrayList<>();
    private TextView tv_urgency;
    private TextView tv_check_people;
    private String[] urgencys = new String[]{"非常紧急", "一般", "不紧急"};
    private CustomProgressDialog mProgressDialog;
    private TextView tv_apply_reason;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_apply_out);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        OrderInfo orderInfo = (OrderInfo) intent.getSerializableExtra(Constants.ORDER_INFO);
        String orderNum = orderInfo.getOrderNum();
        initHeader(orderNum);
        for (int i = 0; i < 6; i++) {
            checkPeoples.add("test" + i);
        }
        String totalWeight = orderInfo.getTotalWeight();
        tv_net_weight.setText(totalWeight);
    }

    @Override
    protected void initView() {
        tv_apply_reason = (TextView) findViewById(R.id.tv_apply_reason);
        tv_apply_reason.setOnClickListener(this);
        tv_urgency = (TextView) findViewById(R.id.tv_urgency);
        tv_urgency.setOnClickListener(this);
        tv_check_people = (TextView) findViewById(R.id.tv_check_people);
        tv_check_people.setOnClickListener(this);
        tv_net_weight = (TextView) findViewById(R.id.et_net_weight);
        findViewById(R.id.btn_commit).setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apply_reason:
                ViewGroup etView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null);
                final EditText et_reason = (EditText) etView.findViewById(R.id.et_reason);
                AlertView alertView = new AlertView("申请理由", null, "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                String reason = et_reason.getText().toString().trim();
                                tv_apply_reason.setText(reason);
                                break;
                        }
                    }
                });
                alertView.addExtView(etView).setCancelable(false).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showInputMethod();
                    }
                }, 100);
                break;
            case R.id.tv_urgency:
                new AlertView("选择紧急度", null, null, null, urgencys, this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        tv_urgency.setText(urgencys[position]);
                    }
                }).setCancelable(false).show();
                break;
            case R.id.tv_check_people:
                String[] strings = new String[checkPeoples.size()];
                checkPeoples.toArray(strings);
                new AlertView("选择送审人", null, null, null, strings, this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        tv_check_people.setText(checkPeoples.get(position));
                    }
                }).setCancelable(false).show();
                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    public void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    private void showInputMethod() {
        //自动弹出键盘
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //强制隐藏Android输入法窗口
        // inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0);
    }

    private void commit() {
        String applyReason = tv_apply_reason.getText().toString().trim();
        if (TextUtils.isEmpty(applyReason)) {
            CommonUtils.showToast(this, "未填写申请理由！");
            return;
        }
        String urgency = tv_urgency.getText().toString().trim();
        if (TextUtils.isEmpty(urgency)) {
            CommonUtils.showToast(this, "未选择紧急程度！");
            return;
        }
        String checkPeople = tv_check_people.getText().toString().trim();
        if (TextUtils.isEmpty(checkPeople)) {
            CommonUtils.showToast(this, "未选择送审人！");
            return;
        }
//        String netWeight = tv_net_weight.getText().toString().trim();
//        if (TextUtils.isEmpty(netWeight)) {
//            CommonUtils.showToast(this, "未填写订单总重量！");
//            return;
//        }
        boolean checkNetWork = CommonUtils.checkNetWork(this);
        if (checkNetWork) {
            mProgressDialog = new CustomProgressDialog(this, "申请提交中");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                        CommonUtils.showToast(ApplyOutActivity.this, "提交成功");
                        finish();
                    }
                }
            }, 2000);
        } else {
            CommonUtils.showToast(this, "请检查网络");
        }
    }
}
