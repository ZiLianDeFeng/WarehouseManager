package com.hgad.warehousemanager.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
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

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_inware_byhand);
    }

    @Override
    protected void initData() {
        initHeader("入库-手动添加");
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
        bottonPopupWindowUtils = new BottonPopupWindowUtils(ware, row, column, floor);
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

                break;
            case R.id.pop_confirm:
                confirmAddress();
                break;
        }
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
