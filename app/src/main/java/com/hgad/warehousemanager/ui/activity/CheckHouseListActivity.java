package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baozi.treerecyclerview.adpater.TreeRecyclerAdapter;
import com.baozi.treerecyclerview.factory.ItemHelperFactory;
import com.baozi.treerecyclerview.item.TreeItem;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.CheckHouseBean.ColEntry;
import com.hgad.warehousemanager.bean.CheckHouseBean.RowEntry;
import com.hgad.warehousemanager.bean.CheckHouseBean.RowItemParent;
import com.hgad.warehousemanager.bean.CheckTaskInfo;
import com.hgad.warehousemanager.bean.request.WareHouseRequest;
import com.hgad.warehousemanager.bean.response.WareHouseResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017/11/8.
 */
public class CheckHouseListActivity extends BaseActivity {
    private static final int NOTIFY = 100;
    private RecyclerView rl;
    private List<RowEntry> rowData = new ArrayList<>();
    private TreeRecyclerAdapter checkListAdapter;
    private String userName;
    private String ware;
    private String state;
    private int taskId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOTIFY:
                    List<TreeItem> treeItemList = ItemHelperFactory.createTreeItemList(rowData, RowItemParent.class, null);
                    checkListAdapter.setDatas(treeItemList);
                    checkListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_check_house_detail);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        CheckTaskInfo checkTaskInfo = (CheckTaskInfo) intent.getSerializableExtra(Constants.CHECK_INFO);
        String taskName = checkTaskInfo.getName();
        String wareNum = checkTaskInfo.getWareNo();
        state = checkTaskInfo.getState();
        taskId = checkTaskInfo.getTaskId();
        initHeader(taskName);
        ware = wareNum;
        userName = SPUtils.getString(this, SPConstants.USER_NAME);
//        showDialog("数据同步中");
        if (Constants.DEBUG) {
            List<String> positionList = new ArrayList<>();
            for (int i = 1; i < 10; i++) {
                positionList.add("01010" + i);
            }
            Map<String, List<ColEntry>> map = new TreeMap<>();
            String rowName;
            for (String positionCode : positionList) {
                rowName = positionCode.substring(2, 4);
                List<ColEntry> curList = map.get(rowName);
                if (curList == null) {
                    curList = new ArrayList<>();
                }
                curList.add(new ColEntry(positionCode.substring(4, 6), positionCode, taskId));
                Collections.sort(curList, new Comparator<ColEntry>() {
                    @Override
                    public int compare(ColEntry lhs, ColEntry rhs) {
                        return lhs.getColName().compareTo(rhs.getColName());
                    }
                });
                map.put(rowName, curList);
            }
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                List<ColEntry> li = (List<ColEntry>) entry.getValue();
                rowData.add(new RowEntry(key, li));
            }
            handler.sendEmptyMessage(NOTIFY);
        } else {
            WareHouseRequest wareHouseRequest = new WareHouseRequest(wareNum);
            sendRequest(wareHouseRequest, WareHouseResponse.class);
        }
    }

    @Override
    protected void initView() {
        rl = (RecyclerView) findViewById(R.id.rl_check);
        rl.setLayoutManager(new GridLayoutManager(this, 6));
        rl.setItemAnimator(new DefaultItemAnimator());
        checkListAdapter = new TreeRecyclerAdapter(this);
        checkListAdapter.setOnExpandListener(new TreeRecyclerAdapter.OnExpandListener() {

            @Override
            public void onExpand(int position) {
                rl.scrollToPosition(position);
            }
        });
        rl.setAdapter(checkListAdapter);
        rl.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (view.getLayoutParams() instanceof GridLayoutManager.LayoutParams) {
                    GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                    int spanIndex = layoutParams.getSpanIndex();//在一行中所在的角标，第几列
                    int spanSize = layoutParams.getSpanSize();
                    if (spanSize == 2) {
                        outRect.bottom = 10;
                    }
                    if (spanIndex == 0 && spanSize == 2) {
                        outRect.left = 30;
                        outRect.right = 10;
                    } else if (spanIndex == 4) {
                        outRect.left = 0;
                        outRect.right = 30;
                    } else if (spanSize == 6) {
                        outRect.left = 30;
                        outRect.right = 30;
                    } else {
                        outRect.left = 0;
                        outRect.right = 10;
                    }
                }
            }
        });

    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
//        isConnect = true;
        if (request instanceof WareHouseRequest) {
            WareHouseResponse wareHouseResponse = (WareHouseResponse) response;
            if (wareHouseResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(wareHouseResponse.getErrorMsg())) {
                    WareHouseResponse.DataEntity dataEntity = wareHouseResponse.getData().get(0);
                    List<WareHouseResponse.DataEntity.PositionListEntity> positionList = dataEntity.getPositionList();
                    Map<String, List<ColEntry>> map = new TreeMap<>();
                    String rowName;
                    for (WareHouseResponse.DataEntity.PositionListEntity positionListEntity : positionList) {
                        String positionCode = positionListEntity.getPositionCode();
                        rowName = positionCode.substring(2, 4);
                        List<ColEntry> curList = map.get(rowName);
                        if (curList == null) {
                            curList = new ArrayList<>();
                        }
                        curList.add(new ColEntry(positionCode.substring(4, 6), positionCode, taskId));
                        Collections.sort(curList, new Comparator<ColEntry>() {
                            @Override
                            public int compare(ColEntry lhs, ColEntry rhs) {
                                return lhs.getColName().compareTo(rhs.getColName());
                            }
                        });
                        map.put(rowName, curList);
                    }
                    Iterator iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        String key = (String) entry.getKey();
                        List<ColEntry> li = (List<ColEntry>) entry.getValue();
                        rowData.add(new RowEntry(key, li));
                    }
                    handler.sendEmptyMessage(NOTIFY);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }


}
