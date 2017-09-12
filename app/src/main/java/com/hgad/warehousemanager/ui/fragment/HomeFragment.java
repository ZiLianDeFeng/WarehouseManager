package com.hgad.warehousemanager.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseFragment;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.activity.ChangeWareActivity;
import com.hgad.warehousemanager.ui.activity.CheckRecordActivity;
import com.hgad.warehousemanager.ui.activity.HistoryActivity;
import com.hgad.warehousemanager.ui.activity.InWareChooseActivity;
import com.hgad.warehousemanager.ui.activity.NotificationActivity;
import com.hgad.warehousemanager.ui.activity.OutWareActivity;
import com.hgad.warehousemanager.ui.activity.ReviewActivity;
import com.hgad.warehousemanager.ui.activity.ReviewResultActivity;
import com.hgad.warehousemanager.ui.activity.ScanResultActivity;
import com.hgad.warehousemanager.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class HomeFragment extends BaseFragment {
    private static final int SCAN = 199;

    private View mView;
    //    private ConvenientBanner<String> convenientBanner;
    private List<String> mImageList = new ArrayList<>();
    private TextView tv_out_review;
    private TextView tv_review_result;

    @Override
    protected void initData() {
        // TODO: 2017/8/14 根据用户权限在首页显示不同的功能
        if (Constants.NORMAL) {
            tv_out_review.setVisibility(View.GONE);
            tv_review_result.setVisibility(View.VISIBLE);
        } else {
            tv_out_review.setVisibility(View.VISIBLE);
            tv_review_result.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView() {
        mView.findViewById(R.id.tv_in_ware).setOnClickListener(this);
        mView.findViewById(R.id.tv_change_ware).setOnClickListener(this);
        mView.findViewById(R.id.tv_out_ware).setOnClickListener(this);
        mView.findViewById(R.id.tv_history).setOnClickListener(this);
        mView.findViewById(R.id.tv_check).setOnClickListener(this);
//        mView.findViewById(R.id.tv_task_notification).setOnClickListener(this);
        mView.findViewById(R.id.iv_scan).setOnClickListener(this);
        tv_out_review = (TextView) mView.findViewById(R.id.tv_out_review);
        tv_out_review.setOnClickListener(this);
        tv_review_result = (TextView) mView.findViewById(R.id.tv_review_result);
        tv_review_result.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
//        convenientBanner.startTurning(2000);
    }

    @Override
    public void onPause() {
        super.onPause();
//        convenientBanner.stopTurning();
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_home, null);
        return mView;
    }

    @Override
    public <Res extends BaseResponse> void onSuccessResult(BaseRequest request, Res response) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                Intent intent = new Intent(mContext, ScanResultActivity.class);
                intent.putExtra(Constants.SCAN_RESULT, result);
                intent.putExtra(Constants.TYPE, Constants.SCAN_RESULT);
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
            case R.id.tv_history:
                go2History();
                break;
            case R.id.tv_check:
                go2Check();
                break;
//            case R.id.tv_task_notification:
//                go2Notification();
//                break;
            case R.id.iv_scan:
                go2Scan();
                break;
            case R.id.tv_out_review:
                go2Review();
                break;
            case R.id.tv_review_result:
                go2ReviewResult();
                break;
        }
    }

    private void go2History() {
        Intent intent = new Intent(mContext, HistoryActivity.class);
        startActivity(intent);
    }

    private void go2ReviewResult() {
        Intent intent = new Intent(mContext, ReviewResultActivity.class);
        startActivity(intent);
    }

    private void go2Review() {
        Intent intent = new Intent(mContext, ReviewActivity.class);
        startActivity(intent);
    }

    private void go2Notification() {
        Intent intent = new Intent(mContext, NotificationActivity.class);
        startActivity(intent);
    }

    private void go2Scan() {
        Intent intent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(intent, SCAN);
    }

    private void go2ChangeWare() {
        Intent intent = new Intent(mContext, ChangeWareActivity.class);
        startActivity(intent);
    }

    private void go2Check() {
        Intent intent = new Intent(mContext, CheckRecordActivity.class);
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
