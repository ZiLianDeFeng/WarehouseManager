package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.CheckResultRequest;
import com.hgad.warehousemanager.bean.request.WareHouseRequest;
import com.hgad.warehousemanager.bean.response.CheckResultResponse;
import com.hgad.warehousemanager.bean.response.WareHouseResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;
import com.hgad.warehousemanager.view.SeatTable;

import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class WareHouseActivity extends BaseActivity {

    private SeatTable seatView;
    private WareInfo wareInfo;
    //    private TextView tv_confirm;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler();
    private String type;
    private String address;


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_ware_house);
    }

    @Override
    protected void initData() {
//        seatView.setData(15, 15);
        Intent intent = getIntent();
        type = intent.getStringExtra(Constants.TYPE);
        address = intent.getStringExtra(Constants.ADDRESS);
        if (address != null) {
            String ware = address.substring(0, 2);
            WareHouseRequest wareHouseRequest = new WareHouseRequest(ware);
            sendRequest(wareHouseRequest, WareHouseResponse.class);
        }
//        if (Constants.CHECK.equals(type) || Constants.OUT_WARE.equals(type)) {
//            initHeader(address);
//            tv_confirm.setText(Constants.CHECK.equals(type) ? "盘点" : Constants.OUT_WARE.equals(type) ? "出库" : null);
//            int rWare = Integer.parseInt(address.substring(0, address.indexOf("仓")).trim());
//            final int rRow = Integer.parseInt(address.substring(address.indexOf("仓") + 1, address.indexOf("排")).trim());
//            final int rColumn = Integer.parseInt(address.substring(address.indexOf("排") + 1, address.indexOf("垛")).trim());
//            final int rFloor = Integer.parseInt(address.substring(address.indexOf("垛") + 1, address.indexOf("号")).trim());
//            seatView.setScreenName(rWare + "号仓库");
//            seatView.setMaxSelected(0);
//            seatView.setType(Constants.CURRENT);
//            seatView.setSeatChecker(new SeatTable.SeatChecker() {
//                @Override
//                public boolean isValidSeat(int row, int column) {
//                    return true;
//                }
//
//                @Override
//                public boolean isSold(int row, int column) {
//
//                    return false;
//                }
//
//                @Override
//                public void checked(int row, int column) {
//
//                }
//
//                @Override
//                public void unCheck(int row, int column) {
//
//                }
//
//                @Override
//                public boolean isUnFull(int row, int column) {
//                    return false;
//                }
//
//                @Override
//                public boolean isCurrentSeat(int row, int column) {
//                    if (row == (rRow - 1) && column == (rColumn - 1))
//                        return true;
//                    return false;
//                }
//
//                @Override
//                public boolean isCheckSeat(int row, int column) {
//                    return false;
//                }
//
//                @Override
//                public String[] checkedSeatTxt(int row, int column) {
//                    return null;
//                }
//            });
//        } else
        if (Constants.CHECK_RECORD.equals(type)) {
            final int[] checkRows = new int[]{1, 1, 2, 2, 3, 3};
            final int[] checkColumns = new int[]{1, 2, 1, 2, 1, 2};
            initHeader("盘点统计");
//            tv_confirm.setVisibility(View.INVISIBLE);
            seatView.setData(15, 15);
            seatView.setScreenName("");
            seatView.setMaxSelected(0);
            seatView.setType(Constants.CHECK_RECORD);
            seatView.setSeatChecker(new SeatTable.SeatChecker() {
                @Override
                public boolean isValidSeat(int row, int column) {
                    return true;
                }

                @Override
                public boolean isSold(int row, int column) {
                    return false;
                }

                @Override
                public void checked(int row, int column) {

                }

                @Override
                public void unCheck(int row, int column) {

                }

                @Override
                public boolean isUnFull(int row, int column) {
                    return false;
                }

                @Override
                public boolean isCurrentSeat(int row, int column) {

                    return false;
                }

                @Override
                public boolean isCheckSeat(int row, int column) {
                    for (int i = 0; i < checkRows.length; i++) {
                        if (row == (checkRows[i] - 1) && column == (checkColumns[i] - 1)) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public String[] checkedSeatTxt(int row, int column) {
                    return new String[0];
                }
            });
        }
    }

    @Override
    protected void initView() {
        seatView = (SeatTable) findViewById(R.id.seatView);
//        tv_confirm = (TextView) findViewById(R.id.btn_confirm);
//        tv_confirm.setVisibility(View.VISIBLE);
//        tv_confirm.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (request instanceof WareHouseRequest) {
            WareHouseResponse wareHouseResponse = (WareHouseResponse) response;
            if (wareHouseResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(wareHouseResponse.getErrorMsg())) {
                    WareHouseResponse.DataEntity dataEntity = wareHouseResponse.getData().get(0);
                    int rows = dataEntity.getRows();
                    int cols = dataEntity.getCols();
                    String name = dataEntity.getName();
                    List<WareHouseResponse.DataEntity.PositionListEntity> positionList = dataEntity.getPositionList();
                    for (WareHouseResponse.DataEntity.PositionListEntity positionListEntity : positionList) {
                        String raw = positionListEntity.getPositionCode().substring(2, 4);
                        String column = positionListEntity.getPositionCode().substring(4, 6);
                    }
                    initSeatTable(rows, cols, name);
                }
            }
        }
    }

    private void initSeatTable(int rows, int cols, String name) {
        seatView.setData(rows, cols);
        seatView.setScreenName(name);
        if (Constants.CHECK.equals(type) || Constants.OUT_WARE.equals(type)) {
            initHeader(address);
//            tv_confirm.setText(Constants.CHECK.equals(type) ? "盘点" : Constants.OUT_WARE.equals(type) ? "出库" : null);
            int rWare = Integer.parseInt(address.substring(0, address.indexOf("仓")).trim());
            final int rRow = Integer.parseInt(address.substring(address.indexOf("仓") + 1, address.indexOf("排")).trim());
            final int rColumn = Integer.parseInt(address.substring(address.indexOf("排") + 1, address.indexOf("垛")).trim());
            final int rFloor = Integer.parseInt(address.substring(address.indexOf("垛") + 1, address.indexOf("号")).trim());
            seatView.setScreenName(rWare + "号仓库");
            seatView.setMaxSelected(0);
            seatView.setType(Constants.CURRENT);
            seatView.setSeatChecker(new SeatTable.SeatChecker() {
                @Override
                public boolean isValidSeat(int row, int column) {
                    return true;
                }

                @Override
                public boolean isSold(int row, int column) {
                    return false;
                }

                @Override
                public void checked(int row, int column) {

                }

                @Override
                public void unCheck(int row, int column) {

                }

                @Override
                public boolean isUnFull(int row, int column) {
                    return false;
                }

                @Override
                public boolean isCurrentSeat(int row, int column) {
                    if (row == (rRow - 1) && column == (rColumn - 1))
                        return true;
                    return false;
                }

                @Override
                public boolean isCheckSeat(int row, int column) {
                    return false;
                }

                @Override
                public String[] checkedSeatTxt(int row, int column) {
                    return null;
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                showConfirmDialog();
                break;
        }
    }

    private void showConfirmDialog() {
//        if ("出库".equals(tv_confirm.getText())) {
////            DialogUtils.showAlert(this, "提示", "确定出库或去移位？", "确定", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
//                    outWare();
////                }
////            }, "去移位", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    changeWare();
////                }
////            }, AlertDialog.THEME_HOLO_LIGHT);
//        } else if ("盘点".equals(tv_confirm.getText())) {
//            DialogUtils.showAlert(this, "提示", "信息正确并完成或信息错误去移位？", "正确", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finishCheck();
//                }
//            }, "去移位", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    changeWare();
//                }
//            }, AlertDialog.THEME_HOLO_LIGHT);
//        } else if ("入库".equals(tv_confirm.getText())) {
//            DialogUtils.showAlert(this, "提示", "确定选择当前位置的最上层或者需要去移位？", "确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    confirm();
//                }
//            }, "去移位", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    changeWare();
//                }
//            }, AlertDialog.THEME_HOLO_LIGHT);
//        }
    }

    private void finishCheck() {
        CheckResultRequest checkResultRequest = new CheckResultRequest();
        sendRequest(checkResultRequest, CheckResultResponse.class);
    }

    private void outWare() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                }
                CommonUtils.showToast(WareHouseActivity.this, "出库成功");
                finish();
            }
        }, 2000);
        customProgressDialog = new CustomProgressDialog(this, "数据提交中");
//        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
    }

    private void changeWare() {
//        if ("出库".equals(tv_confirm.getText())) {
//            Intent intent = new Intent(this, ChangeWareActivity.class);
//            startActivity(intent);
//        } else if ("盘点".equals(tv_confirm.getText())) {
//            Intent intent = new Intent(this, ScanResultActivity.class);
//            intent.putExtra(Constants.TYPE, Constants.CHANGE_WARE);
//            startActivity(intent);
//        }
    }

    private void confirm() {

    }

}
