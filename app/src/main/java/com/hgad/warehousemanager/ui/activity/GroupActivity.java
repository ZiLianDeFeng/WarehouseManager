package com.hgad.warehousemanager.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.GroupInfo;
import com.hgad.warehousemanager.bean.request.GroupListRequest;
import com.hgad.warehousemanager.bean.response.GroupListResponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */
public class GroupActivity extends BaseActivity {

    private List<GroupInfo> data = new ArrayList<>();
    private GroupAdapter groupAdapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_group);
    }

    @Override
    protected void initData() {
        initHeader(getString(R.string.drawer_menu_group));
        getGroupList();
//        for (int i = 0; i < 10; i++) {
//            data.add(new GroupInfo(i, "班组" + i, i == 0 ? "张三，李四，王五，赵六，小明，小红" : "张三，李四，王五，赵六", "50", i == 0 ? "0" : "1"));
//        }
//        groupAdapter.notifyDataSetChanged();
    }

    private void getGroupList() {
        GroupListRequest groupListRequest = new GroupListRequest();
        sendRequest(groupListRequest, GroupListResponse.class);
    }

    @Override
    protected void initView() {
        ListView lv_group = (ListView) findViewById(R.id.lv_group);
        groupAdapter = new GroupAdapter(data, this);
        lv_group.setAdapter(groupAdapter);
        TextView emptyView = new TextView(this);
        emptyView.setText("暂无班组");
        emptyView.setTextSize(20f);
        emptyView.setGravity(Gravity.CENTER);
        ((ViewGroup) lv_group.getParent()).addView(emptyView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        lv_group.setEmptyView(emptyView);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (request instanceof GroupListRequest) {
            GroupListResponse groupListResponse = (GroupListResponse) response;
            if (groupListResponse.getResponseCode() != null) {
                if (groupListResponse.getResponseCode().getCode() == 200) {
                    List<GroupListResponse.DataEntity.ListEntity> list = groupListResponse.getData().getList();
                    for (GroupListResponse.DataEntity.ListEntity listEntity : list) {
                        GroupInfo groupInfo = new GroupInfo();
                        groupInfo.setData(listEntity);
                        data.add(groupInfo);
                    }
                    groupAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
