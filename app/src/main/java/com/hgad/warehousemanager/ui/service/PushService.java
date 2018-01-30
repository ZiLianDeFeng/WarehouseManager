package com.hgad.warehousemanager.ui.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hgad.warehousemanager.ui.receiver.PushReceiver;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/7/26.
 */
public class PushService extends Service {

    private PushReceiver pushReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pushReceiver = new PushReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(JPushInterface.ACTION_NOTIFICATION_RECEIVED);
        registerReceiver(pushReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pushReceiver);
    }
}
