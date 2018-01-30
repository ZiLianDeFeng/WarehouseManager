package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/10/27.
 */
public class HouseStatisticsResponse extends BaseResponse {

    /**
     * data : [{"code":"01","positionList":null,"name":"仓库一","remark":null,"fixed":9,"id":1,"rows":15,"storey":27,"cols":13,"status":"0"},{"code":"02","positionList":null,"name":"仓库二","remark":null,"fixed":0,"id":2,"rows":15,"storey":27,"cols":16,"status":"0"},{"code":"03","positionList":null,"name":"仓库三","remark":null,"fixed":0,"id":3,"rows":15,"storey":27,"cols":17,"status":"0"},{"code":"04","positionList":null,"name":"仓库四","remark":null,"fixed":0,"id":4,"rows":10,"storey":27,"cols":12,"status":"0"},{"code":"05","positionList":null,"name":"仓库五","remark":null,"fixed":0,"id":5,"rows":8,"storey":27,"cols":17,"status":"0"},{"code":"06","positionList":null,"name":"仓库六","remark":null,"fixed":0,"id":6,"rows":6,"storey":27,"cols":13,"status":"0"}]
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
         * code : 01
         * positionList : null
         * name : 仓库一
         * remark : null
         * fixed : 9
         * id : 1
         * rows : 15
         * storey : 27
         * cols : 13
         * status : 0
         */
        private String code;
        private String positionList;
        private String name;
        private String remark;
        private int fixed;
        private int id;
        private int rows;
        private int storey;
        private int cols;
        private String status;

        public void setCode(String code) {
            this.code = code;
        }

        public void setPositionList(String positionList) {
            this.positionList = positionList;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setFixed(int fixed) {
            this.fixed = fixed;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public void setStorey(int storey) {
            this.storey = storey;
        }

        public void setCols(int cols) {
            this.cols = cols;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public String getPositionList() {
            return positionList;
        }

        public String getName() {
            return name;
        }

        public String getRemark() {
            return remark;
        }

        public int getFixed() {
            return fixed;
        }

        public int getId() {
            return id;
        }

        public int getRows() {
            return rows;
        }

        public int getStorey() {
            return storey;
        }

        public int getCols() {
            return cols;
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
