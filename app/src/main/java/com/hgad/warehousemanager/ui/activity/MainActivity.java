package com.hgad.warehousemanager.ui.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.bean.request.CheckVersionRequest;
import com.hgad.warehousemanager.bean.response.CheckVersionResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.DrawerAdapter;
import com.hgad.warehousemanager.ui.fragment.HomeFragment;
import com.hgad.warehousemanager.ui.fragment.UserFragment;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.DownloadUtil;
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
    protected static final int NEED_UPDATE = 0;
    private static final int DOWN_OK = 1; // 下载完成
    private static final int DOWN_ERROR = 2;
    private static final int CODE = 100;
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
    private ImageView iv_user_icon;
    private Uri uri;
    private NotificationManager notificationManager;
    private Notification notification;
    private RemoteViews contentView;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case NEED_UPDATE:
                    // 提醒用户是否更新
                    String url = (String) msg.obj;
//                    String fileUrl = getFileName(url);
                    CommonUtils.verifyStoragePermissions(MainActivity.this);
                    showUpdateDialog(url);
                    break;
                case DOWN_OK:
                    // 下载完成，点击安装
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                    notification.setLatestEventInfo(FragmentTabActivity.this, getString(R.string.app_name), "下载完成", pendingIntent);
                    notification.contentView.setTextViewText(R.id.notificationTitle, "下载完成");
                    notificationManager.notify(notification_id, notification);
                    notificationManager.cancel(notification_id);
                    installApk(destFile);
//                    stopService(updateIntent);
                    break;
                case DOWN_ERROR:
//                    notification.setLatestEventInfo(FragmentTabActivity.this, getString(R.string.app_name), "下载失败", pendingIntent);
                    notification.contentView.setTextViewText(R.id.notificationTitle, "下载失败");
                    break;
                default:
//                    stopService(updateIntent);
                    break;
            }
        }

        ;
    };
    private File destFile;
    private String ip;
    private int curPro;

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
        ip = SPUtils.getString(this, SPConstants.IP);
        checkVersion();
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
        iv_user_icon = (ImageView) findViewById(R.id.iv_user_icon);
        iv_user_icon.setOnClickListener(this);

        DrawerAdapter drawerAdapter = new DrawerAdapter(this);
        drawerAdapter.setOnItemClickListener(new OnItemClickListener());
        drawerAdapter.setHeadClickListener(new DrawerAdapter.OnHeadClickListener() {
            @Override
            public void headClick() {
//                Intent intent = new Intent(MainActivity.this, UserSettingActivity.class);
//                startActivity(intent);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        drawer.closeDrawer(GravityCompat.START);
//                    }
//                }, 500);
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
        if (request instanceof CheckVersionRequest) {
            CheckVersionResponse checkVersionResponse = (CheckVersionResponse) response;
            if (checkVersionResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(checkVersionResponse.getErrorMsg())) {
                    String url = checkVersionResponse.getData();
                    if (url != null) {
                        String version = url.substring(url.lastIndexOf("v") + 1, url.lastIndexOf(".apk"));
                        String versionName = getVersionName();
                        if (!version.equalsIgnoreCase(versionName)) {
                            Message message = handler.obtainMessage();
                            message.obj = url;
                            message.what = NEED_UPDATE;
                            handler.sendMessage(message);
                        }
                    }
                }
            }
        }
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


    private void go() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private int notification_id = 1;

    public void createNotification() {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon = R.mipmap.ic_launcher;
//        // 这个参数是通知提示闪出来的值.
        notification.tickerText = "开始下载";
        // pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

        // 这里面的参数是通知栏view显示的内容
//        notification.setLatestEventInfo(this, R.string.app_name, "下载：0%", pendingIntent);
        // notificationManager.notify(notification_id, notification);

        /***
         * 在这里我们用自定的view来显示Notification
         */
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        notification.contentView = contentView;
        notification.contentView.setTextViewText(R.id.notificationTitle, "正在下载");
        notification.contentView.setTextViewText(R.id.notificationPercent, "0%");
        notification.contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

//        updateIntent = new Intent(this,LoginActivity.class);
//        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

//        notification.contentIntent = pendingIntent;
        notificationManager.notify(notification_id, notification);
    }


    private void checkVersion() {
        String versionName = getVersionName();
        CheckVersionRequest checkVersionRequest = new CheckVersionRequest("v" + versionName);
        sendRequest(checkVersionRequest, CheckVersionResponse.class);
//        String url = "";
//        String version = "1.0.0";
//        if (!version.equalsIgnoreCase(versionName)) {
//            Message message = handler.obtainMessage();
//            message.obj = url;
//            message.what = NEED_UPDATE;
//            handler.sendMessage(message);
//        }
    }

    /**
     * 获取版本号
     *
     * @return
     */
    private String getVersionName() {
        // PackageManager
        PackageManager pm = getPackageManager();
        // PackageInfo
        try {
            // flags 代表可以获取的包信息的内容 传0即可 因为任何Flag都可以获取版本号
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
        }
        return null;
    }

    private void showUpdateDialog(final String url) {
        final String fileName = getFileName(url);
        new AlertView("更新信息", "已发布最新版本，建议立即更新使用", "暂不更新", new String[]{"开始更新"}, null, this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        // SD卡  内部存储
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            //SD卡存在
                            destFile = new File(Environment.getExternalStorageDirectory(), fileName);
                            int i = 1;
                            while (destFile.exists()) {
                                destFile = new File(Environment.getExternalStorageDirectory(), "(" + i + ")" + fileName);
                                i++;
                            }
                        } else {
                            //SD卡不存在
                            destFile = new File(getFilesDir(), fileName);
                            int i = 1;
                            while (destFile.exists()) {
                                destFile = new File(getFilesDir(), "(" + i + ")" + fileName);
                                i++;
                            }
                        }
//                        Random random = new Random();
//                        int nextInt = random.nextInt();
//                        nextInt = Math.abs(nextInt);
//                        // SD卡  内部存储
//                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                            //SD卡存在
//                            destFile = new File(Environment.getExternalStorageDirectory(), nextInt + fileName);
//                        } else {
//                            //SD卡不存在
//                            destFile = new File(getFilesDir(), nextInt + fileName);
//                        }
                        downLoadAPk(url, destFile);
                        break;
                }
            }
        }).setCancelable(false).show();
    }

    Runnable run = new Runnable() {

        @Override
        public void run() {
            if (curPro != 100) {
                notificationManager.notify(notification_id, notification);//这里是更新notification,就是更新进度条
                handler.postDelayed(run, 200);
            }
        }
    };

    private void downLoadAPk(final String url, final File destFile) {
        createNotification();
        handler.post(run);
        String path = HttpConstants.APKFormatUrl(ip) + url;
        path = path.replace("\\", "/");
        String destFilePath = destFile.getPath();
        DownloadUtil.get().download(path, destFilePath, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                Message message = handler.obtainMessage();
                message.what = DOWN_OK;
                handler.sendMessage(message);
                Log.e("down", "onDownloadSuccess: ");
            }

            @Override
            public void onDownloading(final int progress) {
                curPro = progress;
//                double x_double = progress * 1.0;
//                double tempresult = x_double / total;
//                DecimalFormat df1 = new DecimalFormat("0.00"); // ##.00%
//                // 百分比格式，后面不足2位的用0补齐
//                String result = df1.format(tempresult);
                notification.contentView.setTextViewText(R.id.notificationPercent, progress + "%");
                notification.contentView.setProgressBar(R.id.notificationProgress, 100, progress, false);
                Log.e("down", "onDownloading: " + progress);
            }

            @Override
            public void onDownloadFailed() {
                Message message = handler.obtainMessage();
                message.what = DOWN_ERROR;
                handler.sendMessage(message);
                Log.e("down", "onDownloadFailed: ");
            }
        });
    }

    /**
     * 安装apk的方法
     *
     * @param apkFile
     */
    public void installApk(File apkFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //Uri.parse("file://"+apkFile.getAbsolutePath()) 以前的写法
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        //启动系统中的安装界面Activity  获取结果
        startActivityForResult(intent, 250);
    }

    /**
     * 根据URL切割文件名
     *
     * @param url
     * @return
     */
    protected String getFileName(String url) {
        return url.substring(url.lastIndexOf("\\") + 1);
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
