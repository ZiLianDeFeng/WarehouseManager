package com.hgad.warehousemanager.constants;

/**
 * Created by ck on 2016/8/8.
 */
public class HttpConstants {

    public static final String HOST = "http://";

    public static final String REQ = "/tts/api/";

    public static final String PRODUCT = "product/";

    public static final String STATICS = "statics/";

    public static final String POSITION = "position/";

    public static final String SYSTEM = "system/";

    public static final String APK = "/tts";

    public static final String GROUP = "group/";

    public static String ReqFormatUrl(String url, String action) {
        return HOST + url + REQ + action;
    }

    public static String APKFormatUrl(String url) {
        return HOST + url + APK;
    }

}
