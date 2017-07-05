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
    public static final String IN_WARE = "inWare";
    public static final String CHANGE_WARE = "changeWare";
    public static final String CHECK = "check";
    public static final String ADDRESS = "address";
}
