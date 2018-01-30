package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/7/21.
 */
public class TaskResponse extends BaseResponse {

    /**
     * data : {"msg":null,"page":{"pageTotal":0,"start":0,"pageSize":10,"countTotal":2,"end":0,"page":0},"list":[{"orderNo":"000001","userIds":"18,4,1","userNames":"test,测试2,管理员","startTime":1500480000000,"remark":"TTTTTTTTTTT","finished":0,"proCount":1,"id":23,"endTime":null,"title":"TEST","type":"0","status":"2"},{"orderNo":"000001","userIds":"1,5,6","userNames":"管理员,测试3,测试4","startTime":1500480000000,"remark":"AAAAAAAACCC","finished":1,"proCount":1,"id":24,"endTime":null,"title":"TTTTTT","type":"0","status":"3"}],"responseCode":null}
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
         * page : {"pageTotal":0,"start":0,"pageSize":10,"countTotal":2,"end":0,"page":0}
         * list : [{"orderNo":"000001","userIds":"18,4,1","userNames":"test,测试2,管理员","startTime":1500480000000,"remark":"TTTTTTTTTTT","finished":0,"proCount":1,"id":23,"endTime":null,"title":"TEST","type":"0","status":"2"},{"orderNo":"000001","userIds":"1,5,6","userNames":"管理员,测试3,测试4","startTime":1500480000000,"remark":"AAAAAAAACCC","finished":1,"proCount":1,"id":24,"endTime":null,"title":"TTTTTT","type":"0","status":"3"}]
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
             * pageSize : 10
             * countTotal : 2
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
             * orderNo : 000001
             * userIds : 18,4,1
             * userNames : test,测试2,管理员
             * startTime : 1500480000000
             * remark : TTTTTTTTTTT
             * finished : 0
             * proCount : 1
             * id : 23
             * endTime : null
             * title : TEST
             * type : 0
             * status : 2
             */
            private String orderNo;
            private String userIds;
            private String userNames;
            private String startTime;
            private String remark;
            private int finished;
            private int proCount;
            private int id;
            private String endTime;
            private String title;
            private String type;
            private String status;

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public void setUserIds(String userIds) {
                this.userIds = userIds;
            }

            public void setUserNames(String userNames) {
                this.userNames = userNames;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public void setFinished(int finished) {
                this.finished = finished;
            }

            public void setProCount(int proCount) {
                this.proCount = proCount;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public String getUserIds() {
                return userIds;
            }

            public String getUserNames() {
                return userNames;
            }

            public String getStartTime() {
                return startTime;
            }

            public String getRemark() {
                return remark;
            }

            public int getFinished() {
                return finished;
            }

            public int getProCount() {
                return proCount;
            }

            public int getId() {
                return id;
            }

            public String getEndTime() {
                return endTime;
            }

            public String getTitle() {
                return title;
            }

            public String getType() {
                return type;
            }

            public String getStatus() {
                return status;
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
