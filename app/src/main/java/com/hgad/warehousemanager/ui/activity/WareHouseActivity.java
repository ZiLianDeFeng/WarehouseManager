package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.view.View;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.view.SeatTable;

/**
 * Created by Administrator on 2017/6/26.
 */
public class WareHouseActivity extends BaseActivity {

    private SeatTable seatView;
    private WareInfo wareInfo;

    public void gitlab(){
        Okgitlab();
    }

    public void Okgitlab(){
        gg();
    }

    private void gg() {
        g();
    }

    private void g() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_ware_house);
    }

    @Override
    protected void initData() {
        seatView.setData(15, 15);
        Intent intent = getIntent();
        String type = intent.getStringExtra(Constants.TYPE);
        String address = intent.getStringExtra(Constants.ADDRESS);
        if (!Constants.CHECK.equals(type)) {
            initHeader("仓库位置");
            int rWare = Integer.parseInt(address.substring(0, address.indexOf("仓")).trim());
            final int rRow = Integer.parseInt(address.substring(address.indexOf("仓") + 1, address.indexOf("排")).trim());
            final int rColumn = Integer.parseInt(address.substring(address.indexOf("排") + 1, address.indexOf("垛")).trim());
            final int rFloor = Integer.parseInt(address.substring(address.indexOf("垛") + 1, address.indexOf("层")).trim());
            seatView.setScreenName(rWare + "号仓库");
            seatView.setMaxSelected(1);
            seatView.setSeatChecker(new SeatTable.SeatChecker() {
                @Override
                public boolean isValidSeat(int row, int column) {
                    return true;
                }

                @Override
                public boolean isSold(int row, int column) {
                    if (row == (rRow - 1) && column == (rColumn - 1))
                        return true;
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
                public String[] checkedSeatTxt(int row, int column) {
                    return null;
                }
            });
        } else {
            initHeader("仓库选位");
            final int[] rows = new int[]{4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6};
            final int[] columns = new int[]{4, 5, 6, 7, 8, 9, 10, 4, 5, 6, 7, 8, 9, 10, 3, 4, 5, 6, 7, 8, 9, 10, 11};
            final int[] unSeatRows = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2,3 , 3, 3, 3, 4, 4};
            final int[] unSeatColumns = new int[]{1, 2, 3, 4, 12, 13, 14, 15, 1, 2, 3, 13, 14, 15, 1, 2, 14, 15, 1, 15};
            final int[] unFullRows = new int[]{7, 7, 7, 8, 8, 8};
            final int[] unFullColumns = new int[]{14, 12, 13, 13, 14, 12};
            seatView.setScreenName("八号仓库");
            seatView.setMaxSelected(1);
            seatView.setSeatChecker(new SeatTable.SeatChecker() {

                @Override
                public boolean isValidSeat(int row, int column) {
                    for (int i = 0; i < unSeatRows.length; i++) {
                        if (row == (unSeatRows[i] - 1) && column == (unSeatColumns[i] - 1)) {
                            return false;
                        }
                    }
                    return true;
                }

                @Override
                public boolean isSold(int row, int column) {
                    for (int i = 0; i < rows.length; i++) {
                        if (row == (rows[i] - 1) && column == (columns[i] - 1)) {
                            return true;
                        }
                    }
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
                    for (int i = 0; i < unFullRows.length; i++) {
                        if (row == (unFullRows[i] - 1) && column == (unFullColumns[i] - 1)) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public String[] checkedSeatTxt(int row, int column) {
                    return new String[]{column + "列"};
                }

            });
        }


    }

    @Override
    protected void initView() {
        seatView = (SeatTable) findViewById(R.id.seatView);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View view) {

    }
}
