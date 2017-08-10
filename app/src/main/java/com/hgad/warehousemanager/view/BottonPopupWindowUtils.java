package com.hgad.warehousemanager.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.request.WareHouseRequest;
import com.hgad.warehousemanager.bean.response.ErrorResponseInfo;
import com.hgad.warehousemanager.bean.response.WareHouseResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.Callback;
import com.hgad.warehousemanager.net.NetUtil;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.CommonViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */
public class BottonPopupWindowUtils implements Callback {
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
    //    private WheelView wv_floor;
    private String type;
    private LinearLayout ll_ware;
    private LinearLayout ll_map_ware;
    private TextView tv_fixed_ware;
    private TextView tv_fixed_map_ware;
    private boolean connect;
    private AlertView mAlertViewExt;
    private CustomProgressDialog customProgressDialog;
    private String[] numbers;
    private String chooseFloor;


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

    public BottonPopupWindowUtils(String ware, String floor, String title, String type) {
        this.ware = ware;
        this.floor = floor;
        this.title = title;
        this.type = type;
        isMap = false;
    }

    public PopupWindow creat(Context context, String[] wareNums, String[] rows, String[] columns, String[] floors, View.OnClickListener listener) {
        this.numbers = floors;
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
//        ll_ware = (LinearLayout) popupWindowView.findViewById(R.id.ll_ware);
//        ll_map_ware = (LinearLayout) popupWindowView.findViewById(R.id.ll_map_ware);
        tv_fixed_ware = (TextView) popupWindowView.findViewById(R.id.tv_fixed_ware);
        tv_fixed_map_ware = (TextView) popupWindowView.findViewById(R.id.tv_fixed_map_ware);
        wv_ware_num = (WheelView) popupWindowView.findViewById(R.id.wv_ware_num);
        wv_ware_num.setOffset(1);
        wv_ware_num.setItems(Arrays.asList(wareNums));
        wv_ware_num.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                ware = item;
            }
        });
//        wv_floor = (WheelView) popupWindowView.findViewById(R.id.wv_floor);
//        wv_floor.setOffset(1);
//        wv_floor.setItems(Arrays.asList(floors));
//        wv_floor.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
//                floor = item;
//            }
//        });
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

    private List<String> unSeatRows = new ArrayList<>();
    private List<String> unSeatColums = new ArrayList<>();
    private List<String> seatRows = new ArrayList<>();
    private List<String> seatColums = new ArrayList<>();
    private List<String> unFullRows = new ArrayList<>();
    private List<String> unFullColums = new ArrayList<>();

    public void initSeatTable(final List<String> rows, final List<String> columns, final List<String> unSeatRows, final List<String> unSeatColums, final List<String> unFullRows, final List<String> unFullColums) {
        seatView.setType(Constants.CHOOSE);
        seatView.setMaxSelected(1);
        seatView.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                for (int i = 0; i < unSeatRows.size(); i++) {
                    if (row == (Integer.parseInt(unSeatRows.get(i)) - 1) && column == (Integer.parseInt(unSeatColums.get(i)) - 1)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                for (int i = 0; i < rows.size(); i++) {
                    if (row == (Integer.parseInt(rows.get(i)) - 1) && column == (Integer.parseInt(columns.get(i)) - 1)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void checked(int row, int column) {
                if (row < 9) {
                    BottonPopupWindowUtils.this.row = "0" + (row + 1);
                } else {
                    BottonPopupWindowUtils.this.row = "" + (row + 1);
                }
                if (column < 9) {
                    BottonPopupWindowUtils.this.column = "0" + (column + 1);
                } else {
                    BottonPopupWindowUtils.this.column = "" + (column + 1);
                }
                showFloorDialog();
            }

            @Override
            public void unCheck(int row, int column) {
                BottonPopupWindowUtils.this.row = null;
                BottonPopupWindowUtils.this.column = null;
            }

            @Override
            public boolean isUnFull(int row, int column) {
                for (int i = 0; i < unFullRows.size(); i++) {
                    if (row == (Integer.parseInt(unFullRows.get(i)) - 1) && column == (Integer.parseInt(unFullColums.get(i)) - 1)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean isCurrentSeat(int row, int column) {
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

    private void showFloorDialog() {
        chooseFloor = null;
        //拓展窗口
        ViewGroup extView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.alertext_floor, null);
        GridView gl = (GridView) extView.findViewById(R.id.gl_floor);
        final NumberAdapter numberAdapter = new NumberAdapter();
        gl.setAdapter(numberAdapter);
        gl.setNumColumns(9);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).setTitle("选择层号")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (chooseFloor!=null) {
                            floor = chooseFloor;
                            pop_switch.setText(floor + "层");
                        }else {
                            CommonUtils.showToast(context,"未选择层号");
                        }
                    }
                }).setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.setView(extView);
        alertDialog.show();
    }

    class NumberAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return numbers.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_number);
            final TextView tv_number = (TextView) holder.getView(R.id.tv_number);
            tv_number.setText(numbers[position]);
            tv_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        chooseFloor = ((TextView) v).getText().toString().trim();
                    }
                }
            });
            return holder.convertView;
        }
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
//                floor = wv_floor.getSeletedItem();
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
//                floor = wl_floor.getSeletedItem();
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
        if (Constants.CHANGE_WARE.equals(type)) {
            wv_ware_num.setVisibility(View.INVISIBLE);
            wl_ware.setVisibility(View.INVISIBLE);
            tv_fixed_map_ware.setText(ware);
            tv_fixed_map_ware.setVisibility(View.VISIBLE);
            tv_fixed_ware.setText(ware);
            tv_fixed_ware.setVisibility(View.VISIBLE);
        }
    }

    private int index = 1;
    Handler mainHandler = new Handler(Looper.getMainLooper());

    public void change() {
        if (index == 1) {
            boolean isNetWork = CommonUtils.checkNetWork(context);
            if (isNetWork) {
                connect = false;
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!connect) {
                            seatView.setData(15, 99);
                            seatView.setScreenName(ware+"号仓库");
                            initSeatTable(seatRows, seatColums, unSeatRows, unSeatColums, unFullRows, unFullColums);
                            seatView.invalidate();
                        }
                    }
                }, 2000);

                WareHouseRequest wareHouseRequest = new WareHouseRequest(ware);
                NetUtil.sendRequest(wareHouseRequest, WareHouseResponse.class, this);
                pop_address.setVisibility(View.INVISIBLE);
//                pop_switch.setVisibility(View.INVISIBLE);
                pop_switch.setText(floor+"层");
                pop_switch.setOnClickListener(null);
                pop_confirm.setText("确定");
                index++;
            } else {
                CommonUtils.showToast(context, context.getString(R.string.check_net));
            }
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

    @Override
    public void onSuccess(BaseRequest request, Object response) {
        if (request instanceof WareHouseRequest) {
            connect = true;
            WareHouseResponse wareHouseResponse = (WareHouseResponse) response;
            if (wareHouseResponse.getResponseCode().getCode() == 200) {
                if (wareHouseResponse.getErrorMsg().equals("请求成功")) {
                    WareHouseResponse.DataEntity dataEntity = wareHouseResponse.getData().get(0);
                    int rows = dataEntity.getRows();
                    int cols = dataEntity.getCols();
                    String name = dataEntity.getName();
                    List<WareHouseResponse.DataEntity.PositionListEntity> positionList = dataEntity.getPositionList();
                    for (WareHouseResponse.DataEntity.PositionListEntity positionListEntity : positionList) {
                        String raw = positionListEntity.getPositionCode().substring(2, 4);
                        String column = positionListEntity.getPositionCode().substring(4, 6);
                        List<String> storey = positionListEntity.getStorey();
                        boolean hava = false;
                        boolean unfull = false;
                        if (storey.get(0) != null) {
                            hava = true;
                        }
                        if (storey.get(storey.size() - 1) == null) {
                            unfull = true;
                        }
                        if (hava) {
                            if (unfull) {
                                unFullRows.add(raw);
                                unFullColums.add(column);
                            } else {
                                seatRows.add(raw);
                                seatColums.add(column);
                            }
                        }
                    }
                    seatView.setData(rows, cols);
                    initSeatTable(seatRows, seatColums, unSeatRows, unSeatColums, unFullRows, unFullColums);
                    seatView.setScreenName(name);
                    seatView.invalidate();
                }
            }
        }
    }

    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {

    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }
}
