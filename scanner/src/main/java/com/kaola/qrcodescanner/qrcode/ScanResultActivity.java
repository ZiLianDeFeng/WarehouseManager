package com.kaola.qrcodescanner.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kaola.qrcodescanner.R;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/9/1.
 */
public class ScanResultActivity extends Activity {

    private TextView tv_test;
    private TextView tv_test1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        getResultIfnotNull(result);
    }

    private void getResultIfnotNull(String resultStr) {
        if (resultStr != null) {
            String encoding = CommonUtils.getEncoding(resultStr);
            String UTF_Str = "";
            String GB_Str = "";
            boolean is_cN = false;
            try {
                if (encoding.equals("GB2312")) {
                    UTF_Str = new String(resultStr.getBytes("GB2312"), "GB2312");
                } else if (encoding.equals("ISO-8859-1")) {
                    UTF_Str = new String(resultStr.getBytes("ISO-8859-1"), "UTF-8");
                }
                is_cN = CommonUtils.isChineseCharacter(UTF_Str);
                //防止有人特意使用乱码来生成二维码来判断的情况
                boolean b = CommonUtils.isSpecialCharacter(resultStr);
                if (b) {
                    is_cN = true;
                }
                if (!is_cN) {
                    GB_Str = new String(resultStr.getBytes("ISO-8859-1"), "GB2312");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String result;
            if (is_cN) {
                result = UTF_Str;
            } else {
                result = GB_Str;
            }
            tv_test.setText(result);
        }

    }

    private String utf8Togb2312(String str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '+':
                    sb.append(' ');
                    break;
                case '%':
                    try {
                        sb.append((char) Integer.parseInt(str.substring(i + 1, i + 3), 16));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException();
                    }
                    i += 2;
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        String result = sb.toString();
        String res = null;
        try {
            byte[] inputBytes = result.getBytes("UTF-8");
            res = new String(inputBytes, "GB2312");
        } catch (Exception e) {
        }
        return res;
    }


    private void initView() {
        tv_test = (TextView) findViewById(R.id.tv_test);
        tv_test1 = (TextView) findViewById(R.id.tv_test1);
    }
}
