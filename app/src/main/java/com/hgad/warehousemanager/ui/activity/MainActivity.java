package com.hgad.warehousemanager.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.ui.adapter.DrawerAdapter;
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
    private LinearLayout right;
    private RecyclerView left;
    private boolean isDrawer = false;
    private DrawerLayout drawer;
    private HomeFragment homeFragment;
    private UserFragment userFragment;
    private Handler handler = new Handler();

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        CommonUtils.verifyStoragePermissions(this);
    }

    @Override
    protected void initData() {
        homeFragment = new HomeFragment();
        userFragment = new UserFragment();
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
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        right = (LinearLayout) findViewById(R.id.right);
        left = (RecyclerView) findViewById(R.id.rv_drawer);
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isDrawer) {
                    return left.dispatchTouchEvent(motionEvent);
                } else {
                    return false;
                }
            }
        });
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer = true;
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                right.layout(left.getRight(), 0, left.getRight() + display.getWidth(), display.getHeight());
                homeFragment.onPause();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                homeFragment.onPause();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer = false;
                homeFragment.onResume();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        findViewById(R.id.iv_user_icon).setOnClickListener(this);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        DrawerAdapter drawerAdapter = new DrawerAdapter();
        drawerAdapter.setOnItemClickListener(new OnItemClickListener());
        left.setLayoutManager(new LinearLayoutManager(this));
        left.setAdapter(drawerAdapter);
    }

    public class OnItemClickListener implements DrawerAdapter.OnItemClickListener {

        @Override
        public void itemClick(DrawerAdapter.DrawerItemNormal drawerItemNormal) {
            switch (drawerItemNormal.titleRes) {
                case R.string.drawer_menu_camera://首页
                    break;
                case R.string.drawer_menu_gallery://排行榜
                    break;
                case R.string.drawer_menu_tools://栏目
                    break;
                case R.string.drawer_menu_favorites://搜索
                    break;
                case R.string.drawer_menu_setting://设置
                    go();
                    break;
                case R.string.drawer_menu_air://夜间模式
                    break;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawer.closeDrawer(GravityCompat.START);
                }
            },500);
        }
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitBy2Click();
        }
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
        switch (view.getId()) {
            case R.id.iv_user_icon:
                showDrawer();
                break;
        }
    }

    private void showDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_camera:
//            case R.id.nav_gallery:
//            case R.id.nav_tools:
//            case R.id.nav_favorites:
//            case R.id.nav_setting:
//            case R.id.nav_air:
////                go();
//                break;
//        }
//        return true;
//    }

    private void go() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    /**
     * 菜单、返回键响应
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exitBy2Click();      //调用双击退出函数
//        }
//        return false;
//    }

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
