package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.view.BottonPopupWindowUtils;

/**
 * Created by Administrator on 2017/6/29.
 */
public class InWareByHandActivity extends BaseActivity {

    private EditText et_markNum;
    private BottonPopupWindowUtils bottonPopupWindowUtils;
    private PopupWindow bottonPopupWindow;
    private String row = "1";
    private String column = "1";
    private String floor = "1";
    private String ware = "1";
    private Button btn_commit;
    private String address;
    private TextView tv_addressWare;
    private String type;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_inware_byhand);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra(Constants.TYPE);
        if (Constants.CHANGE_WARE.equals(type)) {
            initHeader("移位-手动移位");
        } else if (Constants.IN_WARE.equals(type)) {
            initHeader("入库-手动添加");
        } else if (Constants.CHECK.equals(type)) {
            initHeader("盘点-手动盘点");
        }
    }

    @Override
    protected void initView() {
        et_markNum = (EditText) findViewById(R.id.et_markNum);
        tv_addressWare = (TextView) findViewById(R.id.tv_addressWare);
        tv_addressWare.setOnClickListener(this);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        initBottonPopupWindow();
    }

    String[] wareNums = {"1", "2", "3", "4", "5", "6"};
    String[] rows = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] columns = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] floors = {"1", "2", "3"};

    private void initBottonPopupWindow() {
        bottonPopupWindowUtils = new BottonPopupWindowUtils(ware, row, column, floor, getResources().getString(R.string.choose_address));
        bottonPopupWindow = bottonPopupWindowUtils.creat(this, wareNums, rows, columns, floors, this);
        bottonPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.backgroundAlpha(1f, InWareByHandActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backWarm();
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_addressWare:
                chooseAddress();
                break;
            case R.id.btn_commit:
                commit();
                break;
            case R.id.pop_confirm:
                confirmAddress();
                break;
            case R.id.pop_cancle:
                bottonPopupWindow.dismiss();
                break;
        }
    }

    private void commit() {
        if (TextUtils.isEmpty(et_markNum.getText().toString().trim())) {
            CommonUtils.showToast(this, "未输入标签号");
            return;
        }
        if (TextUtils.isEmpty(tv_addressWare.getText().toString().trim())) {
            CommonUtils.showToast(this, "未选择仓库位置");
            return;
        }
        if (Constants.CHANGE_WARE.equals(type)) {
            changeCommit();
        } else if (Constants.IN_WARE.equals(type)) {
            inCommit();
        } else if (Constants.CHECK.equals(type)) {
            checkCommit();
        }
    }

    private void checkCommit() {
        CommonUtils.showToast(this, "信息正确");
        finish();
    }

    private void inCommit() {
        CommonUtils.showToast(this, "入库成功");
        finish();
    }

    private void changeCommit() {
        CommonUtils.showToast(this, "移位成功");
        finish();
    }

    private void confirmAddress() {
        ware = bottonPopupWindowUtils.getWare();
        row = bottonPopupWindowUtils.getRow();
        column = bottonPopupWindowUtils.getColumn();
        floor = bottonPopupWindowUtils.getFloor();
        address = ware + "仓 " + row + "排 " + column + "垛 " + floor + "层 ";
        CommonUtils.stringInterceptionChangeLarge(tv_addressWare, address, new String[]{ware, row, column, floor}, "仓", "排", "垛", "层");
        bottonPopupWindow.dismiss();
    }

    private void chooseAddress() {
        bottonPopupWindow.showAtLocation(btn_commit, Gravity.BOTTOM, 0, 0);
        CommonUtils.backgroundAlpha(0.8f, this);
    }
}
