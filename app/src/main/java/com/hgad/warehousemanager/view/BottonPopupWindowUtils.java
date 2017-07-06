package com.hgad.warehousemanager.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hgad.warehousemanager.R;

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

    public BottonPopupWindowUtils(String ware, String row, String column, String floor, String title) {
        this.ware = ware;
        this.row = row;
        this.column = column;
        this.floor = floor;
        this.title = title;
    }

    public PopupWindow creat(Context context, String[] wareNums, String[] rows, String[] columns, String[] floors, View.OnClickListener listener) {
        View popupWindowView = View.inflate(context, R.layout.popupwindow_address, null);
        bottonPopupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置PopupWindow的弹出和消失效果
        bottonPopupWindow.setAnimationStyle(R.style.popupAnimation);
        bottonPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        bottonPopupWindow.setOutsideTouchable(true);
        popupWindowView.findViewById(R.id.pop_confirm).setOnClickListener(listener);
        popupWindowView.findViewById(R.id.pop_cancle).setOnClickListener(listener);
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
        WheelView wv_row = (WheelView) popupWindowView.findViewById(R.id.wv_row);
        wv_row.setOffset(1);
        wv_row.setItems(Arrays.asList(rows));
        wv_row.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                row = item;
            }
        });
        WheelView wv_column = (WheelView) popupWindowView.findViewById(R.id.wv_column);
        wv_column.setOffset(1);
        wv_column.setItems(Arrays.asList(columns));
        wv_column.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                column = item;
            }
        });
        WheelView wv_floor = (WheelView) popupWindowView.findViewById(R.id.wv_floor);
        wv_floor.setOffset(1);
        wv_floor.setItems(Arrays.asList(floors));
        wv_floor.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                floor = item;
            }
        });
//        bottonPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(1f);
//            }
//        });
        return bottonPopupWindow;
    }

    public void show(View parent, int grarity, int x, int y) {
        bottonPopupWindow.showAtLocation(parent, grarity, x, y);
    }
}
