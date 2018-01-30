package com.hgad.warehousemanager.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.response.ErrorResponseInfo;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.Callback;
import com.hgad.warehousemanager.net.NetUtil;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;


public abstract class BaseActivity extends AppCompatActivity implements Callback<BaseResponse>, View.OnClickListener {
    private int states = 3;
    private AlertView alertView;

    protected void initHeader(String text) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.setText(text);
        }
    }

    public void back(View view) {
        onBackPressed();
    }

    public void backWarm() {
        if (!alertView.isShowing()) {
            alertView.show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getApplication().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTextSize();
        setContentView();
        initView();
        initData();
        initBackDialog();
    }

    private void initBackDialog() {
        alertView = new AlertView("提示", "是否确认返回", "取消", new String[]{"确认"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        finish();
                        break;
                }
            }
        }).setCancelable(false);
    }

    private void setTextSize() {
        int size = SPUtils.getInt(this, SPConstants.TEXT_SIZE);
        if (size != 0) {
            states = size;
        }
        if (1 == states) {
            setTheme(R.style.Default_TextSize_Small);
        } else if (2 == states) {
            setTheme(R.style.Default_TextSize_Middle);
        } else {
            setTheme(R.style.Default_TextSize_Big);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
////        setTextSize();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getApplication().removeActivity(this);
    }

    protected abstract void setContentView();

    protected abstract void initData();

    protected abstract void initView();

    public void sendRequest(BaseRequest request, Class<? extends BaseResponse> responseClass) {
        boolean netWork = CommonUtils.checkNetWork(this);
        if (netWork) {
            NetUtil.sendRequest(request, responseClass, this);
        } else {
            CommonUtils.showToast(this, getString(R.string.check_net));
        }
    }

    public void cancel(){
        NetUtil.cancel();
    };

    public abstract void onSuccessResult(BaseRequest request, BaseResponse response);


    @Override
    public void onSuccess(BaseRequest request, BaseResponse response) {
        onSuccessResult(request, response);
    }


    /*

        所有的错误吗处理都在这个地方
     */
    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {
        if (errorResponseInfo != null) {
//            CommonUtils.showToast(BaseActivity.this, "网络不稳定！");
            //没有登录，跳转到登录界面

//            if ("1533".equals(errorResponseInfo.error_code)) {
//
//                Toast.makeText(this, "需要去登录", Toast.LENGTH_SHORT).show();
//
//            }
        }
    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }
}

