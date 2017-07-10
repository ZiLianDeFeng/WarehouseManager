package com.hgad.warehousemanager.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.util.CommonUtils;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/7/4.
 */
public class BottonPopupWindowUtils {
    private String ware;
    private String row;
    private String column;
    private String floor;
    private String title;
    private PopupWindow bottonPopupWindow;
    private boolean isMap;
    private LinearLayout pop_address;
    private LinearLayout pop_map;
    private SeatTable seatView;
    //    private TextView pop_switch;
    private Context context;
    private TextView pop_confirm;

    public String getWare() {
        return ware;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }

    public String getFloor() {
        return floor;
    }

    public PopupWindow getBottonPopupWindow() {
        return bottonPopupWindow;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMap() {
        return isMap;
    }

    public void setMap(boolean map) {
        isMap = map;
    }

    public BottonPopupWindowUtils(String ware, String row, String column, String floor, String title) {
        this.ware = ware;
        this.row = row;
        this.column = column;
        this.floor = floor;
        this.title = title;
        isMap = false;
    }


    public PopupWindow creat(Context context, String[] wareNums, String[] rows, String[] columns, String[] floors, View.OnClickListener listener) {
        this.context = context;
        View popupWindowView = View.inflate(context, R.layout.popupwindow_address, null);
        bottonPopupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置PopupWindow的弹出和消失效果
        bottonPopupWindow.setAnimationStyle(R.style.popupAnimation);
        bottonPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        bottonPopupWindow.setOutsideTouchable(true);
        pop_confirm = (TextView) popupWindowView.findViewById(R.id.pop_confirm);
        pop_confirm.setOnClickListener(listener);
        popupWindowView.findViewById(R.id.pop_cancle).setOnClickListener(listener);
//        pop_switch = (TextView) popupWindowView.findViewById(R.id.pop_switch);
//        pop_switch.setOnClickListener(listener);
        pop_address = (LinearLayout) popupWindowView.findViewById(R.id.pop_address);
        pop_map = (LinearLayout) popupWindowView.findViewById(R.id.pop_map);
        seatView = (SeatTable) popupWindowView.findViewById(R.id.seatView);
        initSeatTable();
        TextView poptitle = (TextView) popupWindowView.findViewById(R.id.pop_title);
        poptitle.setText(title);
        WheelView wv_ware_num = (WheelView) popupWindowView.findViewById(R.id.wv_ware_num);
        wv_ware_num.setOffset(1);
        wv_ware_num.setItems(Arrays.asList(wareNums));
        wv_ware_num.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                ware = item;
            }
        });
//        WheelView wv_row = (WheelView) popupWindowView.findViewById(R.id.wv_row);
//        wv_row.setOffset(1);
//        wv_row.setItems(Arrays.asList(rows));
//        wv_row.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
//                row = item;
//            }
//        });
//        WheelView wv_column = (WheelView) popupWindowView.findViewById(R.id.wv_column);
//        wv_column.setOffset(1);
//        wv_column.setItems(Arrays.asList(columns));
//        wv_column.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
//                column = item;
//            }
//        });
        WheelView wv_floor = (WheelView) popupWindowView.findViewById(R.id.wv_floor);
        wv_floor.setOffset(1);
        wv_floor.setItems(Arrays.asList(floors));
        wv_floor.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                floor = item;
            }
        });
        return bottonPopupWindow;
    }

    public String getConfirmText() {
        return pop_confirm.getText().toString();
    }

    private void initSeatTable() {
        seatView.setData(15, 15);
        final int[] rows = new int[]{4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6};
        final int[] columns = new int[]{4, 5, 6, 7, 8, 9, 10, 4, 5, 6, 7, 8, 9, 10, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        final int[] unSeatRows = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4};
        final int[] unSeatColumns = new int[]{1, 2, 3, 4, 12, 13, 14, 15, 1, 2, 3, 13, 14, 15, 1, 2, 14, 15, 1, 15};
        final int[] unFullRows = new int[]{7, 7, 7, 8, 8, 8};
        final int[] unFullColumns = new int[]{14, 12, 13, 13, 14, 12};

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
                BottonPopupWindowUtils.this.row = row + 1 + "";
                BottonPopupWindowUtils.this.column = column + 1 + "";
            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public boolean isUnFull(int row, int column) {
//                    for (int i = 0; i < unFullRows.length; i++) {
//                        if (row == (unFullRows[i] - 1) && column == (unFullColumns[i] - 1)) {
//                            return true;
//                        }
//                    }
                return false;
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
    }

    public void show(View parent, int grarity, int x, int y) {
        bottonPopupWindow.showAtLocation(parent, grarity, x, y);
        index = 1;
        pop_address.setVisibility(View.VISIBLE);
        pop_map.setVisibility(View.VISIBLE);
//        pop_switch.setVisibility(View.VISIBLE);
//        pop_confirm.setVisibility(View.INVISIBLE);
        pop_confirm.setText("下一步");
    }

    private int index = 1;

    public void change() {
        if (index == 1) {
            seatView.setScreenName(ware + "号仓库");
            seatView.invalidate();
            pop_address.setVisibility(View.INVISIBLE);
            index++;
        } else if (index == 2) {
            if (row == null || column == null) {
                CommonUtils.showToast(context, "还未选择地址");
                return;
            }
            pop_map.setVisibility(View.INVISIBLE);
//            pop_switch.setVisibility(View.INVISIBLE);
//            pop_confirm.setVisibility(View.VISIBLE);
            pop_confirm.setText("确定");
            index++;
        } else if (index == 3) {

        }
//        isMap = !isMap;
//        if (isMap) {
//            pop_address.setVisibility(View.INVISIBLE);
//            pop_map.setVisibility(View.VISIBLE);
//        } else {
//            pop_address.setVisibility(View.VISIBLE);
//            pop_map.setVisibility(View.INVISIBLE);
//        }
    }
}
