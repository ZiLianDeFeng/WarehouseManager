package com.hgad.warehousemanager.ui.activity;

import android.view.View;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.BaseRequest;

/**
 * Created by Administrator on 2017/7/20.
 */
public class FaroriteActivity extends BaseActivity{
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_favorite);
    }

    @Override
    protected void initData() {
        initHeader("我的收藏");
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {

    }
}
