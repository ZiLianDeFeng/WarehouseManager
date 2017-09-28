package com.hgad.warehousemanager.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.WeatherEntity;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.util.SPUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Created by huangda on 2016/8/14.
 */
public class WeatherActivity extends Activity {

    //自定义变量
    private TextView cityNameText;       //用于显示城市名
    private TextView publishText;        //用于显示发布时间
    private TextView weatherDespText;    //用于显示天气描述信息
    private TextView temp1Text;          //用于显示最低气温
    private TextView temp2Text;          //用于显示最高气温
    private TextView currentDateText;    //用于显示当前日期
    //更新天气按钮
    private String weatherJson;          //获取JSON格式
//    private String mWeatherCode;         //天气代码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent = getIntent();
        String weatherCode = intent.getStringExtra("weatherCode");
        if (TextUtils.isEmpty(weatherCode)) {
            weatherCode = "101200101";
        }
        Log.d("WeatherActivity", "weatherCode" + weatherCode);
        cityNameText = (TextView) findViewById(R.id.tv_title);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
//        Button changeCity = (Button) findViewById(R.id.refresh_weather);
        currentDateText = (TextView) findViewById(R.id.current_date);
        TextView changeCity = (TextView) findViewById(R.id.btn_confirm);
        changeCity.setText("切换");
        changeCity.setVisibility(View.VISIBLE);
        changeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCity();
            }
        });
        //执行线程访问http
        //否则 NetworkOnMainThreadException
        selectWeather(weatherCode);

    }

    private void selectWeather(final String weatherCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //访问中国天气网
                final String weatherUrl = "http://wthrcdn.etouch.cn/weather_mini?citykey="
                        + weatherCode;
                weatherJson = queryStringForGet(weatherUrl);
                Message message = new Message();
                message.obj = weatherJson;
                handler.sendMessage(message);
            }
        }).start();
    }


    public void back(View view) {
        onBackPressed();
        finish();
    }

    /**
     * 解析Json格式数据并显示
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String weatherJson = (String) msg.obj;
            try {
                Gson gson = new Gson();
                WeatherEntity weatherEntity = gson.fromJson(weatherJson, WeatherEntity.class);
                WeatherEntity.DataEntity data = weatherEntity.getData();
                List<WeatherEntity.DataEntity.ForecastEntity> forecast = data.getForecast();
                WeatherEntity.DataEntity.ForecastEntity today = forecast.get(0);
                cityNameText.setText(data.getCity() + "(天气)");
                String low = today.getLow();
                String lowTemperature = low.substring(low.indexOf("温") + 1, low.length()).trim();
                String high = today.getHigh();
                String highTemperature = high.substring(high.indexOf("温") + 1, high.length()).trim();
                temp1Text.setText(lowTemperature);
                temp2Text.setText(highTemperature);
                weatherDespText.setText(today.getType());
                publishText.setText("当前温度：" + data.getWendu() + "℃");
                //获取当前日期
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
                currentDateText.setText(today.getDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 网络查询
     */
    private String queryStringForGet(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        String result = null;
        try {
            okhttp3.Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                result = response.body().string();
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void changeCity() {
        //创建对话框
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("请选择一个城市");
        //指定下拉列表的显示数据
        final String[] cities = {"北京", "上海", "武汉", "天津", "广州", "杭州", "贵阳", "台北", "香港", "合肥",
                "福州", "南昌", "长沙", "成都", "昆明", "南宁", "海口", "哈尔滨", "重庆", "长春", "沈阳",
                "呼和浩特", "石家庄"};
        final String[] codes = {"101010100", "101020100", "101200101", "101030100", "101280101", "101210101",
                "101260101", "101340101", "101320101", "101220101", "101230101", "101240101", "101250101",
                "101270101", "101290101", "101300101", "101310101", "101050101", "101040100", "101060101",
                "101070101", "101080101", "101090101"};
//        builder.setItems(cities, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String weatherCode = codes[which];
//                SPUtils.put(WeatherActivity.this, SPConstants.WEATHER_CITY, weatherCode);
//                selectWeather(weatherCode);
//            }
//        });
//        builder.show();

        new AlertView("请选择一个城市", null, null, null, cities, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                String weatherCode = codes[position];
                SPUtils.put(WeatherActivity.this, SPConstants.WEATHER_CITY, weatherCode);
                selectWeather(weatherCode);
            }
        }).setCancelable(true).show();
    }
}
