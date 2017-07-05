package com.hgad.warehousemanager.base;

import android.app.Activity;
import android.app.Application;

import com.hgad.warehousemanager.util.CrashHandler;

import java.util.LinkedList;
import java.util.List;

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

    private static BaseApplication baseApplication = null;

    public static BaseApplication getApplication() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        CrashHandler.getInstance().init(getApplicationContext());
    }

}
