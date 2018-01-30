package com.hgad.warehousemanager.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseFragment;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.bean.UserInfo;
import com.hgad.warehousemanager.bean.request.CreateGroupRequest;
import com.hgad.warehousemanager.bean.request.DeletMemberRequest;
import com.hgad.warehousemanager.bean.request.GroupDeleteRequest;
import com.hgad.warehousemanager.bean.request.GroupStateRequest;
import com.hgad.warehousemanager.bean.request.OutOrderListRequest;
import com.hgad.warehousemanager.bean.response.CreateGroupResponse;
import com.hgad.warehousemanager.bean.response.DeletMemberResponse;
import com.hgad.warehousemanager.bean.response.GroupDeleteResponse;
import com.hgad.warehousemanager.bean.response.GroupStateResponse;
import com.hgad.warehousemanager.bean.response.OutOrderListResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.activity.AddMemberActivity;
import com.hgad.warehousemanager.ui.activity.ProductListActivity;
import com.hgad.warehousemanager.ui.adapter.MemberAdapter;
import com.hgad.warehousemanager.ui.adapter.NewOrderAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.CommonDialog;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/8/10.
 */
public class OutWareFragment extends BaseFragment {

    private static final int OUT_WARE = 420;
    private static final int HEAD_MAN = 330;
    private static final int ADD_MEMBER = 340;
    private View mView;
    private XListView lv;
    private List<OrderInfo> data = new ArrayList<>();
    private NewOrderAdapter orderAdapter;
    private SwipeRefreshLayout swipeRefreshView;
    private Animation operatingAnim;
    private RelativeLayout rl_info;
    private ImageView infoOperating;
    private boolean isConnect;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.NOTIFY:
                    isConnect = true;
                    if (isRefresh) {
                        lv.stopRefresh();
                        lv.setRefreshTime(CommonUtils.getCurrentTime());
                    } else {
                        lv.stopLoadMore();
                    }
                    rl_info.setVisibility(View.INVISIBLE);
                    infoOperating.clearAnimation();
                    if (data.size() < 10) {
                        lv.setPullLoadEnable(false);
                    } else {
                        lv.setPullLoadEnable(true);
                    }
                    orderAdapter.notifyDataSetChanged();
                    if (data.size() != 0 && isRefresh) {
                        lv.setSelection(0);
                    }
                    break;
            }
        }
    };
    private String type = Constants.OUT_TYPE;
    private int currentPage = 1;
    private int userId;
    private String state = "'1','2'";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.ORDER_REFRESH)) {
                callRefresh();
            }
        }
    };
    private String curOrderNum;
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
    private TextView tv_group_name;
    private TextView tv_create_date;
    private int groupId;
    private ListView lv_member;
    private boolean forOutware;
    private boolean forReviewWare;
    private OrderInfo curOrderInfo;
    private String post;

    public void setType(String type) {
        this.state = type;
        callRefresh();
    }

    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ORDER_REFRESH);
        mContext.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void initView() {
        lv = (XListView) mView.findViewById(R.id.lv_order);
        orderAdapter = new NewOrderAdapter(data, mContext);
        orderAdapter.setCallFreshListener(callRefreshListener);
        orderAdapter.setGroupStateListener(groupStateListener);
        orderAdapter.setOnActionListener(onActionListener);
        lv.setAdapter(orderAdapter);
//        lv.setOnItemClickListener(itemListener);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(xlistviewListener);
        TextView tv_empty = (TextView) mView.findViewById(R.id.tv_empty);
        lv.setEmptyView(tv_empty);
        initAnimation();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_MEMBER:
                if (resultCode == Constants.RESULT_OK) {
//                    List<UserInfo> addList = (List<UserInfo>) data.getSerializableExtra(Constants.DATA);
//                    for (int i = 0; i < addList.size(); i++) {
//                        UserInfo userInfo = addList.get(i);
//                        userInfo.setChecked(false);
//                        memberList.add(userInfo);
//                    }
//                    memberAdapter.notifyDataSetChanged();
                    getGroupState();
                }
                break;
            case HEAD_MAN:
                if (resultCode == Constants.RESULT_OK) {
                    List<UserInfo> addList = (List<UserInfo>) data.getSerializableExtra(Constants.DATA);
                    UserInfo userInfo = addList.get(0);
                    String realName = userInfo.getRealName();
                    tv_name.setText(realName);
                }
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registBroadcastReceiver(receiver);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void initData() {
        userId = SPUtils.getInt(mContext, SPConstants.USER_ID);
        post = SPUtils.getString(mContext, SPConstants.POST);
        boolean netWork = CommonUtils.checkNetWork(mContext);
        if (netWork) {
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
            }
            isConnect = false;
            if (Constants.DEBUG) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            data.add(new OrderInfo("F3Q4235" + i, 50, "1", i, "1", "zang", "test", "2017-8-15", "1000"));
                        }
                        orderAdapter.notifyDataSetChanged();
                        rl_info.setVisibility(View.INVISIBLE);
                        infoOperating.clearAnimation();
                    }
                }, 2000);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isConnect) {
                            rl_info.setVisibility(View.INVISIBLE);
                            infoOperating.clearAnimation();
                        }
                    }
                }, 5000);
                OutOrderListRequest outOrderListRequest = new OutOrderListRequest(currentPage, state, curOrderNum);
                sendRequest(outOrderListRequest, OutOrderListResponse.class);
                currentPage--;
            }
        } else {
            CommonUtils.showToast(mContext, getString(R.string.check_net));
        }
//        }
    }

    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            boolean isSwipe = orderAdapter.isSwipe();
//            if (isSwipe)
//                return;
            OrderInfo orderInfo = data.get(position - 1);
            Intent intent = new Intent(mContext, ProductListActivity.class);
//            if (orderInfo.getState().equals("1") || orderInfo.getState().equals("2")) {
//                intent.putExtra(Constants.TYPE, Constants.OUT_WARE);
//            } else if (orderInfo.getState().equals("3") || orderInfo.getState().equals("4")) {
//                intent.putExtra(Constants.TYPE, Constants.REVIEW_TYPE);
//            }
            intent.putExtra(Constants.ORDER_INFO, orderInfo);
            startActivity(intent);
        }
    };


    private NewOrderAdapter.OnActionListener onActionListener = new NewOrderAdapter.OnActionListener() {
        @Override
        public void onOutWare(OrderInfo orderInfo) {
            curOrderInfo = orderInfo;
            forOutware = true;
            getGroupState();
        }

        @Override
        public void onReviewWare(OrderInfo orderInfo) {
            curOrderInfo = orderInfo;
            forReviewWare = true;
            getGroupState();
        }
    };

    private void initAnimation() {
        rl_info = (RelativeLayout) mView.findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) mView.findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    private NewOrderAdapter.GroupStateListener groupStateListener = new NewOrderAdapter.GroupStateListener() {
        @Override
        public boolean inGroupOrNot() {
//            if (!inGroup) {
//                CommonUtils.showToast(mContext, "当前不在任何班组中，请先加入一个班组或创建一个班组");
//                showGroup();
//            }
            getGroupState();
            return inGroup;
        }
    };

    private NewOrderAdapter.CallFreshListener callRefreshListener = new NewOrderAdapter.CallFreshListener() {
        @Override
        public void callFresh() {
            callRefresh();
        }
    };

    private boolean isRefresh;
    private XListView.IXListViewListener xlistviewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            callRefresh();
        }

        @Override
        public void onLoadMore() {
            callLoadMore();
        }
    };

    private void callLoadMore() {
        boolean isNetWork = CommonUtils.checkNetWork(mContext);
        if (isNetWork) {
            isRefresh = false;
            isConnect = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isConnect) {
                        lv.stopLoadMore();
                    }
                }
            }, 5000);
            currentPage++;
            OutOrderListRequest outOrderListRequest = new OutOrderListRequest(currentPage, state, curOrderNum);
            sendRequest(outOrderListRequest, OutOrderListResponse.class);
            currentPage--;
        } else {
            lv.stopLoadMore();
            CommonUtils.showToast(mContext, getString(R.string.check_net));
        }
    }

    private void callRefresh() {
        boolean isNetWork = CommonUtils.checkNetWork(mContext);
        if (isNetWork) {
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
            }
            isConnect = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isConnect) {
                        lv.stopRefresh();
                    }
                }
            }, 5000);
            isRefresh = true;
            currentPage = 1;
            OutOrderListRequest outOrderListRequest = new OutOrderListRequest(currentPage, state, curOrderNum);
            sendRequest(outOrderListRequest, OutOrderListResponse.class);
            currentPage--;
        } else {
            lv.stopRefresh();
            CommonUtils.showToast(mContext, getString(R.string.check_net));
        }
    }

    public void getGroupState() {
        showDialog(getString(R.string.info_check));
        GroupStateRequest groupStateRequest = new GroupStateRequest(userId + "");
        sendRequest(groupStateRequest, GroupStateResponse.class);
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_out_ware, null);
        return mView;
    }

    @Override
    public <Res extends BaseResponse> void onSuccessResult(BaseRequest request, Res response) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
            isConnect = true;
        }
        if (request instanceof OutOrderListRequest) {
            OutOrderListResponse outOrderListResponse = (OutOrderListResponse) response;
            if (outOrderListResponse.getData() != null) {
                currentPage++;
                if (currentPage == 1) {
                    if (data != null) {
                        data.clear();
                    }
                }
                if (currentPage == outOrderListResponse.getData().getPage().getPage()) {
                    List<OutOrderListResponse.DataEntity.ListEntity> list = outOrderListResponse.getData().getList();
                    for (OutOrderListResponse.DataEntity.ListEntity listEntity : list) {
                        OrderInfo orderInfo = new OrderInfo();
                        orderInfo.setData(listEntity);
                        data.add(orderInfo);
                    }
                }
                handler.sendEmptyMessageDelayed(Constants.NOTIFY, 200);
            }
        } else if (request instanceof GroupStateRequest) {
            GroupStateResponse groupStateResponse = (GroupStateResponse) response;
            if (groupStateResponse.getResponseCode() != null) {
                if (groupStateResponse.getResponseCode().getCode() == 200) {
                    if (groupStateResponse.getData() != null) {
                        GroupStateResponse.DataEntity data = groupStateResponse.getData();
                        groupId = data.getId();
                        inGroup = true;
                        if (forOutware) {
                            forOutware = false;
                            if (curOrderInfo != null) {
                                Intent intent = new Intent(mContext, ProductListActivity.class);
                                intent.putExtra(Constants.TYPE, Constants.OUT_WARE);
                                intent.putExtra(Constants.ORDER_INFO, curOrderInfo);
                                intent.putExtra(Constants.PRO_TYPE, curOrderInfo.getProductType());
                                startActivity(intent);
                                return;
                            }
                        }
                        if (forReviewWare) {
                            forReviewWare = false;
                            if (curOrderInfo != null) {
                                Intent intent = new Intent(mContext, ProductListActivity.class);
                                intent.putExtra(Constants.TYPE, Constants.REVIEW_TYPE);
                                intent.putExtra(Constants.ORDER_INFO, curOrderInfo);
                                intent.putExtra(Constants.PRO_TYPE, curOrderInfo.getProductType());
                                startActivity(intent);
                                return;
                            }
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
                        if (forOutware || forReviewWare) {
                            forOutware = false;
                            forReviewWare = false;
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
    public void onClick(View v) {
        switch (v.getId()) {
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
                deletGroup();
                break;
        }
    }

    private void deletGroup() {
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
//        Intent intent = new Intent(this, GroupActivity.class);
//        startActivity(intent);
        if (groupPop.isShowing()) {
            groupPop.dismiss();
        }
        backgroundAlpha(0.8f);
        groupPop.showAsDropDown(fab_group, 0, -(fab_group.getHeight() + measuredHeight));
    }

    public void search(String orderNum) {
        curOrderNum = orderNum;
        callRefresh();
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
