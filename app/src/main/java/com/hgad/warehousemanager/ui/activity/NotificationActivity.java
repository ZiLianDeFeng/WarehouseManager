package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.MessageInfo;
import com.hgad.warehousemanager.bean.request.TaskRequest;
import com.hgad.warehousemanager.bean.response.TaskResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.MessageAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/7/19.
 */
public class NotificationActivity extends BaseActivity {

    private XListView lv;
    private List<MessageInfo> data = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private RelativeLayout rl_info;
    private Animation operatingAnim;
    private ImageView infoOperating;
    private int currentPage = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.NOTIFY:
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
                    messageAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private String type;
    private int userId;
    private boolean isRefresh;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_notification);
    }

    @Override
    protected void initData() {
        initHeader("任务通知");
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        if (operatingAnim != null) {
            rl_info.setVisibility(View.VISIBLE);
            infoOperating.startAnimation(operatingAnim);
            TaskRequest taskRequest = new TaskRequest(type, currentPage, userId);
            sendRequest(taskRequest, TaskResponse.class);
            currentPage--;
        }
    }

    @Override
    protected void initView() {
        lv = (XListView) findViewById(R.id.lv_message);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        lv.setEmptyView(tv_empty);
        messageAdapter = new MessageAdapter(data, this);
        messageAdapter.setCallFreshListener(callRefreshListener);
        lv.setAdapter(messageAdapter);
        lv.setPullRefreshEnable(true);
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(listViewListener);
        lv.setOnItemClickListener(itemListener);
        initAnimation();
    }

    private void initAnimation() {
        rl_info = (RelativeLayout) findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MessageInfo messageInfo = data.get(position - 1);
            Intent intent = new Intent(NotificationActivity.this, ProductListActivity.class);
            intent.putExtra(Constants.TYPE, messageInfo.getType());
//            intent.putExtra(Constants.ORDER_NUMBER, messageInfo.getOrderNum());
            intent.putExtra(Constants.MESSAGE_INFO, messageInfo);
            startActivity(intent);
        }
    };

    private MessageAdapter.CallFreshListener callRefreshListener = new MessageAdapter.CallFreshListener() {
        @Override
        public void callFresh() {
            callRefresh();
        }
    };

    private XListView.IXListViewListener listViewListener = new XListView.IXListViewListener() {
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
        boolean isNetWork = CommonUtils.checkNetWork(this);
        if (isNetWork) {
            isRefresh = false;
            currentPage++;
            TaskRequest taskRequest = new TaskRequest(type, currentPage, userId);
            sendRequest(taskRequest, TaskResponse.class);
            currentPage--;
        } else {
            lv.stopLoadMore();
        }
    }

    private void callRefresh() {
        boolean isNetWork = CommonUtils.checkNetWork(this);
        if (isNetWork) {
            isRefresh = true;
            currentPage = 1;
            TaskRequest taskRequest = new TaskRequest(type, currentPage, userId);
            sendRequest(taskRequest, TaskResponse.class);
            currentPage--;
        } else {
            lv.stopRefresh();
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (request instanceof TaskRequest) {
            TaskResponse taskResponse = (TaskResponse) response;
            if (taskResponse.getData() != null) {
                currentPage++;
                if (currentPage == 1) {
                    if (data != null) {
                        data.clear();
                    }
                }
                if (currentPage == taskResponse.getData().getPage().getPage()) {
                    List<TaskResponse.DataEntity.ListEntity> list = taskResponse.getData().getList();
                    for (TaskResponse.DataEntity.ListEntity listEntity : list) {
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setData(listEntity);
                        data.add(messageInfo);
                    }
                }
                handler.sendEmptyMessageDelayed(Constants.NOTIFY, 200);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
