package com.hgad.warehousemanager.ui.activity;

import android.support.v4.app.Fragment;
import android.view.View;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.fragment.ReviewFragment;

/**
 * Created by Administrator on 2017/8/21.
 */
public class ReviewListActivity extends BaseActivity {

    private ReviewFragment reviewFragment;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_review_list);
    }

    @Override
    protected void initData() {
        initHeader("复核");
        replaceFragment(reviewFragment);
    }

    @Override
    protected void initView() {
        reviewFragment = new ReviewFragment();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl, fragment)
                .commit();
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {
    }
}
