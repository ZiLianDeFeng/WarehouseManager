package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.UserInfo;
import com.hgad.warehousemanager.bean.request.AddMemberRequest;
import com.hgad.warehousemanager.bean.request.GetMemberListRequest;
import com.hgad.warehousemanager.bean.response.AddMemberResponse;
import com.hgad.warehousemanager.bean.response.GetMemberListResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.AddMemberAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */
public class AddMemberActivity extends BaseActivity {

    private ListView lv_add;
    private List<UserInfo> userInfoList = new ArrayList<>();
    private AddMemberAdapter addMemberAdapter;
    private String addType;
    private List<UserInfo> hasList;
    private int groupId;
    private CustomProgressDialog customProgressDialog;
    private boolean isConnect;
    private Handler handler = new Handler();

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_add_member);
    }

    @Override
    protected void initData() {
//        for (int i = 0; i < 10; i++) {
//            userInfoList.add(new UserInfo(i, "test" + i, false));
//        }
        Intent intent = getIntent();
        hasList = (List<UserInfo>) intent.getSerializableExtra(Constants.HAS_ADD);
        addType = intent.getStringExtra(Constants.ADD_TYPE);
        groupId = intent.getIntExtra(Constants.GROUP_ID, 0);
        if (Constants.HEADER.equals(addType)) {
            initHeader("设置组长");
        } else if (Constants.MEMBER.equals(addType)) {
            initHeader("添加组员");
        }
        getMemberList();
    }

    private void getMemberList() {
        GetMemberListRequest getMemberListRequest = new GetMemberListRequest();
        sendRequest(getMemberListRequest, GetMemberListResponse.class);
    }

    @Override
    protected void initView() {
        lv_add = (ListView) findViewById(R.id.lv_add_member);
        addMemberAdapter = new AddMemberAdapter(userInfoList, this);
        lv_add.setAdapter(addMemberAdapter);
        TextView tv_confirm = (TextView) findViewById(R.id.btn_confirm);
        tv_confirm.setVisibility(View.VISIBLE);
        tv_confirm.setText("确定");
        tv_confirm.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
            isConnect = true;
        }
        if (request instanceof AddMemberRequest) {
            AddMemberResponse addMemberResponse = (AddMemberResponse) response;
            setResult(Constants.RESULT_OK);
            finish();
        } else if (request instanceof GetMemberListRequest) {
            GetMemberListResponse getMemberListResponse = (GetMemberListResponse) response;
            if (getMemberListResponse.getResponseCode() != null) {
                if (getMemberListResponse.getResponseCode().getCode() == 200) {
                    List<GetMemberListResponse.DataEntity> data = getMemberListResponse.getData();
                    if (data != null) {
                        for (GetMemberListResponse.DataEntity dataEntity : data) {
                            UserInfo userInfo = new UserInfo();
                            userInfo.setData(dataEntity);
                            userInfoList.add(userInfo);
                        }
                        for (int i = 0; i < hasList.size(); i++) {
                            for (int j = 0; j < userInfoList.size(); j++) {
                                if (hasList.get(i).getId() == (userInfoList.get(j).getId())) {
                                    userInfoList.remove(j);
                                    break;
                                }
                            }
                        }
                        addMemberAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                add();
                break;
        }
    }

    private void add() {
        List<UserInfo> addList = new ArrayList<>();
        for (int i = 0; i < userInfoList.size(); i++) {
            if (userInfoList.get(i).isChecked()) {
                addList.add(userInfoList.get(i));
            }
        }
        if (addList.size() == 0) {
            CommonUtils.showToast(this, "未选择人员");
            return;
        }
        if (Constants.HEADER.equals(addType)) {
            if (addList.size() > 1 || addList.size() == 0) {
                CommonUtils.showToast(this, "只能选择一人为班组长！");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA, (Serializable) addList);
            setResult(Constants.RESULT_OK, intent);
            finish();
        } else if (Constants.MEMBER.equals(addType)) {
            StringBuilder names = new StringBuilder();
            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < addList.size(); i++) {
                if (i == addList.size() - 1) {
                    names.append(addList.get(i).getRealName());
                    ids.append(addList.get(i).getId());
                } else {
                    names.append(addList.get(i).getRealName() + ",");
                    ids.append(addList.get(i).getId() + ",");
                }
            }
            String addNames = names.toString();
            String addIds = ids.toString();
            showDialog(getString(R.string.commit_data));
            AddMemberRequest addMemberRequest = new AddMemberRequest(groupId + "", addNames, addIds);
            sendRequest(addMemberRequest, AddMemberResponse.class);
        }
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
                        CommonUtils.showToast(AddMemberActivity.this, getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }
}
