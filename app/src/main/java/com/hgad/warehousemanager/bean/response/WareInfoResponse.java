package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

/**
 * Created by Administrator on 2017/7/25.
 */
public class WareInfoResponse extends BaseResponse {


    /**
     * data : {"orderNo":null,"orderItem":null,"specification":null,"positionCode":"01010123","remark":null,"type":"0","pieces":null,"sheets":1,"identification":"Z3Q7142141","customerId":null,"steelGrade":null,"id":22211,"proName":null,"netWgt":null,"status":"0"}
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
         * orderNo : null
         * orderItem : null
         * specification : null
         * positionCode : 01010123
         * remark : null
         * type : 0
         * pieces : null
         * sheets : 1
         * identification : Z3Q7142141
         * customerId : null
         * steelGrade : null
         * id : 22211
         * proName : null
         * netWgt : null
         * status : 0
         */
        private String orderNo;
        private String orderItem;
        private String specification;
        private String positionCode;
        private String remark;
        private String type;
        private String pieces;
        private int sheets;
        private String identification;
        private String customerId;
        private String steelGrade;
        private int id;
        private String proName;
        private String netWgt;
        private String status;

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
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

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setPieces(String pieces) {
            this.pieces = pieces;
        }

        public void setSheets(int sheets) {
            this.sheets = sheets;
        }

        public void setIdentification(String identification) {
            this.identification = identification;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public void setSteelGrade(String steelGrade) {
            this.steelGrade = steelGrade;
        }

        public void setId(int id) {
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

        public String getOrderNo() {
            return orderNo;
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

        public String getRemark() {
            return remark;
        }

        public String getType() {
            return type;
        }

        public String getPieces() {
            return pieces;
        }

        public int getSheets() {
            return sheets;
        }

        public String getIdentification() {
            return identification;
        }

        public String getCustomerId() {
            return customerId;
        }

        public String getSteelGrade() {
            return steelGrade;
        }

        public int getId() {
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
