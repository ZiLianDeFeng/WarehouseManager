package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.OrderAdapter;
import com.hgad.warehousemanager.ui.fragment.OutWareFragment;
import com.hgad.warehousemanager.ui.fragment.ReviewFragment;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.zxing.activity.ScannerActivity;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/6/29.
 */
public class OutWareActivity extends BaseActivity {
    private static final int SCAN = 199;
    private XListView lv;
    private List<OrderInfo> data = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private EditText et_order_num;
    private SwipeRefreshLayout swipeRefreshView;
    private Animation operatingAnim;
    private RelativeLayout rl_info;
    private ImageView infoOperating;

    private LinearLayout ll_more;
    private PopupWindow morePopupWindow;

    //    private RadioGroup rg_title;
//    private RadioButton rb_out_ware;
//    private RadioButton rb_review;
    private OutWareFragment outWareFragment;
    private ReviewFragment reviewFragment;
    private RadioGroup rg_out;
    private RadioButton rb_all;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_outware);
    }

    @Override
    protected void initData() {
        initHeader("出库");
//        Intent intent = getIntent();
//        String orderNum = intent.getStringExtra(Constants.ORDER_NUMBER);
//        ((RadioButton) rg_title.getChildAt(0)).setChecked(true);
        replaceFragment(outWareFragment);
        rb_all.setChecked(true);
    }

    @Override
    protected void initView() {
//        rg_title = (RadioGroup) findViewById(R.id.rg_title);
//        rb_out_ware = (RadioButton) findViewById(R.id.rb_out_ware);
//        rb_review = (RadioButton) findViewById(R.id.rb_review);
//        rg_title.setOnCheckedChangeListener(listener);
        outWareFragment = new OutWareFragment();
        rg_out = (RadioGroup) findViewById(R.id.rg_out);
        rg_out.setOnCheckedChangeListener(checkChangeListener);
        rb_all = (RadioButton) findViewById(R.id.rb_all);
//        RadioButton rb_should_out = (RadioButton) findViewById(R.id.rb_should_out);
//        RadioButton rb_done_out = (RadioButton) findViewById(R.id.rb_done_out);
//        reviewFragment = new ReviewFragment();
//        et_order_num = (EditText) findViewById(R.id.et_order_num);
//        findViewById(R.id.btn_find).setOnClickListener(this);
//        TextView tv_review = (TextView) findViewById(R.id.btn_confirm);
//        tv_review.setText("复核");
//        tv_review.setOnClickListener(this);
//        tv_review.setVisibility(View.VISIBLE);
    }

    private String type;
    private RadioGroup.OnCheckedChangeListener checkChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_all:
                    type = "'1','2'";
                    if (outWareFragment.isAdded()) {
                        outWareFragment.setType(type);
                    }
                    break;
                case R.id.rb_should_out:
                    type = "'1'";
                    if (outWareFragment.isAdded()) {
                        outWareFragment.setType(type);
                    }
                    break;
                case R.id.rb_done_out:
                    type = "'2'";
                    if (outWareFragment.isAdded()) {
                        outWareFragment.setType(type);
                    }
                    break;
            }
        }
    };

    //    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            switch (checkedId) {
//                case R.id.rb_out_ware:
//                    if (outWareFragment.isAdded()) {
//                        replaceFragment(reviewFragment, outWareFragment);
//                    } else {
//                        replaceFragment(outWareFragment);
//                    }
//                    break;
//                case R.id.rb_review:
//                    if (reviewFragment.isAdded()) {
//                        replaceFragment(outWareFragment, reviewFragment);
//                    } else {
//                        replaceFragment(reviewFragment);
//                    }
//                    break;
//            }
//        }
//    };
//
//    public void replaceFragment(Fragment from, Fragment to) {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
//                .beginTransaction();
//        if (!to.isAdded()) {    // 先判断是否被add过
//            fragmentTransaction.hide(from).add(R.id.fl, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
//        } else {
//            fragmentTransaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
//        }
//    }
//
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl, fragment)
                .commit();
    }

    private void initMorePopupWindow() {
        View contentView = View.inflate(this, R.layout.popupwindow_inware, null);
        morePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        morePopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000"))); //设置背景
        morePopupWindow.setFocusable(true); //设置获取焦点
//        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        morePopupWindow.setOutsideTouchable(true);
        morePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.backgroundAlpha(1f, OutWareActivity.this);
            }
        });
        TextView tv_hand = (TextView) contentView.findViewById(R.id.tv_hand);
        tv_hand.setText("手动出库");
        contentView.findViewById(R.id.ll_in_hand).setOnClickListener(this);
        contentView.findViewById(R.id.ll_in_scan).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String resultStr = bundle.getString("result");
                Intent intent = new Intent(this, ScanResultActivity.class);
                intent.putExtra(Constants.SCAN_RESULT, resultStr);
                intent.putExtra(Constants.TYPE, Constants.OUT_WARE);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find:
                search();
                break;
            case R.id.ll_search:
                showMore();
                break;
            case R.id.ll_in_hand:
                go2InHand();
                break;
            case R.id.ll_in_scan:
                go2Scan();
                break;
//            case R.id.btn_confirm:
//                go2Review();
//                break;
        }
    }

    private void go2Review() {
        Intent intent = new Intent(this, ReviewListActivity.class);
        startActivity(intent);
    }

    private void showMore() {
        morePopupWindow.showAsDropDown(ll_more);
        CommonUtils.backgroundAlpha(0.8f, this);
    }

    private void go2Scan() {
        morePopupWindow.dismiss();
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivityForResult(intent, SCAN);
    }

    private void go2InHand() {
        morePopupWindow.dismiss();
        Intent intent = new Intent(this, InWareByHandActivity.class);
        intent.putExtra(Constants.TYPE, Constants.OUT_WARE);
        startActivity(intent);
    }

    private void search() {
        String orderNum = et_order_num.getText().toString().trim();
//        if (TextUtils.isEmpty(orderNum)) {
//            CommonUtils.showToast(this, "未输入订单号哦！");
//            return;
//        }
//        if (rb_out_ware.isChecked()) {
        outWareFragment.search(orderNum);
//        } else {
//            reviewFragment.search(orderNum);
//        }
    }
}
