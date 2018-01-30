package com.hgad.warehousemanager.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;

/**
 * Created by Administrator on 2017/9/5.
 */
public class AddAndSubText extends LinearLayout implements View.OnClickListener {
    private Button btn_sub;
    private Button btn_add;
    private TextView tv_value;
    //属性监听
    private int value = 1;//默认值
    private int minValue = 1;//最小值
    private int maxValue = 10;//最大值

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if (type.equals("row") || type.equals("column")) {
            btn_add.setVisibility(INVISIBLE);
        }
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getValue() {
        String valueStr = tv_value.getText().toString().trim();//文本的内容
        if (!TextUtils.isEmpty(valueStr)) {
            value = Integer.valueOf(valueStr);
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        if (value < 10) {
            tv_value.setText("0" + value);
        } else {
            tv_value.setText(value + "");
        }
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public AddAndSubText(Context context) {
        this(context, null);
    }

    public AddAndSubText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public AddAndSubText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //要让布局和当前类形成一个整体
        View.inflate(context, R.layout.number_add_sub_view, this);
        btn_sub = (Button) findViewById(R.id.btn_sub);
        btn_add = (Button) findViewById(R.id.btn_add);
        tv_value = (TextView) findViewById(R.id.tv_value);
        getValue();//获得当前值
        //设置点击事件
        btn_add.setOnClickListener(this);
        btn_sub.setOnClickListener(this);

        //得到属性
        if (attrs != null) {
            TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes

                    (context, attrs, R.styleable.NumberAddSubView);
            int value = typedArray.getInt(R.styleable.NumberAddSubView_value, 0);
            if (value > 0) {
                setValue(value);
            }
            int minValue = typedArray.getInt(R.styleable.NumberAddSubView_minValue, 0);
            if (minValue > 0) {
                setMinValue(minValue);
            }
            int maxValue = typedArray.getInt(R.styleable.NumberAddSubView_maxValue, 0);
            if (maxValue > 0) {
                setMaxValue(maxValue);
            }

            Drawable numberAddSubBackground = typedArray.getDrawable

                    (R.styleable.NumberAddSubView_NumberAddSubBackground);
            if (numberAddSubBackground != null) {
                setBackground(numberAddSubBackground);
            }
            Drawable numberAddBackground = typedArray.getDrawable

                    (R.styleable.NumberAddSubView_NumberAddBackground);
            if (numberAddBackground != null) {
                btn_add.setBackground(numberAddBackground);
            }
            Drawable numberSubBackground = typedArray.getDrawable

                    (R.styleable.NumberAddSubView_NumberSubBackground);
            if (numberSubBackground != null) {
                btn_sub.setBackground(numberSubBackground);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sub://减
                subNumber();

                break;
            case R.id.btn_add://加
                new AlertView("提示", "确定当前" + (type.equals("row") ? "排" : type.equals("column") ? "垛" : "号") + "位无剩余货品需扫描\n并切换到下一" + (type.equals("row") ? "排" : type.equals("column") ? "垛" : "号"), "取消", new String[]{"确定"}, null, getContext(), AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                addNumber();
                                break;
                        }
                    }
                }).setCancelable(false).show();
                break;
        }
    }

    /**
     * 减
     */
    public void subNumber() {
        if (value > minValue) {
            value -= 1;
        }
        if (listener != null) {
            listener.onButtonSub(value);
        }
        setValue(value);
    }

    /**
     * 加
     */
    public void addNumber() {
        if (value < maxValue) {
            if ("floor".equals(type) && (value == 9 || value == 19)) {
//                value += 2;
                value = value + 2;
            } else {
                value = value + 1;
            }
        }
        if (listener != null) {
            listener.onButtonAdd(value);
        }
        setValue(value);
    }

    /**
     * 监听数字增加减少控件
     */
    public interface OnNumberClickListener {
        /**
         * 当减少按钮被点击的时候回调
         *
         * @param value
         */
        void onButtonSub(int value);

        /**
         * 当增加按钮被点击的时候回调
         *
         * @param value
         */
        void onButtonAdd(int value);

    }

    public OnNumberClickListener listener;

    /**
     * 设置监听数字按钮
     *
     * @param listener
     */
    public void setOnNumberClickListener(OnNumberClickListener listener) {
        this.listener = listener;
    }
}
