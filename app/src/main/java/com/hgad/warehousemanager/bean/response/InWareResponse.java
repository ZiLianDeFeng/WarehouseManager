package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

/**
 * Created by Administrator on 2017/7/13.
 */
public class InWareResponse extends BaseResponse {

    /**
     * data : {"sheets":null,"orderNo":null,"identification":null,"orderItem":null,"specification":null,"positionCode":null,"steelGrade":null,"id":null,"proName":null,"netWgt":null,"status":null}
     * responseCode : {"code":500,"desc":"服务器错误"}
     * errorMsg : 服务器错误
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
         * sheets : null
         * orderNo : null
         * identification : null
         * orderItem : null
         * specification : null
         * positionCode : null
         * steelGrade : null
         * id : null
         * proName : null
         * netWgt : null
         * status : null
         */
        private String sheets;
        private String orderNo;
        private String identification;
        private String orderItem;
        private String specification;
        private String positionCode;
        private String steelGrade;
        private String id;
        private String proName;
        private String netWgt;
        private String status;

        public void setSheets(String sheets) {
            this.sheets = sheets;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public void setIdentification(String identification) {
            this.identification = identification;
        }

        public void setOrderItem(String orderItem) {
            this.orderItem = orderItem;
        }

        public void setSpecification(String specification) {
            this.specification = specification;
        }

        public void setPositionCode(String positionCode) {
            this.positionCode = positionCode;
        }

        public void setSteelGrade(String steelGrade) {
            this.steelGrade = steelGrade;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setProName(String proName) {
            this.proName = proName;
        }

        public void setNetWgt(String netWgt) {
            this.netWgt = netWgt;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSheets() {
            return sheets;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public String getIdentification() {
            return identification;
        }

        public String getOrderItem() {
            return orderItem;
        }

        public String getSpecification() {
            return specification;
        }

        public String getPositionCode() {
            return positionCode;
        }

        public String getSteelGrade() {
            return steelGrade;
        }

        public String getId() {
            return id;
        }

        public String getProName() {
            return proName;
        }

        public String getNetWgt() {
            return netWgt;
        }

        public String getStatus() {
            return status;
        }
    }

    public class ResponseCodeEntity {
        /**
         * code : 500
         * desc : 服务器错误
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
