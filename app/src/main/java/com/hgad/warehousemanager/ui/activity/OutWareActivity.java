package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.bean.UserInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.MemberAdapter;
import com.hgad.warehousemanager.ui.adapter.OrderAdapter;
import com.hgad.warehousemanager.ui.fragment.OutWareFragment;
import com.hgad.warehousemanager.ui.fragment.ReviewFragment;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.CommonDialog;
import com.hgad.warehousemanager.view.CustomProgressDialog;
import com.hgad.warehousemanager.zxing.activity.QrScanActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/6/29.
 */
public class OutWareActivity extends BaseActivity {
    private static final int SCAN = 199;
    private static final int HEAD_MAN = 330;
    private static final int ADD_MEMBER = 340;
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
    private FloatingActionButton fab_group;
    private String realName;
    private View popView;
    private Button btn_commit;
    private TextView tv_name;
    private EditText et_group_name;
    private MemberAdapter memberAdapter;
    private List<UserInfo> memberList = new ArrayList<>();
    private int measuredHeight;
    private PopupWindow groupPop;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler();
    private boolean isConnect;
    private List<Fragment> fragments = new ArrayList<>();

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
        addHideShow(outWareFragment);
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
        LinearLayout ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ll_search.setVisibility(View.VISIBLE);
        ll_search.setOnClickListener(this);
//        fab_group = (FloatingActionButton) findViewById(R.id.fab_group);
//        fab_group.setOnClickListener(this);
//        initGroupPopupwindow();
    }

    boolean inGroup = false;

    private void initGroupPopupwindow() {
        realName = SPUtils.getString(this, SPConstants.REAL_NAME);
        if (!inGroup) {
            popView = View.inflate(this, R.layout.popup_create_group, null);
            et_group_name = (EditText) popView.findViewById(R.id.et_group_name);
            tv_name = (TextView) popView.findViewById(R.id.tv_name);
            tv_name.setOnClickListener(this);
            tv_name.setText(realName);
            btn_commit = (Button) popView.findViewById(R.id.btn_commit);
            btn_commit.setOnClickListener(this);
        } else {
            popView = View.inflate(this, R.layout.popup_group, null);
            ListView lv_member = (ListView) popView.findViewById(R.id.lv_member);
            memberAdapter = new MemberAdapter(this, memberList);
            lv_member.setAdapter(memberAdapter);
            popView.findViewById(R.id.tv_add_member).setOnClickListener(this);
            popView.findViewById(R.id.tv_delete_group).setOnClickListener(this);
        }
        popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        measuredHeight = popView.getMeasuredHeight();
        groupPop = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        groupPop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000"))); //设置背景
        groupPop.setFocusable(true); //设置获取焦点
//        groupPop.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//        groupPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        groupPop.setAnimationStyle(R.style.GroupPopupWindowAnimation);
        groupPop.setOutsideTouchable(true);
        groupPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
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

    // 第一次会创建，后续不会销毁，也不会创建
    private void addHideShow(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!fragments.contains(fragment)) {
            fragmentTransaction.add(R.id.fl, fragment);
            fragments.add(fragment);
        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl, fragment)
                .commit();
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
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
        super.onActivityResult(requestCode, resultCode, data);
//        outWareFragment.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String resultStr = bundle.getString("result");
                Intent intent = new Intent(this, ScanResultActivity.class);
                String codeType = bundle.getString("codeType");
                intent.putExtra(Constants.CODE_TYPE, codeType);
                intent.putExtra(Constants.SCAN_RESULT, resultStr);
                intent.putExtra(Constants.TYPE, Constants.OUT_WARE);
                startActivity(intent);
            }
//        } else if (resultCode == Constants.RESULT_OK && requestCode == ADD_MEMBER) {
//            List<UserInfo> addList = (List<UserInfo>) data.getSerializableExtra(Constants.DATA);
//            for (int i = 0; i < addList.size(); i++) {
//                UserInfo userInfo = addList.get(i);
//                userInfo.setChecked(false);
//                memberList.add(userInfo);
//            }
//            memberAdapter.notifyDataSetChanged();
//        } else if (resultCode == Constants.RESULT_OK && requestCode == HEAD_MAN) {
//            List<UserInfo> addList = (List<UserInfo>) data.getSerializableExtra(Constants.DATA);
//            UserInfo userInfo = addList.get(0);
//            String realName = userInfo.getRealName();
//            tv_name.setText(realName);
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
//                showMore();
                toSearch();
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

//            case R.id.fab_group:
//                showGroup();
//                break;
//            case R.id.tv_add_member:
//                addMember();
//                break;
//            case R.id.tv_name:
//                setHeadman();
//                break;
//            case R.id.btn_commit:
//                creatGroup();
//                break;
//            case R.id.tv_delete_group:
//                deletGroup();
//                break;
        }
    }

    private void deletGroup() {
        CommonDialog commonDialog = new CommonDialog(this, "提示", "确认解散班组?", "确认", "取消");
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.setCancelable(true);
        commonDialog.setClicklistener(new CommonDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                showDialog(getString(R.string.commit_data));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        customProgressDialog.dismiss();
                        isConnect = true;
                        CommonUtils.showToast(OutWareActivity.this, "解散成功");
                        inGroup = false;
                        groupPop.dismiss();
                        initGroupPopupwindow();
                        showGroup();
                    }
                }, 1000);
            }

            @Override
            public void doCancel() {

            }
        });
        commonDialog.show();
    }

    private void setHeadman() {
        Intent intent = new Intent(this, AddMemberActivity.class);
        intent.putExtra(Constants.ADD_TYPE, Constants.HEADER);
        intent.putExtra(Constants.HAS_ADD, ((Serializable) memberList));
        startActivityForResult(intent, HEAD_MAN);
    }

    private void creatGroup() {
        String groupName = et_group_name.getText().toString().trim();
        if (TextUtils.isEmpty(groupName)) {
            CommonUtils.showToast(this, "未填写班组名称");
            return;
        }
        showDialog(getString(R.string.commit_data));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                customProgressDialog.dismiss();
                isConnect = true;
                memberList.clear();
                CommonUtils.showToast(OutWareActivity.this, "创建成功");
                inGroup = true;
                memberList.add(new UserInfo(0, "test0", false));
                groupPop.dismiss();
                initGroupPopupwindow();
                showGroup();
            }
        }, 1000);
    }

    private void addMember() {
        Intent intent = new Intent(this, AddMemberActivity.class);
        intent.putExtra(Constants.ADD_TYPE, Constants.MEMBER);
        intent.putExtra(Constants.HAS_ADD, ((Serializable) memberList));
        startActivityForResult(intent, ADD_MEMBER);
    }

    private void showGroup() {
//        Intent intent = new Intent(this, GroupActivity.class);
//        startActivity(intent);
        backgroundAlpha(0.8f);
        groupPop.showAsDropDown(fab_group, 0, -(fab_group.getHeight() + measuredHeight));
    }


    private void toSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
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
        Intent intent = new Intent(this, QrScanActivity.class);
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

    private void showDialog(String content) {
        customProgressDialog = new CustomProgressDialog(this, content);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        isConnect = false;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isConnect) {
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                        CommonUtils.showToast(OutWareActivity.this, getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }
}
