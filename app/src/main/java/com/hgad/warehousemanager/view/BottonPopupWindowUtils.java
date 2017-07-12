package com.hgad.warehousemanager.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
    private TextView pop_switch;
    private Context context;
    private TextView pop_confirm;
    private FrameLayout fl_map;
    private LinearLayout ll_wheel;
    private WheelView wl_row;
    private WheelView wl_column;
    private WheelView wl_floor;
    private WheelView wl_ware;
    private WheelView wv_ware_num;
    private WheelView wv_floor;


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

    public BottonPopupWindowUtils(String ware, String floor, String title) {
        this.ware = ware;
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
        pop_switch = (TextView) popupWindowView.findViewById(R.id.pop_switch);
        pop_switch.setOnClickListener(switchListener);
        pop_address = (LinearLayout) popupWindowView.findViewById(R.id.pop_address);
        pop_map = (LinearLayout) popupWindowView.findViewById(R.id.pop_map);
        fl_map = (FrameLayout) popupWindowView.findViewById(R.id.fl_map);
        ll_wheel = (LinearLayout) popupWindowView.findViewById(R.id.ll_wheel);
        seatView = (SeatTable) popupWindowView.findViewById(R.id.seatView);
        TextView poptitle = (TextView) popupWindowView.findViewById(R.id.pop_title);
        poptitle.setText(title);
        wv_ware_num = (WheelView) popupWindowView.findViewById(R.id.wv_ware_num);
        wv_ware_num.setOffset(1);
        wv_ware_num.setItems(Arrays.asList(wareNums));
        wv_ware_num.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                ware = item;
            }
        });
        wv_floor = (WheelView) popupWindowView.findViewById(R.id.wv_floor);
        wv_floor.setOffset(1);
        wv_floor.setItems(Arrays.asList(floors));
        wv_floor.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                floor = item;
            }
        });
        wl_ware = (WheelView) popupWindowView.findViewById(R.id.wl_ware);
        wl_ware.setOffset(1);
        wl_ware.setItems(Arrays.asList(wareNums));
        wl_ware.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                ware = item;
            }
        });
        wl_floor = (WheelView) popupWindowView.findViewById(R.id.wl_floor);
        wl_floor.setOffset(1);
        wl_floor.setItems(Arrays.asList(floors));
        wl_floor.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                floor = item;
            }
        });

        wl_row = (WheelView) popupWindowView.findViewById(R.id.wl_row);
        wl_row.setOffset(1);
        wl_row.setItems(Arrays.asList(rows));
        wl_row.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                row = item;
            }
        });
        wl_column = (WheelView) popupWindowView.findViewById(R.id.wl_column);
        wl_column.setOffset(1);
        wl_column.setItems(Arrays.asList(columns));
        wl_column.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                column = item;
            }
        });
        row = wl_row.getSeletedItem();
        column = wl_column.getSeletedItem();
        ware = wl_ware.getSeletedItem();
        floor = wl_floor.getSeletedItem();
        return bottonPopupWindow;
    }

    public String getConfirmText() {
        return pop_confirm.getText().toString();
    }

    public void initSeatTable(final int[] rows, final int[] columns, final int[] unSeatRows, final int[] unSeatColums, int[] unFullRows, int[] unFullColums) {
        seatView.setData(15, 15);

        seatView.setMaxSelected(1);
        seatView.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                for (int i = 0; i < unSeatRows.length; i++) {
                    if (row == (unSeatRows[i] - 1) && column == (unSeatColums[i] - 1)) {
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
//                        if (row == (unFullRows[i] - 1) && column == (unFullColums[i] - 1)) {
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

    private View.OnClickListener switchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isMap = !isMap;
            if (isMap) {
                ll_wheel.setVisibility(View.INVISIBLE);
                fl_map.setVisibility(View.VISIBLE);
                pop_confirm.setText("下一步");
                ware = wv_ware_num.getSeletedItem();
                floor = wv_floor.getSeletedItem();
                row = null;
                column = null;
            } else {
                index = 1;
                pop_confirm.setText("确定");
                ll_wheel.setVisibility(View.VISIBLE);
                fl_map.setVisibility(View.INVISIBLE);
                row = wl_row.getSeletedItem();
                column = wl_column.getSeletedItem();
                ware = wl_ware.getSeletedItem();
                floor = wl_floor.getSeletedItem();
            }
        }
    };

    public void show(View parent, int grarity, int x, int y) {
        bottonPopupWindow.showAtLocation(parent, grarity, x, y);
        index = 1;
        pop_address.setVisibility(View.VISIBLE);
        pop_map.setVisibility(View.VISIBLE);
//        pop_switch.setVisibility(View.VISIBLE);
//        pop_confirm.setVisibility(View.INVISIBLE);
        pop_confirm.setText("确定");
    }

    private int index = 1;

    public void change() {
        if (index == 1) {
            seatView.setScreenName(ware + "号仓库");
            seatView.invalidate();
            pop_address.setVisibility(View.INVISIBLE);
            pop_switch.setVisibility(View.INVISIBLE);
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

    }
}
