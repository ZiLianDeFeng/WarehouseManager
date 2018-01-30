package com.hgad.warehousemanager.ui.activity;

import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.request.DataStatisticsRequest;
import com.hgad.warehousemanager.bean.request.HouseStatisticsRequest;
import com.hgad.warehousemanager.bean.response.DataStatisticsResponse;
import com.hgad.warehousemanager.bean.response.HouseStatisticsResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;
import com.hgad.warehousemanager.view.NewMarkerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */
public class DataStatisticsActivity extends BaseActivity implements OnChartValueSelectedListener {

    private RadioGroup rg;
    private XAxis xl;
    private HorizontalBarChart mHorizontalBarChart;
    private YValueFormatter valueFormatter;
    private List<String> names = new ArrayList<>();
    private ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
    //    private ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
    float spaceForBar = 10f;
    private PieChart mPieChartStock;
    private ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
    private TextView tv_show;
    private NewMarkerView markerView;
    private TranslateAnimation translateAnimation;
    private AnimationSet animationSet;
    private String startDate;
    private String endDate;
    private ArrayList<HouseBean> houseBeans = new ArrayList<>();
    private CustomProgressDialog mCustomProgressDialog;
    private boolean mIsConnect;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_data_statistics);
    }

    @Override
    protected void initData() {
        initHeader("数据统计");
//        showProgressDialog("请稍等");
        ((RadioButton) rg.getChildAt(0)).setChecked(true);
        getHouseData();
    }

    private void getHouseData() {
        HouseStatisticsRequest houseStatisticsRequest = new HouseStatisticsRequest();
        sendRequest(houseStatisticsRequest, HouseStatisticsResponse.class);
    }


    private void resetData() {
        if (Constants.DEBUG) {
            showProgressDialog("请稍等");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCustomProgressDialog.dismiss();
                    mIsConnect = true;
                }
            }, 1000);
            entries.clear();
            yVals1.clear();
            names.clear();
            for (int i = 0; i < 10; i++) {
                int val = (int) (Math.random() * 500);
                int va2 = (int) (Math.random() * 500);
                yVals1.add(new BarEntry(i * spaceForBar, new float[]{val, va2}));
//            yVals2.add(new BarEntry(i * spaceForBar, va2));
                names.add(i + "");
            }
            xl.setLabelCount(names.size());
            int total = 0;
            for (int i = 0; i < 6; i++) {
                int val = (int) (Math.random() * 1000);
                entries.add(new PieEntry(val, i + 1 + "号仓库"));
                total += val;
            }
            mPieChartStock.setCenterText(generateCenterSpannableText(total + "\n库存统计"));
            setData();
            setData(entries, mPieChartStock);
        } else {
            showProgressDialog("请稍等");
            DataStatisticsRequest dataStatisticsRequest = new DataStatisticsRequest(startDate, endDate);
            sendRequest(dataStatisticsRequest, DataStatisticsResponse.class);
        }
    }

    //设置中间文字
    private SpannableString generateCenterSpannableText(String count) {
        SpannableString s = new SpannableString(count);
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//         s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//         s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//         s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    protected void initView() {
        initPieChart();
        initHorizontalBarChart();
        rg = (RadioGroup) findViewById(R.id.rg_performance);
        rg.setOnCheckedChangeListener(checkedChangeListener);
        tv_show = (TextView) findViewById(R.id.tv_show);
    }

    private void initPieChart() {
        mPieChartStock = (PieChart) findViewById(R.id.mPieChart_stock);
        mPieChartStock.setOnChartValueSelectedListener(this);
        mPieChartStock.setUsePercentValues(false);
        mPieChartStock.getDescription().setEnabled(false);
        mPieChartStock.setExtraOffsets(5, 10, 5, 5);
        mPieChartStock.setDragDecelerationFrictionCoef(0.95f);

        mPieChartStock.setDrawSliceText(false);
        mPieChartStock.setDrawHoleEnabled(true);
        mPieChartStock.setHoleColor(Color.TRANSPARENT);

        mPieChartStock.setTransparentCircleColor(Color.TRANSPARENT);
        mPieChartStock.setTransparentCircleAlpha(110);

        mPieChartStock.setCenterTextSize(16f);
        mPieChartStock.setCenterTextColor(Color.WHITE);

        mPieChartStock.setHoleRadius(58f);
        mPieChartStock.setTransparentCircleRadius(61f);

        mPieChartStock.setDrawCenterText(true);

        mPieChartStock.setRotationAngle(0);
        // 触摸旋转
        mPieChartStock.setRotationEnabled(true);
        mPieChartStock.setHighlightPerTapEnabled(true);

        mPieChartStock.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        mPieChartStock.setNoDataText(getResources().getString(R.string.no_data));
        int width = getWindowManager().getDefaultDisplay().getWidth();
        markerView = new NewMarkerView(this, R.layout.custom_marker_view_layout, width);
        animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(alphaAnimation);
        translateAnimation = new TranslateAnimation(0, 0, -100f, 0);
        translateAnimation.setDuration(1000);
        // set the marker to the chart
        mPieChartStock.setDrawMarkers(true);
        mPieChartStock.setMarker(markerView);

        //变化监听
        mPieChartStock.setOnChartValueSelectedListener(this);
        Legend legend = mPieChartStock.getLegend();  //设置比例图
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);  //左下边显示
        legend.setFormSize(8f);//比例块字体大小
        legend.setXEntrySpace(3f);//设置距离饼图的距离，防止与饼图重合
        legend.setYEntrySpace(2f);
        //设置比例块换行...
//        mLegend.setWordWrapEnabled(true);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);

        legend.setTextColor(Color.WHITE);
        legend.setForm(Legend.LegendForm.SQUARE);//设置比例块形状，默认为方块
    }

    private void initHorizontalBarChart() {
        mHorizontalBarChart = (HorizontalBarChart) findViewById(R.id.mHorizontalBarChart);
        //设置相关属性
        mHorizontalBarChart.setOnChartValueSelectedListener(this);
        mHorizontalBarChart.setDrawBarShadow(false);
        mHorizontalBarChart.setDrawValueAboveBar(true);
        mHorizontalBarChart.getDescription().setEnabled(false);
        mHorizontalBarChart.setMaxVisibleValueCount(60);
        mHorizontalBarChart.setPinchZoom(false);
        mHorizontalBarChart.setDrawGridBackground(false);

        //x轴
        xl = mHorizontalBarChart.getXAxis();
        valueFormatter = new YValueFormatter();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        xl.setValueFormatter(valueFormatter);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);
        xl.setTextSize(11f);
        xl.setLabelCount(10);
        xl.setTextColor(Color.WHITE);

        //y轴
        YAxis yl = mHorizontalBarChart.getAxisLeft();
        yl.setEnabled(false);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0.1f);
        yl.setTextColor(Color.WHITE);

        //y轴
        YAxis yr = mHorizontalBarChart.getAxisRight();
        yr.setDrawAxisLine(false);
        yr.setEnabled(true);
        yr.setDrawGridLines(true);
        yr.setAxisMinimum(0.1f);
        yr.setTextColor(Color.WHITE);
        yr.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                java.text.DecimalFormat df = new java.text.DecimalFormat("#0");
                String format = df.format(value);
                return "" + format + "吨";//这句是重点!
            }
        });

        mHorizontalBarChart.setFitBars(true);
        mHorizontalBarChart.animateY(2500);
        mHorizontalBarChart.setNoDataText(getResources().getString(R.string.no_data));

        Legend l = mHorizontalBarChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setTextSize(12f);
        l.setFormSize(12f);
        l.setTextColor(Color.WHITE);
    }

    RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_day:
                    getRange("day");
                    resetData();
                    break;
                case R.id.rb_month:
                    getRange("month");
                    resetData();
                    break;
                case R.id.rb_total:
                    getRange("total");
                    resetData();
                    break;
            }
        }
    };

    //设置数据
    private void setData(ArrayList<PieEntry> entries, PieChart view) {
        PieDataSet dataSet =  new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineColor(Color.WHITE);//设置连接线的颜色
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter());
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return ((int) entry.getY()) + "";
            }
        });
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        view.setData(data);
        view.highlightValues(null);
        //刷新
        view.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setData() {
        float barWidth = 8f;
        valueFormatter.setmValues(names);
        BarDataSet set1;
//        BarDataSet set2;
        if (mHorizontalBarChart.getData() != null &&
                mHorizontalBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mHorizontalBarChart.getData().getDataSetByIndex(0);
//            set2 = (BarDataSet) mHorizontalBarChart.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
//            set2.setValues(yVals2);
            mHorizontalBarChart.getData().notifyDataChanged();
            mHorizontalBarChart.notifyDataSetChanged();
            mHorizontalBarChart.animateY(2500);
        } else {
            set1 = new BarDataSet(yVals1, "");
//            set1.setColors(getColors(3)[0]);
            set1.setColors(getColors(3)[0], getColors(1)[0]);
            set1.setDrawValues(true);
            set1.setStackLabels(new String[]{"入库", "出库"});
            set1.setValueTextColor(Color.WHITE);
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                    int n = (int) value;
                    return value + "";
                }
            });
//            set2 = new BarDataSet(yVals2, "");
//            set2.setColors(getColors(1));
//            set2.setDrawValues(true);
//            set2.setValueTextColor(Color.WHITE);
//            set2.setValueFormatter(new IValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                    int n = (int) value;
//                    return n + "";
//                }
//            });
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
//            dataSets.add(set2);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);
            mHorizontalBarChart.setData(data);
            mHorizontalBarChart.animateY(2500);
        }
    }

    private int[] getColors(int i) {
        int stacksize = 1;
        //有尽可能多的颜色每项堆栈值
        int[] colors = new int[stacksize];
        colors[0] = ColorTemplate.MATERIAL_COLORS[i];
        return colors;
    }

    private void getRange(String type) {
        String currentTime = CommonUtils.getCurrentTime();
        currentTime = currentTime.substring(0, 10);
        int year = Integer.parseInt(currentTime.substring(0, 4));
        int month = Integer.parseInt(currentTime.substring(5, 7));
        if ("day".equals(type)) {
            startDate = currentTime;
            endDate = currentTime;
        } else if ("month".equals(type)) {
            Date beginDayofMonth = CommonUtils.getSupportBeginDayofMonth(year, month);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String beginDate = dateFormat.format(beginDayofMonth);
            startDate = beginDate;
            Date endDayofMonth = CommonUtils.getSupportEndDayofMonth(year, month);
            String endDate = dateFormat.format(endDayofMonth);
            this.endDate = endDate;
        } else if ("total".equals(type)) {
            startDate = year + "-01-01";
            endDate = year + "-12-31";
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        mIsConnect = true;
        if (mCustomProgressDialog != null) {
            mCustomProgressDialog.dismiss();
        }
        if (request instanceof DataStatisticsRequest) {
            DataStatisticsResponse dataStatisticsResponse = (DataStatisticsResponse) response;
            if (dataStatisticsResponse != null) {
                if (dataStatisticsResponse.getResponseCode().getCode() == 200) {
                    List<DataStatisticsResponse.DataEntity> data = dataStatisticsResponse.getData();
                    yVals1.clear();
                    names.clear();
                    for (int i = 0; i < data.size(); i++) {
                        DataStatisticsResponse.DataEntity dataEntity = data.get(i);
                        String operator = dataEntity.getOperator();
                        String inType = dataEntity.getInType();
                        String outType = dataEntity.getOutType();
                        double total = dataEntity.getTotal();
//                        java.text.DecimalFormat df = new java.text.DecimalFormat("#0");
//                        String format = df.format(total);
                        names.add(operator);
//                        yVals1.add(new BarEntry(i * spaceForBar, new float[]{((float) total), 0}));
                        yVals1.add(new BarEntry(i * spaceForBar, new float[]{Float.parseFloat(inType == null ? "0" : inType.trim()), Float.parseFloat(outType == null ? "0" : outType.trim())}));
                    }
                    xl.setLabelCount(names.size());
                    setData();
                }
            }
        } else if (request instanceof HouseStatisticsRequest) {
            HouseStatisticsResponse houseStatisticsResponse = (HouseStatisticsResponse) response;
            if (houseStatisticsResponse != null) {
                if (houseStatisticsResponse.getResponseCode().getCode() == 200) {
                    List<HouseStatisticsResponse.DataEntity> data = houseStatisticsResponse.getData();
                    int total = 0;
                    for (int i = 0; i < data.size(); i++) {
                        HouseStatisticsResponse.DataEntity dataEntity = data.get(i);
                        int store = dataEntity.getFixed();
                        String name = dataEntity.getName();
                        houseBeans.add(new HouseBean(name, dataEntity.getRows() * dataEntity.getCols() * dataEntity.getStorey()));
                        total += store;
                        entries.add(new PieEntry(store, name));
                    }
                    mPieChartStock.setCenterText(generateCenterSpannableText(total + "\n库存统计"));
                    setData(entries, mPieChartStock);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {
        if (entry instanceof PieEntry) {
//            markerView.startAnimation(animationSet);
            PieEntry pieEntry = (PieEntry) entry;
            int value = (int) pieEntry.getValue();
            String label = pieEntry.getLabel();
            int total = 0;
            for (HouseBean houseBean : houseBeans) {
                if (label.equals(houseBean.getLabel())) {
                    total = houseBean.getFixed();
                }
            }
//            tv_show.setText(label + "的库存为" + value);
            TextView tvContent = (TextView) markerView.findViewById(R.id.tvContent);
            tvContent.setText(value + " / " + total);
        }
    }

    @Override
    public void onNothingSelected() {

    }

    private void showProgressDialog(String content) {
        mCustomProgressDialog = new CustomProgressDialog(this, content);
        mCustomProgressDialog.setCancelable(false);
        mCustomProgressDialog.setCanceledOnTouchOutside(false);
        mCustomProgressDialog.show();
        mIsConnect = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mIsConnect) {
                    if (mCustomProgressDialog != null) {
                        mCustomProgressDialog.dismiss();
                        CommonUtils.showToast(DataStatisticsActivity.this, getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }

    public class YValueFormatter implements IAxisValueFormatter {

        private List<String> mValues;

        public void setmValues(List<String> mValues) {
            this.mValues = mValues;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (mValues.size() != 0) {
                return mValues.get((int) value / 10 % mValues.size());
            }
            return "";
        }
    }

    public class HouseBean {
        private String label;
        private int fixed;

        public HouseBean(String label, int fixed) {
            this.label = label;
            this.fixed = fixed;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getFixed() {
            return fixed;
        }

        public void setFixed(int fixed) {
            this.fixed = fixed;
        }
    }
}
