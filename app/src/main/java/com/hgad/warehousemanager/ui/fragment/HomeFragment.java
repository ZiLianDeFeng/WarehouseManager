package com.hgad.warehousemanager.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseFragment;
import com.hgad.warehousemanager.bean.UserInfo;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.CreateGroupRequest;
import com.hgad.warehousemanager.bean.request.DeletMemberRequest;
import com.hgad.warehousemanager.bean.request.GroupDeleteRequest;
import com.hgad.warehousemanager.bean.request.GroupStateRequest;
import com.hgad.warehousemanager.bean.request.HomeDataRequest;
import com.hgad.warehousemanager.bean.response.CreateGroupResponse;
import com.hgad.warehousemanager.bean.response.DeletMemberResponse;
import com.hgad.warehousemanager.bean.response.GroupDeleteResponse;
import com.hgad.warehousemanager.bean.response.GroupStateResponse;
import com.hgad.warehousemanager.bean.response.HomeDataResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.db.dao.BaseDaoImpl;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.activity.AddMemberActivity;
import com.hgad.warehousemanager.ui.activity.CheckRecordActivity;
import com.hgad.warehousemanager.ui.activity.DataStatisticsActivity;
import com.hgad.warehousemanager.ui.activity.HistoryActivity;
import com.hgad.warehousemanager.ui.activity.NotificationActivity;
import com.hgad.warehousemanager.ui.activity.OperateResultActivity;
import com.hgad.warehousemanager.ui.activity.OutWareActivity;
import com.hgad.warehousemanager.ui.activity.ReviewActivity;
import com.hgad.warehousemanager.ui.activity.ReviewResultActivity;
import com.hgad.warehousemanager.ui.activity.ScanResultActivity;
import com.hgad.warehousemanager.ui.adapter.MemberAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.CommonDialog;
import com.hgad.warehousemanager.view.CustomProgressDialog;
import com.hgad.warehousemanager.zxing.activity.CaptureActivity;
import com.hgad.warehousemanager.zxing.activity.QrScanActivity;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class HomeFragment extends BaseFragment {
    private static final int IN_SCAN = 199;
    private static final int CHANGE_SCAN = 220;
    private static final int RESULT = 330;
    private static final int HEAD_MAN = 400;
    private static final int ADD_MEMBER = 410;
    private View mView;
    //    private ConvenientBanner<String> convenientBanner;
    private TextView tv_out_review;
    private TextView tv_review_result;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.IN_WARE) || action.equals(Constants.CHANGE_WARE)) {
                type = intent.getStringExtra(Constants.TYPE);
                toResult(type);
            }
        }
    };
    private IntenterBoradCastReceiver netReceiver;
    private BaseDaoImpl<WareInfo, Integer> wareDao;
    private TextView tv_check_count;
    private TextView tv_in_count;
    private TextView tv_out_count;
    private TextView tv_today_in_count;
    private String userName;
    private String type;
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
    private int userId;
    private TextView tv_group_name;
    private TextView tv_create_date;
    private int groupId;
    private ListView lv_member;
    private boolean forInware;
    private String post;

    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.IN_WARE);
        intentFilter.addAction(Constants.CHANGE_WARE);
        mContext.registerReceiver(receiver, intentFilter);
    }

    //监听网络状态变化的广播接收器
    public class IntenterBoradCastReceiver extends BroadcastReceiver {

        private ConnectivityManager mConnectivityManager;
        private NetworkInfo netInfo;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    //网络连接
                    String name = netInfo.getTypeName();
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        //WiFi网络
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        //有线网络
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        //3g网络
                    }
                    try {
                        commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    //网络断开
                }
            }

        }
    }

    private void registerNetBroadrecevicer() {
        //获取广播对象
        netReceiver = new IntenterBoradCastReceiver();
        //创建意图过滤器
        IntentFilter filter = new IntentFilter();
        //添加动作，监听网络
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(netReceiver, filter);
    }

    private void commit() throws SQLException {
        List<WareInfo> wareInfos = wareDao.queryAll();
        if (wareInfos != null && wareInfos.size() != 0) {
            new AlertView("提示", "有未提交的入库货品，是否现在去提交", "等会再说", new String[]{"现在就去"}, null, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    switch (position) {
                        case 0:
                            go2Syn();
                            break;
                    }
                }
            }).setCancelable(true).show();
        }
    }

    @Override
    protected void initData() {
//        if (Constants.NORMAL) {
//            tv_out_review.setVisibility(View.GONE);
//            tv_review_result.setVisibility(View.VISIBLE);
//        } else {
//            tv_out_review.setVisibility(View.VISIBLE);
//            tv_review_result.setVisibility(View.GONE);
//        }
        userName = SPUtils.getString(mContext, SPConstants.USER_NAME);
        post = SPUtils.getString(mContext, SPConstants.POST);
        userId = SPUtils.getInt(mContext, SPConstants.USER_ID);
        wareDao = new BaseDaoImpl<>(mContext, WareInfo.class);
        refreshData();
//        getGroupState();
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
        mView.findViewById(R.id.tv_data_syn).setOnClickListener(this);
        tv_out_review = (TextView) mView.findViewById(R.id.tv_out_review);
        tv_out_review.setOnClickListener(this);
        tv_review_result = (TextView) mView.findViewById(R.id.tv_review_result);
        tv_review_result.setOnClickListener(this);
        tv_check_count = (TextView) mView.findViewById(R.id.tv_check_count);
        tv_in_count = (TextView) mView.findViewById(R.id.tv_in_count);
        tv_out_count = (TextView) mView.findViewById(R.id.tv_out_count);
        tv_today_in_count = (TextView) mView.findViewById(R.id.tv_today_in_count);
        fab_group = (FloatingActionButton) mView.findViewById(R.id.fab_group);
        fab_group.setOnClickListener(this);
//        initGroupPopupwindow();
    }

    boolean inGroup = false;

    private void initGroupPopupwindow() {
        realName = SPUtils.getString(mContext, SPConstants.REAL_NAME);
        if (!inGroup) {
            popView = View.inflate(mContext, R.layout.popup_create_group, null);
            et_group_name = (EditText) popView.findViewById(R.id.et_group_name);
            tv_name = (TextView) popView.findViewById(R.id.tv_name);
            tv_name.setOnClickListener(null);
            tv_name.setText(realName);
            btn_commit = (Button) popView.findViewById(R.id.btn_commit);
            btn_commit.setOnClickListener(this);
        } else {
            popView = View.inflate(mContext, R.layout.popup_group, null);
            lv_member = (ListView) popView.findViewById(R.id.lv_member);
            if (memberAdapter == null) {
                memberAdapter = new MemberAdapter(mContext, memberList);
                memberAdapter.setPost(post);
                memberAdapter.setOnDeleteMemberListener(new MemberAdapter.OnDeleteMemberListener() {
                    @Override
                    public void onDeletMember(UserInfo userInfo) {
                        DeletMemberRequest deletMemberRequest = new DeletMemberRequest(groupId + "", userInfo.getRealName(), userInfo.getId() + "");
                        sendRequest(deletMemberRequest, DeletMemberResponse.class);
                    }
                });
            }
            lv_member.setAdapter(memberAdapter);
            popView.findViewById(R.id.tv_add_member).setOnClickListener(this);
            popView.findViewById(R.id.tv_delete_group).setOnClickListener(this);
            tv_group_name = (TextView) popView.findViewById(R.id.tv_group_name);
            tv_create_date = (TextView) popView.findViewById(R.id.tv_create_date);
        }
        popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        measuredHeight = popView.getMeasuredHeight();
        if (groupPop == null) {
            groupPop = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            groupPop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000"))); //设置背景
            groupPop.setFocusable(true); //设置获取焦点
            groupPop.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            groupPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            groupPop.setAnimationStyle(R.style.GroupPopupWindowAnimation);
            groupPop.setOutsideTouchable(true);
            groupPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
        }
        if (groupPop.isShowing()) {
            groupPop.dismiss();
        }
        groupPop.setContentView(popView);
    }

    public void getGroupState() {
        showDialog(getString(R.string.info_check));
        GroupStateRequest groupStateRequest = new GroupStateRequest(userId + "");
        sendRequest(groupStateRequest, GroupStateResponse.class);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getActivity().getWindow().setAttributes(lp);
    }

    private void toResult(String type) {
        Intent intent = new Intent(mContext, OperateResultActivity.class);
        intent.putExtra(Constants.TYPE, type);
        startActivityForResult(intent, RESULT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        registBroadcastReceiver(receiver);
//        registerNetBroadrecevicer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
            receiver = null;
        }
//        if (netReceiver != null) {
//            mContext.unregisterReceiver(netReceiver);
//            netReceiver = null;
//        }
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_home, null);
        return mView;
    }

    @Override
    public <Res extends BaseResponse> void onSuccessResult(BaseRequest request, Res response) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
            isConnect = true;
        }
        if (request instanceof HomeDataRequest) {
            HomeDataResponse homeDataResponse = (HomeDataResponse) response;
            if (homeDataResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(homeDataResponse.getErrorMsg())) {
                    HomeDataResponse.DataEntity dataEntity = homeDataResponse.getData();
                    tv_today_in_count.setText(dataEntity.getDayIn() + "");
                    tv_in_count.setText(dataEntity.getIn() + "");
                    tv_out_count.setText(dataEntity.getOut() + "");
                    tv_check_count.setText(dataEntity.getChk() + "");
                }
            }
        } else if (request instanceof GroupStateRequest) {
            GroupStateResponse groupStateResponse = (GroupStateResponse) response;
            if (groupStateResponse.getResponseCode() != null) {
                if (groupStateResponse.getResponseCode().getCode() == 200) {
                    if (groupStateResponse.getData() != null) {
                        GroupStateResponse.DataEntity data = groupStateResponse.getData();
                        groupId = data.getId();
                        SPUtils.put(mContext, SPConstants.GROUP_ID, groupId);
                        inGroup = true;
                        if (forInware) {
                            forInware = false;
                            Intent intent = new Intent(mContext, QrScanActivity.class);
                            intent.putExtra(Constants.TYPE, Constants.IN_WARE);
                            startActivityForResult(intent, IN_SCAN);
                            return;
                        }
                        initGroupPopupwindow();
                        tv_group_name.setText(data.getGroupName());
                        tv_create_date.setText(data.getCreateDate());
                        String groupUsers = data.getGroupUsers();
                        String[] users = groupUsers.split(",");
                        String groupUserIds = data.getGroupUserIds();
                        String[] ids = groupUserIds.split(",");
                        memberList.clear();
                        for (int i = 0; i < users.length; i++) {
                            if (!TextUtils.isEmpty(users[i])) {
                                memberList.add(new UserInfo(Integer.parseInt(ids[i].trim()), users[i], false));
                            }
                        }
                        memberAdapter.notifyDataSetChanged();
                    } else {
                        inGroup = false;
                        SPUtils.put(mContext, SPConstants.GROUP_ID, 0);
                        if (forInware) {
                            forInware = false;
                            CommonUtils.showToast(mContext, "当前不在任何群组中，请先加入一个群组或创建一个群组");
                        }
                        initGroupPopupwindow();
                    }
                    showGroup();
                }
            }
        } else if (request instanceof CreateGroupRequest) {
            CreateGroupResponse createGroupResponse = (CreateGroupResponse) response;
            if (createGroupResponse.getResponseCode() != null) {
                if (createGroupResponse.getResponseCode().getCode() == 200) {
                    CommonUtils.showToast(mContext, "创建成功");
                    getGroupState();
                }
            }
        } else if (request instanceof DeletMemberRequest) {
            DeletMemberResponse deletMemberResponse = (DeletMemberResponse) response;
            if (deletMemberResponse.getResponseCode() != null) {
                if (deletMemberResponse.getResponseCode().getCode() == 200) {
                    CommonUtils.showToast(mContext, "移除成功");
                    getGroupState();
                }
            }
        } else if (request instanceof GroupDeleteRequest) {
            GroupDeleteResponse groupDeleteResponse = (GroupDeleteResponse) response;
            if (groupDeleteResponse.getResponseCode() != null) {
                if (groupDeleteResponse.getResponseCode().getCode() == 200) {
                    CommonUtils.showToast(mContext, "解散成功");
                    getGroupState();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == IN_SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString(Constants.RESULT);
                String codeType = bundle.getString(Constants.CODE_TYPE);
                String scanState = bundle.getString(Constants.SCAN_STATE);
                Intent intent = new Intent(mContext, ScanResultActivity.class);
                intent.putExtra(Constants.SCAN_RESULT, result);
                intent.putExtra(Constants.TYPE, Constants.IN_WARE);
                intent.putExtra(Constants.CODE_TYPE, codeType);
                intent.putExtra(Constants.SCAN_STATE, scanState);
                startActivity(intent);
            }
        } else if (resultCode == getActivity().RESULT_OK && requestCode == CHANGE_SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString(Constants.RESULT);
                String scanState = bundle.getString(Constants.SCAN_STATE);
                Intent intent = new Intent(mContext, ScanResultActivity.class);
                String codeType = bundle.getString(Constants.CODE_TYPE);
                intent.putExtra(Constants.CODE_TYPE, codeType);
                intent.putExtra(Constants.SCAN_RESULT, result);
                intent.putExtra(Constants.SCAN_STATE, scanState);
                intent.putExtra(Constants.TYPE, Constants.CHANGE_WARE);
                startActivity(intent);
            }
        } else if (resultCode == Constants.RESULT_OK && requestCode == RESULT) {
            String type = data.getStringExtra(Constants.TYPE);
            Intent intent = new Intent(mContext, QrScanActivity.class);
            intent.putExtra(Constants.TYPE, type);
            startActivityForResult(intent, type.equals(Constants.IN_WARE) ? IN_SCAN : CHANGE_SCAN);
        } else if (resultCode == Constants.RESULT_OK && requestCode == ADD_MEMBER) {
//            List<UserInfo> addList = (List<UserInfo>) data.getSerializableExtra(Constants.DATA);
//            for (int i = 0; i < addList.size(); i++) {
//                UserInfo userInfo = addList.get(i);
//                userInfo.setChecked(false);
//                memberList.add(userInfo);
//            }
//            memberAdapter.notifyDataSetChanged();
            getGroupState();
        } else if (resultCode == Constants.RESULT_OK && requestCode == HEAD_MAN) {
            List<UserInfo> addList = (List<UserInfo>) data.getSerializableExtra(Constants.DATA);
            UserInfo userInfo = addList.get(0);
            String realName = userInfo.getRealName();
            tv_name.setText(realName);
        }
        refreshData();
    }

    private void refreshData() {
        HomeDataRequest homeDataRequest = new HomeDataRequest(userName);
        sendRequest(homeDataRequest, HomeDataResponse.class);
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
            case R.id.tv_data_syn:
                go2Syn();
                break;
            case R.id.fab_group:
                if (Constants.DEBUG) {
                    initGroupPopupwindow();
                    showGroup();
                } else {
                    getGroupState();
                }
                break;
            case R.id.tv_add_member:
                addMember();
                break;
            case R.id.tv_name:
//                setHeadman();
                break;
            case R.id.btn_commit:
                creatGroup();
                break;
            case R.id.tv_delete_group:
                deleteGroup();
                break;
        }
    }

    private void deleteGroup() {
        if (!post.contains("组长")) {
            CommonUtils.showToast(mContext, "班组只能由组长解散");
            return;
        }
        CommonDialog commonDialog = new CommonDialog(mContext, "提示", "确认解散班组?", "确认", "取消");
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.setCancelable(true);
        commonDialog.setClicklistener(new CommonDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                if (Constants.DEBUG) {
                    showDialog(getString(R.string.commit_data));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            customProgressDialog.dismiss();
                            isConnect = true;
                            CommonUtils.showToast(mContext, "解散成功");
                            inGroup = false;
                            groupPop.dismiss();
                            initGroupPopupwindow();
                            showGroup();
                        }
                    }, 1000);
                } else {
                    GroupDeleteRequest groupDeleteRequest = new GroupDeleteRequest(groupId + "");
                    sendRequest(groupDeleteRequest, GroupDeleteResponse.class);
                }
            }

            @Override
            public void doCancel() {

            }
        });
        commonDialog.show();
    }

    private void setHeadman() {
        Intent intent = new Intent(mContext, AddMemberActivity.class);
        intent.putExtra(Constants.ADD_TYPE, Constants.HEADER);
        intent.putExtra(Constants.HAS_ADD, ((Serializable) memberList));
        startActivityForResult(intent, HEAD_MAN);
    }

    private boolean isConnect;

    private void creatGroup() {
        String groupName = et_group_name.getText().toString().trim();
        String createName = tv_name.getText().toString().trim();
        if (!post.contains("组长")) {
            CommonUtils.showToast(mContext, "班组只能由组长创建");
            return;
        }
        if (TextUtils.isEmpty(groupName)) {
            CommonUtils.showToast(mContext, "未填写班组名称");
            return;
        }
        showDialog(getString(R.string.commit_data));
        if (Constants.DEBUG) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    customProgressDialog.dismiss();
                    isConnect = true;
                    memberList.clear();
                    CommonUtils.showToast(mContext, "创建成功");
                    inGroup = true;
                    memberList.add(new UserInfo(0, "test0", false));
                    groupPop.dismiss();
                    initGroupPopupwindow();
                    showGroup();
                }
            }, 1000);
        } else {
            CreateGroupRequest createGroupRequest = new CreateGroupRequest(userId + "", createName, groupName);
            sendRequest(createGroupRequest, CreateGroupResponse.class);
        }
    }

    private void addMember() {
        if (!post.contains("组长")) {
            CommonUtils.showToast(mContext, "人员只能由组长添加");
            return;
        }
        Intent intent = new Intent(mContext, AddMemberActivity.class);
        intent.putExtra(Constants.ADD_TYPE, Constants.MEMBER);
        intent.putExtra(Constants.HAS_ADD, ((Serializable) memberList));
        intent.putExtra(Constants.GROUP_ID, groupId);
        startActivityForResult(intent, ADD_MEMBER);
    }

    private void showGroup() {
        if (groupPop.isShowing()) {
            groupPop.dismiss();
        }
        backgroundAlpha(0.8f);
        groupPop.showAsDropDown(fab_group, 0, -(fab_group.getHeight() + measuredHeight));
    }

    private void go2Syn() {
        Intent intent = new Intent(mContext, DataStatisticsActivity.class);
        startActivity(intent);
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
        startActivityForResult(intent, IN_SCAN);
    }

    private void go2ChangeWare() {
//        Intent intent = new Intent(mContext, ChangeWareActivity.class);
//        startActivity(intent);
        Intent intent = new Intent(mContext, QrScanActivity.class);
        intent.putExtra(Constants.TYPE, Constants.CHANGE_WARE);
        startActivityForResult(intent, CHANGE_SCAN);
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
//        Intent intent = new Intent(mContext, InWareChooseActivity.class);
//        startActivity(intent);
        forInware = true;
        getGroupState();
//        if (!inGroup) {
//            CommonUtils.showToast(mContext, "当前不在任何群组中，请先加入一个群组或创建一个群组");
//            showGroup();
//            return;
//        }
//        Intent intent = new Intent(mContext, QrScanActivity.class);
//        intent.putExtra(Constants.TYPE, Constants.IN_WARE);
//        startActivityForResult(intent, IN_SCAN);
    }

    private void showDialog(String content) {
        customProgressDialog = new CustomProgressDialog(mContext, content);
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
                        CommonUtils.showToast(mContext, getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }
}
