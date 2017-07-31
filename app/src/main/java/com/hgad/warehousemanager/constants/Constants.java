package com.hgad.warehousemanager.constants;

import android.os.Environment;

/**
 * Created by Administrator on 2017/6/26.
 */
public class Constants {
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
    public static final String EDIT_IP ="editIp" ;
}
