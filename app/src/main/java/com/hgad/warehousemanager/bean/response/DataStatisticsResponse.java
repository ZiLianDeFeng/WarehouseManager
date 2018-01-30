package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/10/27.
 */
public class DataStatisticsResponse extends BaseResponse {


    /**
     * data : [{"operDate":null,"total":3.236666666666667,"mvType":null,"id":null,"inType":null,"outType":null,"operator":"辛时利"},{"operDate":null,"total":3.236666666666667,"mvType":null,"id":null,"inType":null,"outType":null,"operator":"段家香"},{"operDate":null,"total":3.236666666666667,"mvType":null,"id":null,"inType":null,"outType":null,"operator":"管理员"}]
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
         * operDate : null
         * total : 3.236666666666667
         * mvType : null
         * id : null
         * inType : null
         * outType : null
         * operator : 辛时利
         */
        private String operDate;
        private double total;
        private String mvType;
        private String id;
        private String inType;
        private String outType;
        private String operator;

        public void setOperDate(String operDate) {
            this.operDate = operDate;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public void setMvType(String mvType) {
            this.mvType = mvType;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setInType(String inType) {
            this.inType = inType;
        }

        public void setOutType(String outType) {
            this.outType = outType;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getOperDate() {
            return operDate;
        }

        public double getTotal() {
            return total;
        }

        public String getMvType() {
            return mvType;
        }

        public String getId() {
            return id;
        }

        public String getInType() {
            return inType;
        }

        public String getOutType() {
            return outType;
        }

        public String getOperator() {
            return operator;
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
