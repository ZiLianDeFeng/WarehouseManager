package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */
public class GroupListResponse extends BaseResponse {

    /**
     * data : {"msg":null,"page":{"pageTotal":0,"start":0,"pageSize":1000,"countTotal":15,"end":0,"page":0},"list":[{"closeDate":null,"groupName":"班组","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":18,"groupCode":null,"status":"0","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"办下来","groupUserIds":"1,27,28,","groupUsers":"管理员,张鑫,段家香,","createUser":"管理员","id":17,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"保证","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":16,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组大","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":15,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":14,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组","groupUserIds":"1,27,32,22,","groupUsers":"管理员,张鑫,吴博,邹江生,","createUser":"管理员","id":13,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":null,"groupName":"班组三","groupUserIds":"23,24,","groupUsers":"柳其周,肖休明,","createUser":"管理员","id":12,"groupCode":null,"status":"0","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组二","groupUserIds":"1,25,26,27,","groupUsers":"管理员,聂赛,辛时利,张鑫,","createUser":"管理员","id":11,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组一","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":10,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组一","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":9,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"出库班组","groupUserIds":"1,24,23,25,","groupUsers":"管理员,肖休明,柳其周,聂赛,","createUser":"管理员","id":8,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"tt","groupUserIds":"1,23,27,24,25,","groupUsers":"管理员,柳其周,张鑫,肖休明,聂赛,","createUser":"管理员","id":7,"groupCode":null,"status":"1","createDate":"2017-12-25"},{"closeDate":null,"groupName":"班组test","groupUserIds":null,"groupUsers":null,"createUser":"管理员","id":6,"groupCode":null,"status":"0","createDate":"2017-12-25"},{"closeDate":null,"groupName":"管理员","groupUserIds":" ","groupUsers":" ","createUser":null,"id":5,"groupCode":"super","status":"0","createDate":null},{"closeDate":null,"groupName":"测试组","groupUserIds":"19,1","groupUsers":"管理员,TEST","createUser":null,"id":4,"groupCode":"TEST","status":"0","createDate":null}],"responseCode":null}
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
         * msg : null
         * page : {"pageTotal":0,"start":0,"pageSize":1000,"countTotal":15,"end":0,"page":0}
         * list : [{"closeDate":null,"groupName":"班组","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":18,"groupCode":null,"status":"0","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"办下来","groupUserIds":"1,27,28,","groupUsers":"管理员,张鑫,段家香,","createUser":"管理员","id":17,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"保证","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":16,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组大","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":15,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":14,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组","groupUserIds":"1,27,32,22,","groupUsers":"管理员,张鑫,吴博,邹江生,","createUser":"管理员","id":13,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":null,"groupName":"班组三","groupUserIds":"23,24,","groupUsers":"柳其周,肖休明,","createUser":"管理员","id":12,"groupCode":null,"status":"0","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组二","groupUserIds":"1,25,26,27,","groupUsers":"管理员,聂赛,辛时利,张鑫,","createUser":"管理员","id":11,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组一","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":10,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"班组一","groupUserIds":"1,","groupUsers":"管理员,","createUser":"管理员","id":9,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"出库班组","groupUserIds":"1,24,23,25,","groupUsers":"管理员,肖休明,柳其周,聂赛,","createUser":"管理员","id":8,"groupCode":null,"status":"1","createDate":"2017-12-26"},{"closeDate":"2017-12-26","groupName":"tt","groupUserIds":"1,23,27,24,25,","groupUsers":"管理员,柳其周,张鑫,肖休明,聂赛,","createUser":"管理员","id":7,"groupCode":null,"status":"1","createDate":"2017-12-25"},{"closeDate":null,"groupName":"班组test","groupUserIds":null,"groupUsers":null,"createUser":"管理员","id":6,"groupCode":null,"status":"0","createDate":"2017-12-25"},{"closeDate":null,"groupName":"管理员","groupUserIds":" ","groupUsers":" ","createUser":null,"id":5,"groupCode":"super","status":"0","createDate":null},{"closeDate":null,"groupName":"测试组","groupUserIds":"19,1","groupUsers":"管理员,TEST","createUser":null,"id":4,"groupCode":"TEST","status":"0","createDate":null}]
         * responseCode : null
         */
        private String msg;
        private PageEntity page;
        private List<ListEntity> list;
        private String responseCode;

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public void setPage(PageEntity page) {
            this.page = page;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }

        public String getMsg() {
            return msg;
        }

        public PageEntity getPage() {
            return page;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public class PageEntity {
            /**
             * pageTotal : 0
             * start : 0
             * pageSize : 1000
             * countTotal : 15
             * end : 0
             * page : 0
             */
            private int pageTotal;
            private int start;
            private int pageSize;
            private int countTotal;
            private int end;
            private int page;

            public void setPageTotal(int pageTotal) {
                this.pageTotal = pageTotal;
            }

            public void setStart(int start) {
                this.start = start;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public void setCountTotal(int countTotal) {
                this.countTotal = countTotal;
            }

            public void setEnd(int end) {
                this.end = end;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getPageTotal() {
                return pageTotal;
            }

            public int getStart() {
                return start;
            }

            public int getPageSize() {
                return pageSize;
            }

            public int getCountTotal() {
                return countTotal;
            }

            public int getEnd() {
                return end;
            }

            public int getPage() {
                return page;
            }
        }

        public class ListEntity {
            /**
             * closeDate : null
             * groupName : 班组
             * groupUserIds : 1,
             * groupUsers : 管理员,
             * createUser : 管理员
             * id : 18
             * groupCode : null
             * status : 0
             * createDate : 2017-12-26
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
