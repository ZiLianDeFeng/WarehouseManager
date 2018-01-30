package com.hgad.warehousemanager.constants;

import android.os.Environment;

/**
 * Created by Administrator on 2017/6/26.
 */
public class Constants {

    //测试状态
    public static final boolean DEBUG = false;
    public static final boolean NORMAL = false;
    public static final String MODEL = android.os.Build.MANUFACTURER + "-" + android.os.Build.BRAND + "-" + android.os.Build.MODEL;

    private static final String ABSOLUTE_BASE_PATH_PREFIX = Environment.getExternalStorageDirectory().getPath()
            + "/warehouse/";
    public static final String ABSOLUTE_LOG_PATH_PREFIX = ABSOLUTE_BASE_PATH_PREFIX + "Log/";
    public static final String SCAN_RESULT = "scanResult";
    public static final String TYPE = "type";
    public static final String IN_WARE = "0";
    public static final String CHANGE_WARE = "changeWare";
    public static final String CHECK = "check";
    public static final String ADDRESS = "address";
    public static final String OUT_WARE = "1";
    public static final String COMMIT_RESULT = "commitResult";
    public static final String CHECK_RECORD = "checkRecord";
    public static final String ORDER_NUMBER = "orderNumber";
    public static final String CHOOSE = "choose";
    public static final String CURRENT = "current";
    public static final String WARE_INFO = "wareInfo";
    public static final int NOTIFY = 100;
    public static final String IN_TYPE = "0";
    public static final String OUT_TYPE = "1";
    public static final String ORDER_ID = "orderId";
    public static final String IS_LAST = "isLast";
    public static final int RESULT_OK = 101;
    public static final String LIST_DATA = "listData";
    public static final String ORDER_INFO = "orderInfo";
    public static final String MESSAGE_INFO = "messageInfo";
    public static final String IP_INFO = "ipInfo";
    public static final String EDIT_IP = "editIp";
    public static final String REVIEW_TYPE = "2";
    public static final String REFRESH = "refresh";
    public static final String HISTORY_TYPE = "history";
    public static final String ORDER_REFRESH = "orderRefresh";
    public static final String HANDWRITE_CACHE = "/handwriteCache/";
    public static final String WARE_NUMBER = "wareNumber";
    public static final int HAND = 120;
    public static final int EDIT_OK = 210;
    public static final String PHOTOS = "photos";
    public static final String MARK_NUM = "markNum";
    public static final String ORDER_ITEM = "orderItem";
    public static final String PRO_NAME = "proName";
    public static final String NODE = "node";
    public static final String CHECK_INFO = "checkInfo";
    public static final String RESULT = "result";
    public static final String NEXT = "next";
    public static final String REQUEST_SUCCESS = "请求成功";
    public static final long VIBRATE_DURATION = 200L;
    public static final String QR = "qr";
    public static final String CODE = "code";
    public static final String CODE_TYPE = "codeType";
    public static final String TASK_ID = "taskId";
    public static final String PLATES = "plates";
    public static final String NO_PLATES = "noPlates";
    public static final String SCAN_STATE = "scanState";
    public static final String COUNT = "pieces";
    public static final String OVER = "over";
    public static final String PRO_TYPE = "proType";
    public static final String POSITION_CODE = "positionCode";
    public static final String OCCUPIED = "当前仓位已被占用，请重新选择仓位";
    public static final String HAS_ADD = "hasAdd";
    public static final String DATA = "data";
    public static final String MEMBER = "member";
    public static final String ADD_TYPE = "addType";
    public static final String HEADER = "header";
    public static final String GROUP_ID = "groupId";
}
