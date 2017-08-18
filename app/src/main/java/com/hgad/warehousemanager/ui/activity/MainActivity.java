package com.hgad.warehousemanager.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.ui.adapter.DrawerAdapter;
import com.hgad.warehousemanager.ui.fragment.HomeFragment;
import com.hgad.warehousemanager.ui.fragment.UserFragment;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.FastBlurUtils;
import com.hgad.warehousemanager.util.SPUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private Bitmap roundBitmap;
    private Handler handler = new Handler();
    private ImageView iv_user_icon;
    private Uri uri;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        CommonUtils.verifyStoragePermissions(this);
    }

    @Override
    protected void initData() {
        homeFragment = new HomeFragment();
//        userFragment = new UserFragment();
//        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.rb_home:
//                        addHideShow(homeFragment);
//                        break;
////                    case R.id.rb_data_centre:
////                        addHideShow(dataCenterFragment);
////                        break;
////                    case R.id.rb_performance:
////                        addHideShow(performanceFragment);
////                        break;
//                    case R.id.rb_user:
//                        addHideShow(userFragment);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//        ((RadioButton) rg.getChildAt(0)).setChecked(true);
        addHideShow(homeFragment);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.touxiang);
        roundBitmap = FastBlurUtils.toRoundBitmap(bitmap);
        iv_user_icon.setImageBitmap(roundBitmap);
    }

    @Override
    protected void initView() {
//        rg = (RadioGroup) findViewById(R.id.rg);
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
                mCurrentFragment.onPause();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                homeFragment.onPause();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer = false;
                mCurrentFragment.onResume();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        iv_user_icon = (ImageView)findViewById(R.id.iv_user_icon);
        iv_user_icon.setOnClickListener(this);

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        DrawerAdapter drawerAdapter = new DrawerAdapter(this);
        drawerAdapter.setOnItemClickListener(new OnItemClickListener());
        drawerAdapter.setHeadClickListener(new DrawerAdapter.OnHeadClickListener() {
            @Override
            public void headClick() {
                Intent intent = new Intent(MainActivity.this, UserSettingActivity.class);
                startActivity(intent);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }, 500);
            }
        });
        left.setLayoutManager(new LinearLayoutManager(this));
        left.setAdapter(drawerAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                if (resultCode == RESULT_OK) {
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                }
        }
    }

    public class OnItemClickListener implements DrawerAdapter.OnItemClickListener {

        @Override
        public void itemClick(DrawerAdapter.DrawerItemNormal drawerItemNormal) {
            switch (drawerItemNormal.titleRes) {
                case R.string.drawer_menu_camera://相机
                    takePhoto();
                    break;
                case R.string.drawer_menu_gallery://相册
                    break;
                case R.string.drawer_menu_tools://栏目
                    tool();
                    break;
                case R.string.drawer_menu_favorites://搜索
                    favorite();
                    break;
                case R.string.drawer_menu_setting://设置
                    go();
                    break;
                case R.string.drawer_menu_air:
                    weather();
                    break;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }, 1000);
        }
    }

    private void favorite() {
        Intent intent = new Intent(this, FaroriteActivity.class);
        startActivity(intent);
    }

    private void tool() {
        Intent intent = new Intent(this, GeneralToolsActivity.class);
        startActivity(intent);
    }

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int TAKE_PHOTO_REQUEST_CODE = 2;

    private void takePhoto() {
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    TAKE_PHOTO_REQUEST_CODE);
        }
        if (state.equals(Environment.MEDIA_MOUNTED)) {   //如果可用
            try {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date(System.currentTimeMillis());
                String fileName = format.format(date);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName + ".jpg");
                uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
            } catch (Exception e) {

            }
        } else {
            Toast.makeText(MainActivity.this, "sdcard不可用", Toast.LENGTH_SHORT).show();
        }
    }

    private void weather() {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("weatherCode", SPUtils.getString(this, SPConstants.WEATHER_CITY));
        startActivity(intent);
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
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

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
