package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/12/25.
 */
public class GetMemberListResponse extends BaseResponse {

    /**
     * data : [{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"0","cellphone":"15927052148","remark":null,"id":22,"username":"15927052148","realname":"邹江生","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"0","cellphone":"13733420389","remark":null,"id":23,"username":"13733420389","realname":"柳其周","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"0","cellphone":"13971010928","remark":null,"id":24,"username":"13971010928","realname":"肖休明","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"1","cellphone":"13971261692","remark":null,"id":25,"username":"13971261692","realname":"聂赛","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"0","cellphone":"13986273353","remark":null,"id":26,"username":"13986273353","realname":"辛时利","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"1","cellphone":"13971656251","remark":null,"id":27,"username":"13971656251","realname":"张鑫","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"1","cellphone":"13476822063","remark":null,"id":28,"username":"13476822063","realname":"段家香","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"0","cellphone":"15872362872","remark":null,"id":29,"username":"15872362872","realname":"林典刚","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"0","cellphone":"13971009740","remark":null,"id":30,"username":"13971009740","realname":"夏浩杰","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"0","cellphone":"18971428845","remark":null,"id":31,"username":"18971428845","realname":"何光宇","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"0","cellphone":"13986285950","remark":null,"id":32,"username":"13986285950","realname":"吴博","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"1","cellphone":"15527422709","remark":null,"id":34,"username":"15527422709","realname":"杨静静","status":null},{"password":"e10adc3949ba59abbe56e057f20f883e","post":null,"sex":"1","cellphone":"13098856161","remark":null,"id":35,"username":"13098856161","realname":"蔡早红","status":null}]
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
         * password : e10adc3949ba59abbe56e057f20f883e
         * post : null
         * sex : 0
         * cellphone : 15927052148
         * remark : null
         * id : 22
         * username : 15927052148
         * realname : 邹江生
         * status : null
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
