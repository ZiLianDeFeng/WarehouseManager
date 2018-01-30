package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

/**
 * Created by Administrator on 2017/9/19.
 */
public class HomeDataResponse extends BaseResponse {

    /**
     * data : {"in":64,"chk":7,"dayIn":4,"out":30}
     * responseCode : {"code":200,"desc":"请求成功"}
     * errorMsg : 请求成功
     */
    private DataEntity data;
    private ResponseCodeEntity responseCode;
    private String errorMsg;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setResponseCode(ResponseCodeEntity responseCode) {
        this.responseCode = responseCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public DataEntity getData() {
        return data;
    }

    public ResponseCodeEntity getResponseCode() {
        return responseCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public class DataEntity {
        /**
         * in : 64
         * chk : 7
         * dayIn : 4
         * out : 30
         */
        private int in;
        private int chk;
        private int dayIn;
        private int out;

        public void setIn(int in) {
            this.in = in;
        }

        public void setChk(int chk) {
            this.chk = chk;
        }

        public void setDayIn(int dayIn) {
            this.dayIn = dayIn;
        }

        public void setOut(int out) {
            this.out = out;
        }

        public int getIn() {
            return in;
        }

        public int getChk() {
            return chk;
        }

        public int getDayIn() {
            return dayIn;
        }

        public int getOut() {
            return out;
        }
    }

    public class ResponseCodeEntity {
        /**
         * code : 200
         * desc : 请求成功
         */
        private int code;
        private String desc;

        public void setCode(int code) {
            this.code = code;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
