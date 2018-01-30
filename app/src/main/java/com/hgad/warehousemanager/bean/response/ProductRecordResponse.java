package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */
public class ProductRecordResponse extends BaseResponse {

    /**
     * data : [{"operDate":"2017-11-13 16:56:18","curPosition":"01010102","identification":"G287036128","origPosition":null,"remark":null,"id":27711,"terminal":"初始导入","operType":"0","operator":"admin                                                                                               ","status":"1"}]
     * responseCode : {"code":200,"desc":"请求成功"}
     * errorMsg : 请求成功
     */
    private List<DataEntity> data;
    private ResponseCodeEntity responseCode;
    private String errorMsg;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setResponseCode(ResponseCodeEntity responseCode) {
        this.responseCode = responseCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<DataEntity> getData() {
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
         * operDate : 2017-11-13 16:56:18
         * curPosition : 01010102
         * identification : G287036128
         * origPosition : null
         * remark : null
         * id : 27711
         * terminal : 初始导入
         * operType : 0
         * operator : admin
         * status : 1
         */
        private String operDate;
        private String curPosition;
        private String identification;
        private String origPosition;
        private String remark;
        private int id;
        private String terminal;
        private String operType;
        private String operator;
        private String status;

        public void setOperDate(String operDate) {
            this.operDate = operDate;
        }

        public void setCurPosition(String curPosition) {
            this.curPosition = curPosition;
        }

        public void setIdentification(String identification) {
            this.identification = identification;
        }

        public void setOrigPosition(String origPosition) {
            this.origPosition = origPosition;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }

        public void setOperType(String operType) {
            this.operType = operType;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOperDate() {
            return operDate;
        }

        public String getCurPosition() {
            return curPosition;
        }

        public String getIdentification() {
            return identification;
        }

        public String getOrigPosition() {
            return origPosition;
        }

        public String getRemark() {
            return remark;
        }

        public int getId() {
            return id;
        }

        public String getTerminal() {
            return terminal;
        }

        public String getOperType() {
            return operType;
        }

        public String getOperator() {
            return operator;
        }

        public String getStatus() {
            return status;
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
