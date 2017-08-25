package com.hgad.warehousemanager.base;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.util.CrashHandler;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/6/26.
 */
public class BaseApplication extends Application {
    //存放Activity对象
    private List<Activity> activityList = new LinkedList<Activity>();

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
//            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
//                finishActivity(activity);
                activity.finish();
            }
        }
    }

    private static BaseApplication baseApplication = null;

    public static BaseApplication getApplication() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        CrashHandler.getInstance().init(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.setLatestNotificationNumber(this, 1000);
        initNotification();
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int versionCode = packageInfo.versionCode;
        int oldVersion = SPUtils.getInt(this, SPConstants.VERSION_CODE);
        if (versionCode != oldVersion) {
            SPUtils.put(this, SPConstants.NOT_FRIST, false);
            SPUtils.put(this, SPConstants.VERSION_CODE, versionCode);
        }
    }

    private void initNotification() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.mipmap.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

}
