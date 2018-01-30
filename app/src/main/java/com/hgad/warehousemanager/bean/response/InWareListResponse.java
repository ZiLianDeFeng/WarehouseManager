package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */
public class InWareListResponse extends BaseResponse {

    /**
     * data : {"msg":null,"page":{"pageTotal":0,"start":0,"pageSize":10,"countTotal":1,"end":0,"page":0},"list":[{"sheets":1,"orderNo":"","identification":"Z3Q123456","orderItem":"F3Q123456","specification":"","positionCode":null,"steelGrade":null,"id":3,"proName":"","netWgt":"8550","status":"0"}],"responseCode":null}
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
         * page : {"pageTotal":0,"start":0,"pageSize":10,"countTotal":1,"end":0,"page":0}
         * list : [{"sheets":1,"orderNo":"","identification":"Z3Q123456","orderItem":"F3Q123456","specification":"","positionCode":null,"steelGrade":null,"id":3,"proName":"","netWgt":"8550","status":"0"}]
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
             * countTotal : 1
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
             * sheets : 1
             * orderNo :
             * identification : Z3Q123456
             * orderItem : F3Q123456
             * specification :
             * positionCode : null
             * steelGrade : null
             * id : 3
             * proName :
             * netWgt : 8550
             * status : 0
             */
            private int sheets;
            private String orderNo;
            private String identification;
            private String orderItem;
            private String specification;
            private String positionCode;
            private String steelGrade;
            private int id;
            private String proName;
            private String netWgt;
            private String status;

            public void setSheets(int sheets) {
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

            public int getSheets() {
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
