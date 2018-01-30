package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

/**
 * Created by Administrator on 2017/12/25.
 */
public class GroupStateResponse extends BaseResponse {

    /**
     * data : {"closeDate":null,"groupName":"tt","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":7,"groupCode":null,"status":"0","createDate":"2017-12-25"}
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
         * closeDate : null
         * groupName : tt
         * groupUserIds : 1,
         * groupUsers : 管理员,
         * createUser : 管理员
         * id : 7
         * groupCode : null
         * status : 0
         * createDate : 2017-12-25
         */
        private String closeDate;
        private String groupName;
        private String groupUserIds;
        private String groupUsers;
        private String createUser;
        private int id;
        private String groupCode;
        private String status;
        private String createDate;

        public void setCloseDate(String closeDate) {
            this.closeDate = closeDate;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public void setGroupUserIds(String groupUserIds) {
            this.groupUserIds = groupUserIds;
        }

        public void setGroupUsers(String groupUsers) {
            this.groupUsers = groupUsers;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setGroupCode(String groupCode) {
            this.groupCode = groupCode;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCloseDate() {
            return closeDate;
        }

        public String getGroupName() {
            return groupName;
        }

        public String getGroupUserIds() {
            return groupUserIds;
        }

        public String getGroupUsers() {
            return groupUsers;
        }

        public String getCreateUser() {
            return createUser;
        }

        public int getId() {
            return id;
        }

        public String getGroupCode() {
            return groupCode;
        }

        public String getStatus() {
            return status;
        }

        public String getCreateDate() {
            return createDate;
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
