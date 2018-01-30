package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

/**
 * Created by Administrator on 2017/7/20.
 */
public class LoginResponse extends BaseResponse {


    /**
     * data : {"password":"123456","post":"工程师","sex":"1","cellphone":"18956897458","remark":null,"id":1,"username":"admin","realname":"管理员","status":"0"}
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
         * password : 123456
         * post : 工程师
         * sex : 1
         * cellphone : 18956897458
         * remark : null
         * id : 1
         * username : admin
         * realname : 管理员
         * status : 0
         */
        private String password;
        private String post;
        private String sex;
        private String cellphone;
        private String remark;
        private int id;
        private String username;
        private String realname;
        private String status;

        public void setPassword(String password) {
            this.password = password;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPassword() {
            return password;
        }

        public String getPost() {
            return post;
        }

        public String getSex() {
            return sex;
        }

        public String getCellphone() {
            return cellphone;
        }

        public String getRemark() {
            return remark;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getRealname() {
            return realname;
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
