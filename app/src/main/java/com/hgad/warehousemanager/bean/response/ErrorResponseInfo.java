package com.hgad.warehousemanager.bean.response;

/**
 * Created by Administrator on 2016/8/4.
 */
public class ErrorResponseInfo {

    public String error_code;

    public String error;


    private ErrorResponseInfo() {

    }

    private static class ErrorHolder {

        private static ErrorResponseInfo errorResponseInfo = new ErrorResponseInfo();

    }

    public static ErrorResponseInfo getInstance() {
        return ErrorHolder.errorResponseInfo;
    }
}
