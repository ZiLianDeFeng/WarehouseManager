package com.hgad.warehousemanager.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.ui.fragment.HomeFragment;
import com.hgad.warehousemanager.ui.fragment.UserFragment;
import com.hgad.warehousemanager.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {

    private RadioGroup rg;
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment mCurrentFragment = new Fragment();


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        CommonUtils.verifyStoragePermissions(this);

    }

    @Override
    protected void initData() {
        final HomeFragment homeFragment = new HomeFragment();
        final UserFragment userFragment = new UserFragment();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        addHideShow(homeFragment);
                        break;
//                    case R.id.rb_data_centre:
//                        addHideShow(dataCenterFragment);
//                        break;
//                    case R.id.rb_performance:
//                        addHideShow(performanceFragment);
//                        break;
                    case R.id.rb_user:
                        addHideShow(userFragment);
                        break;
                    default:
                        break;
                }
            }
        });
        ((RadioButton) rg.getChildAt(0)).setChecked(true);

    }

    @Override
    protected void initView() {
        rg = (RadioGroup) findViewById(R.id.rg);

    }


    // 第一次会创建，后续不会销毁，也不会创建
    private void addHideShow(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!fragments.contains(fragment)) {
            fragmentTransaction.add(R.id.fl, fragment);
            fragments.add(fragment);
        } else {
            fragmentTransaction.show(fragment);
        }
        if (fragments != null) {
            fragmentTransaction.hide(mCurrentFragment);
        }
        fragmentTransaction.commit();
        mCurrentFragment = fragment;
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    /**
     * 解决App重启后导致Fragment重叠的问题
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View view) {

    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            BaseApplication.getApplication().exit();
        }

    }
}
