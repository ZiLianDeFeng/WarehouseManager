package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */
public class UserDetailActivity extends BaseActivity {

    private static final int PHOTO_GALLERY = 1;
    //    private EditText et_username;
//    private EditText et_usersex;
//    private EditText et_birthday;
    private EditText et_email;
    private List<String> photos = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_detail);
    }

    @Override
    protected void initData() {
        initHeader("个人资料");
    }

    @Override
    protected void initView() {
//        et_username = (EditText) findViewById(R.id.et_username);
//        et_usersex = (EditText) findViewById(R.id.et_usersex);
//        et_birthday = (EditText) findViewById(R.id.et_birthday);
        et_email = (EditText) findViewById(R.id.et_email);
        findViewById(R.id.rl_user_icon).setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_GALLERY:
                if (resultCode == PhotoActivity.CHOOSE_IMAGES) {
                    if (data != null) {
                        photos.addAll((List<String>) data.getSerializableExtra(Constants.PHOTOS));
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commitDetail();
                break;
            case R.id.rl_user_icon:
                editIcon();
                break;
        }
    }

    private void editIcon() {
        CommonUtils.verifyStoragePermissions(this);
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivityForResult(intent, PHOTO_GALLERY);
    }

    private void commitDetail() {
        String email = et_email.getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra(Constants.PHOTOS, (Serializable) photos);
        setResult(Constants.EDIT_OK, intent);
        finish();
    }
}
