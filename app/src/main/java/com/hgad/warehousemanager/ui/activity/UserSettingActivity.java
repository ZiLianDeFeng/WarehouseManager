package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.FastBlurUtils;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */
public class UserSettingActivity extends BaseActivity {

    private static final int REFRESH_DETAIL = 201;
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
        findViewById(R.id.ll_user_detail).setOnClickListener(this);
        iv_user_icon = (ImageView) findViewById(R.id.iv_user_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        findViewById(R.id.rl_user_guide).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REFRESH_DETAIL && resultCode == Constants.EDIT_OK) {
            CommonUtils.showToast(this, "修改完成");
            if (data != null) {
                List<String> photos = (List<String>) data.getSerializableExtra(Constants.PHOTOS);
                String path = photos.get(0);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                Bitmap roundBitmap = FastBlurUtils.toRoundBitmap(bitmap);
                iv_user_icon.setImageBitmap(roundBitmap);
            }
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_detail:
                go2EditDetail();
                break;
            case R.id.rl_user_guide:
                break;
        }
    }

    private void go2EditDetail() {
        Intent intent = new Intent(this, UserDetailActivity.class);
        startActivityForResult(intent, REFRESH_DETAIL);
    }
}
