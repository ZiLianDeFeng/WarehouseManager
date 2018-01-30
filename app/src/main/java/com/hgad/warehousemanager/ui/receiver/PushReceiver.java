package com.hgad.warehousemanager.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hgad.warehousemanager.ui.activity.NotificationActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/3/22.
 */
public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("PushReceiver1", "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d("PushReceiver1", "[MyReceiver] 接收Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d("PushReceiver1", "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d("PushReceiver1", "收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d("PushReceiver1", "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            if (bundle == null) {
                return;
            }
            String message = bundle.getString(JPushInterface.EXTRA_ALERT);
            int extraJson = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d("onReceive", "onReceive: " + extraJson);
            if (message != null) {
//                if (message.contains("维修")) {
//                    Intent i = new Intent(context, PullResultActivity.class);  //自定义打开的界面
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(i);
//                } else if (message.contains("巡检")) {
//                    Intent i = new Intent(context, PullCheckResultActivity.class);  //自定义打开的界面
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(i);
//                }
                Intent i = new Intent(context, NotificationActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.e("PushReceiver1", "[PushReceiver1]" + intent.getAction() + " connected:" + connected);
        } else {
            Log.d("PushReceiver1", "Unhandled intent - " + intent.getAction());
        }
    }
}
