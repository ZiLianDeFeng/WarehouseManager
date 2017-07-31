package com.hgad.warehousemanager.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseFragment;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.ui.activity.SettingActivity;

/**
 * Created by Administrator on 2017/6/26.
 */
public class UserFragment extends BaseFragment {

    private View mView;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        ((ImageView) mView.findViewById(R.id.iv_setting)).setOnClickListener(this);
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_user, null);
        return mView;
    }

    @Override
    public <Res extends BaseResponse> void onSuccessResult(BaseRequest request, Res response) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                go2Setting();
                break;
        }
    }

    private void go2Setting() {
        Intent intent = new Intent(mContext, SettingActivity.class);
        startActivity(intent);
    }
}
