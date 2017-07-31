package com.hgad.warehousemanager.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.FastBlurUtils;
import com.hgad.warehousemanager.util.SPUtils;

/**
 * Created by Administrator on 2017/7/12.
 */
public class UserSettingActivity extends BaseActivity {

    private ImageView iv_user_icon;
    private TextView tv_name;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_settting);
    }

    @Override
    protected void initData() {
        initHeader("个人中心");
        String userName = SPUtils.getString(this, SPConstants.USER_NAME);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.touxiang);
        Bitmap roundIcon = FastBlurUtils.toRoundBitmap(icon);
        iv_user_icon.setImageBitmap(roundIcon);
        tv_name.setText(userName);
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_edit).setOnClickListener(this);
        iv_user_icon = (ImageView) findViewById(R.id.iv_user_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        findViewById(R.id.rl_user_guide).setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit:
                break;
            case R.id.rl_user_guide:
                break;
        }
    }
}
