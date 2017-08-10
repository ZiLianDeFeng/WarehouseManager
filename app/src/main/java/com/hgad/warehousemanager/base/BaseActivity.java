package com.hgad.warehousemanager.base;

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
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//        builder.setTitle("提示").setMessage("是否确认返回");
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                finish();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setCancelable(false);
//        alertDialog.show();

        new AlertView("提示", "是否确认返回", "取消", new String[]{"确认"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        finish();
                        break;
                }
            }
        }).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getApplication().addActivity(this);
        setTextSize();
        setContentView();
        initView();
        initData();
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

