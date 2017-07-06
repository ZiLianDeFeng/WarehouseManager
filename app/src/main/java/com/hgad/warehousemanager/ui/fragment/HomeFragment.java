package com.hgad.warehousemanager.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseFragment;
import com.hgad.warehousemanager.bean.UserInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.db.dao.BaseDaoImpl;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.ui.activity.ChangeWareActivity;
import com.hgad.warehousemanager.ui.activity.CheckActivity;
import com.hgad.warehousemanager.ui.activity.InWareChooseActivity;
import com.hgad.warehousemanager.ui.activity.OutWareActivity;
import com.hgad.warehousemanager.ui.activity.ScanResultActivity;
import com.hgad.warehousemanager.zxing.activity.CaptureActivity;

/**
 * Created by Administrator on 2017/6/26.
 */
public class HomeFragment extends BaseFragment {
    private static final int SCAN = 199;

    private View mView;
    //    private UserDao userDao;
    private BaseDaoImpl<UserInfo, Integer> userDao;

    @Override
    protected void initData() {
//        userDao = new UserDao(mContext);
        userDao = new BaseDaoImpl<>(mContext, UserInfo.class);
    }

    @Override
    protected void initView() {
        ((TextView) mView.findViewById(R.id.tv_in_ware)).setOnClickListener(this);
        ((TextView) mView.findViewById(R.id.tv_change_ware)).setOnClickListener(this);
        ((TextView) mView.findViewById(R.id.tv_out_ware)).setOnClickListener(this);
        ((TextView) mView.findViewById(R.id.tv_code_scanning)).setOnClickListener(this);
        ((TextView) mView.findViewById(R.id.tv_check)).setOnClickListener(this);
        ((TextView) mView.findViewById(R.id.tv_user_setting)).setOnClickListener(this);
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_home, null);
        return mView;
    }

    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                Intent intent = new Intent(mContext, ScanResultActivity.class);
                intent.putExtra(Constants.SCAN_RESULT, result);
                intent.putExtra(Constants.TYPE,Constants.SCAN_RESULT);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_in_ware:
                go2InWare();
                break;
            case R.id.tv_change_ware:
                go2ChangeWare();
                break;
            case R.id.tv_out_ware:
                go2OutWare();
                break;
            case R.id.tv_code_scanning:
                go2Scan();
                break;
            case R.id.tv_check:
                go2Check();
                break;
            case R.id.tv_user_setting:
                break;
        }
    }

    private void go2Scan() {
        Intent intent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(intent,SCAN);
    }

    private void go2ChangeWare() {
        Intent intent = new Intent(mContext, ChangeWareActivity.class);
        startActivity(intent);
    }

    private void go2Check() {
        Intent intent = new Intent(mContext, CheckActivity.class);
        startActivity(intent);
    }

    private void go2OutWare() {
        Intent intent = new Intent(mContext, OutWareActivity.class);
        startActivity(intent);
    }

    private void go2InWare() {
        Intent intent = new Intent(mContext, InWareChooseActivity.class);
        startActivity(intent);
    }
}
