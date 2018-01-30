package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */
public class CheckTaskListResponse extends BaseResponse {


    /**
     * data : [{"total":"5265","already":"3","checkMan":"管理员,TEST","remark":"test","id":28,"title":"01仓盘点","reCheckMan":"管理员,TEST","house":"01","createDate":null,"status":"0"}]
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
         * total : 5265
         * already : 3
         * checkMan : 管理员,TEST
         * remark : test
         * id : 28
         * title : 01仓盘点
         * reCheckMan : 管理员,TEST
         * house : 01
         * createDate : null
         * status : 0
         */
        private String total;
        private String already;
        private String checkMan;
        private String remark;
        private int id;
        private String title;
        private String reCheckMan;
        private String house;
        private String createDate;
        private String status;

        public void setTotal(String total) {
            this.total = total;
        }

        public void setAlready(String already) {
            this.already = already;
        }

        public void setCheckMan(String checkMan) {
            this.checkMan = checkMan;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setReCheckMan(String reCheckMan) {
            this.reCheckMan = reCheckMan;
        }

        public void setHouse(String house) {
            this.house = house;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTotal() {
            return total;
        }

        public String getAlready() {
            return already;
        }

        public String getCheckMan() {
            return checkMan;
        }

        public String getRemark() {
            return remark;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getReCheckMan() {
            return reCheckMan;
        }

        public String getHouse() {
            return house;
        }

        public String getCreateDate() {
            return createDate;
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
