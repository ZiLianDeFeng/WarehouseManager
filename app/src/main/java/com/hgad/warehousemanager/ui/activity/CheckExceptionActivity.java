package com.hgad.warehousemanager.ui.activity;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.CheckExceptionInfo;
import com.hgad.warehousemanager.bean.CheckFloorInfo;
import com.hgad.warehousemanager.bean.request.CheckFloorDetailRequest;
import com.hgad.warehousemanager.bean.response.CheckFloorDetailResponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.CheckExceptionAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */
public class CheckExceptionActivity extends BaseActivity {

    private RecyclerView rl;
    private CheckExceptionAdapter checkExceptionAdapter;
    private CustomProgressDialog customProgressDialog;
    private boolean isConnect;
    private Handler handler = new Handler();
    private int taskId;
    private List<CheckFloorInfo> floorInfos = new ArrayList<>();
    private List<CheckExceptionInfo> checkExceptionInfos = new ArrayList<>();
    private List<CheckExceptionInfo> showExceptionInfos = new ArrayList<>();
    private List<CheckExceptionInfo> overageExceptionInfos = new ArrayList<>();
    private List<CheckExceptionInfo> lossesExceptionInfos = new ArrayList<>();
    private List<CheckExceptionInfo> wrongExceptionInfos = new ArrayList<>();
    private RadioGroup rg_check;
    private RadioButton rb_losses;
    private RadioButton rb_ware_wrong;
    private RadioButton rb_overage;
    private Animation operatingAnim;
    private ImageView infoOperating;
    private RelativeLayout rl_info;
    private int checkedRadioButtonId;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_check_exception);
    }

    @Override
    protected void initData() {
        initHeader("盘点异常");
//        Intent intent = getIntent();
//        rb_overage.setChecked(true);
//        ((RadioButton) rg_check.getChildAt(0)).setChecked(true);
        checkedRadioButtonId = rb_overage.getId();
        callRefresh();
    }

    @Override
    protected void initView() {
        rl = (RecyclerView) findViewById(R.id.rl_check_exception);
        checkExceptionAdapter = new CheckExceptionAdapter(this, showExceptionInfos);
        checkExceptionAdapter.setOnRefreshListener(new CheckExceptionAdapter.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callRefresh();
            }
        });
        rl.setAdapter(checkExceptionAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                int itemViewType = checkExceptionAdapter.getItemViewType(position);
//                if (2 == itemViewType) {
//                    return 1;
//                } else if (1 == itemViewType) {
//                    return 1;
//                }
//                return 0;
//            }
//        });
        rl.setLayoutManager(gridLayoutManager);
        rg_check = (RadioGroup) findViewById(R.id.rg_check);
        rg_check.setOnCheckedChangeListener(checkChangeListener);
        rb_overage = (RadioButton) findViewById(R.id.rb_overage);
        rb_losses = (RadioButton) findViewById(R.id.rb_losses);
        rb_ware_wrong = (RadioButton) findViewById(R.id.rb_ware_wrong);
        initAnimation();
    }

    private void initAnimation() {
        rl_info = (RelativeLayout) findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    private RadioGroup.OnCheckedChangeListener checkChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_overage:
                    showExceptionInfos.clear();
                    showWaiting();
                    showExceptionInfos.addAll(overageExceptionInfos);
                    checkExceptionAdapter.notifyDataSetChanged();
                    break;
                case R.id.rb_losses:
                    showExceptionInfos.clear();
                    showWaiting();
                    showExceptionInfos.addAll(lossesExceptionInfos);
                    checkExceptionAdapter.notifyDataSetChanged();
                    break;
                case R.id.rb_ware_wrong:
                    showExceptionInfos.clear();
                    showWaiting();
                    showExceptionInfos.addAll(wrongExceptionInfos);
                    checkExceptionAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void showWaiting() {
        if (operatingAnim != null) {
            rl_info.setVisibility(View.VISIBLE);
            infoOperating.startAnimation(operatingAnim);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_info.setVisibility(View.INVISIBLE);
                infoOperating.clearAnimation();
            }
        }, 1000);
    }

    private void callRefresh() {
        showProgressDialog(getString(R.string.info_check));
        CheckFloorDetailRequest checkFloorDetailRequest = new CheckFloorDetailRequest(null, null);
        sendRequest(checkFloorDetailRequest, CheckFloorDetailResponse.class);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
        isConnect = true;
        if (request instanceof CheckFloorDetailRequest) {
            CheckFloorDetailResponse checkFloorDetailResponse = (CheckFloorDetailResponse) response;
            if (checkFloorDetailResponse.getResponseCode().getCode() == 200) {
                floorInfos.clear();
                List<CheckFloorDetailResponse.DataEntity> data = checkFloorDetailResponse.getData();
                for (int i = 0; i < data.size(); i++) {
                    CheckFloorDetailResponse.DataEntity dataEntity = data.get(i);
                    String actPositionCode = dataEntity.getPositionCode();
                    String identification = dataEntity.getRealIdenification();
                    String positionCode = dataEntity.getPositionCode();
                    String id = dataEntity.getId() + "";
                    CheckFloorInfo checkFloorInfo = new CheckFloorInfo(id, actPositionCode, identification, positionCode);
                    if (!TextUtils.isEmpty(identification)) {
                        if (dataEntity.getType() != null) {
                            checkFloorInfo.setState(dataEntity.getType());
                            floorInfos.add(checkFloorInfo);
                        }
                    }
                }
                if (checkExceptionInfos != null) {
                    checkExceptionInfos.clear();
                }
                List<CheckExceptionInfo> exceptionInfos = CheckExceptionInfo.getData(floorInfos);
                checkExceptionInfos.addAll(exceptionInfos);
                overageExceptionInfos.clear();
                lossesExceptionInfos.clear();
                wrongExceptionInfos.clear();
                for (CheckExceptionInfo checkExceptionInfo : checkExceptionInfos) {
                    if (checkExceptionInfo.getCheckFloorInfo().getState().equals("0")) {
                        overageExceptionInfos.add(checkExceptionInfo);
                    }
                    if (checkExceptionInfo.getCheckFloorInfo().getState().equals("1")) {
                        lossesExceptionInfos.add(checkExceptionInfo);
                    }
                    if (checkExceptionInfo.getCheckFloorInfo().getState().equals("2")) {
                        wrongExceptionInfos.add(checkExceptionInfo);
                    }
                }
                rb_overage.setText("盘盈" + "(" + (overageExceptionInfos.size() == 0 ? 0 : overageExceptionInfos.size() - 1) + ")");
                rb_losses.setText("盘亏" + "(" + (lossesExceptionInfos.size() == 0 ? 0 : lossesExceptionInfos.size() - 1) + ")");
                rb_ware_wrong.setText("货位有误" + "(" + (wrongExceptionInfos.size() == 0 ? 0 : wrongExceptionInfos.size() - 1) + ")");
                int checkId = rg_check.getCheckedRadioButtonId();
                if (checkId != -1) {
                    checkedRadioButtonId = checkId;
                }
                switch (checkedRadioButtonId) {
                    case R.id.rb_overage:
                        rb_overage.setChecked(false);
                        rb_overage.setChecked(true);
                        break;
                    case R.id.rb_losses:
                        rb_losses.setChecked(false);
                        rb_losses.setChecked(true);
                        break;
                    case R.id.rb_ware_wrong:
                        rb_ware_wrong.setChecked(false);
                        rb_ware_wrong.setChecked(true);
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void showProgressDialog(String content) {
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
                        CommonUtils.showToast(CheckExceptionActivity.this, getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }
}
