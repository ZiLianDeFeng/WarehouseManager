package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */
public class CheckFloorDetailResponse extends BaseResponse {


    /**
     * data : [{"operDate":"2018-01-02 15:43:16","dbIdentification":null,"orderId":38,"houseCode":"01","positionCode":"01010101","realIdenification":null,"remark":null,"id":47816,"type":null,"status":null}]
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
         * operDate : 2018-01-02 15:43:16
         * dbIdentification : null
         * orderId : 38
         * houseCode : 01
         * positionCode : 01010101
         * realIdenification : null
         * remark : null
         * id : 47816
         * type : null
         * status : null
         */
        private String operDate;
        private String dbIdentification;
        private int orderId;
        private String houseCode;
        private String positionCode;
        private String realIdenification;
        private String remark;
        private int id;
        private String type;
        private String status;

        public void setOperDate(String operDate) {
            this.operDate = operDate;
        }

        public void setDbIdentification(String dbIdentification) {
            this.dbIdentification = dbIdentification;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public void setHouseCode(String houseCode) {
            this.houseCode = houseCode;
        }

        public void setPositionCode(String positionCode) {
            this.positionCode = positionCode;
        }

        public void setRealIdenification(String realIdenification) {
            this.realIdenification = realIdenification;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOperDate() {
            return operDate;
        }

        public String getDbIdentification() {
            return dbIdentification;
        }

        public int getOrderId() {
            return orderId;
        }

        public String getHouseCode() {
            return houseCode;
        }

        public String getPositionCode() {
            return positionCode;
        }

        public String getRealIdenification() {
            return realIdenification;
        }

        public String getRemark() {
            return remark;
        }

        public int getId() {
            return id;
        }

        public String getType() {
            return type;
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
