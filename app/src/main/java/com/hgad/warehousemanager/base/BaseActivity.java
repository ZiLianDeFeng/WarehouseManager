package com.hgad.warehousemanager.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.response.ErrorResponseInfo;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.Callback;
import com.hgad.warehousemanager.net.NetUtil;


public abstract class BaseActivity extends AppCompatActivity implements Callback<BaseReponse>,View.OnClickListener{


    protected void initHeader(String text) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(text);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void backWarm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("提示").setMessage("是否确认返回");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getApplication().addActivity(this);
        setContentView();
        initView();
        initData();
    }

    protected abstract void setContentView();

    protected abstract void initData();

    protected abstract void initView();

    public void sendRequest(BaseRequest request, Class<? extends BaseReponse> responseClass) {

        NetUtil.sendRequest(request, responseClass, this);
    }

    public abstract void onSuccessResult(BaseRequest request, BaseReponse response);


    @Override
    public void onSuccess(BaseRequest request, BaseReponse response) {

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

