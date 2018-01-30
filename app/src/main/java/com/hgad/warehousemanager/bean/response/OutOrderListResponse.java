package com.hgad.warehousemanager.bean.response;

import com.hgad.warehousemanager.net.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */
public class OutOrderListResponse extends BaseResponse {


    /**
     * data : {"msg":null,"page":{"pageTotal":3,"start":0,"pageSize":10,"countTotal":30,"end":10,"page":1},"list":[{"orderType":"0","orderNo":"201711020003","already":null,"validDate":"2017-11-02","createPerson":"管理员","weight":"1.452","remark":"","takePerson":null,"customerName":"北京宝钢北方贸易有限公司","total":null,"price":"0","userIds":null,"customerId":2,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201711020003","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":191,"secondCustomerName":"test","processPerson":null,"productType":"1","createDate":"2017-11-02","status":"1","carIds":null},{"orderType":"0","orderNo":"201711020002","already":null,"validDate":"2017-11-02","createPerson":"管理员","weight":"1.452","remark":"","takePerson":null,"customerName":"北京宝钢北方贸易有限公司","total":null,"price":"0","userIds":null,"customerId":2,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201711020002","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":190,"secondCustomerName":"tet","processPerson":null,"productType":"1","createDate":"2017-11-02","status":"1","carIds":null},{"orderType":"0","orderNo":"201710310013","already":null,"validDate":"2017-10-31","createPerson":"管理员","weight":null,"remark":"","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"0","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710310013","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":189,"secondCustomerName":"fdfd","processPerson":null,"productType":"0","createDate":"2017-10-31","status":"0","carIds":null},{"orderType":"0","orderNo":"201710310002","already":null,"validDate":"2017-10-31","createPerson":"管理员","weight":null,"remark":"","takePerson":null,"customerName":"北京宝钢北方贸易有限公司","total":null,"price":"0","userIds":null,"customerId":2,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710310002","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":188,"secondCustomerName":"TEST","processPerson":null,"productType":null,"createDate":"2017-10-31","status":"0","carIds":null},{"orderType":"0","orderNo":"201710300032","already":null,"validDate":"2017-11-15","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"30","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300032","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":187,"secondCustomerName":"武汉市晓俊物资有限公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"1","carIds":null},{"orderType":"0","orderNo":"201710300031","already":null,"validDate":"2017-11-15","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"30","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300031","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":186,"secondCustomerName":"武汉市晓俊物资有限公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"2","carIds":null},{"orderType":"0","orderNo":"201710300030","already":null,"validDate":"2017-11-15","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"26","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300030","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":185,"secondCustomerName":"武汉浦森电气有限公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"1","carIds":null},{"orderType":"0","orderNo":"201710300029","already":null,"validDate":"2017-11-08","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"30","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300029","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":184,"secondCustomerName":"武汉钢铁有限公司武汉销售分公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"1","carIds":null},{"orderType":"0","orderNo":"201710300028","already":null,"validDate":"2017-11-15","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"30","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300028","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":183,"secondCustomerName":"武汉嘉铁贸易有限公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"2","carIds":null},{"orderType":"0","orderNo":"201710300027","already":null,"validDate":"2017-11-15","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"天津武钢华北销售有限公司","total":null,"price":"26","userIds":null,"customerId":9,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300027","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":182,"secondCustomerName":"武汉中大恒通投资有限公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"1","carIds":null}],"responseCode":null}
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
         * page : {"pageTotal":3,"start":0,"pageSize":10,"countTotal":30,"end":10,"page":1}
         * list : [{"orderType":"0","orderNo":"201711020003","already":null,"validDate":"2017-11-02","createPerson":"管理员","weight":"1.452","remark":"","takePerson":null,"customerName":"北京宝钢北方贸易有限公司","total":null,"price":"0","userIds":null,"customerId":2,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201711020003","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":191,"secondCustomerName":"test","processPerson":null,"productType":"1","createDate":"2017-11-02","status":"1","carIds":null},{"orderType":"0","orderNo":"201711020002","already":null,"validDate":"2017-11-02","createPerson":"管理员","weight":"1.452","remark":"","takePerson":null,"customerName":"北京宝钢北方贸易有限公司","total":null,"price":"0","userIds":null,"customerId":2,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201711020002","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":190,"secondCustomerName":"tet","processPerson":null,"productType":"1","createDate":"2017-11-02","status":"1","carIds":null},{"orderType":"0","orderNo":"201710310013","already":null,"validDate":"2017-10-31","createPerson":"管理员","weight":null,"remark":"","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"0","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710310013","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":189,"secondCustomerName":"fdfd","processPerson":null,"productType":"0","createDate":"2017-10-31","status":"0","carIds":null},{"orderType":"0","orderNo":"201710310002","already":null,"validDate":"2017-10-31","createPerson":"管理员","weight":null,"remark":"","takePerson":null,"customerName":"北京宝钢北方贸易有限公司","total":null,"price":"0","userIds":null,"customerId":2,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710310002","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":188,"secondCustomerName":"TEST","processPerson":null,"productType":null,"createDate":"2017-10-31","status":"0","carIds":null},{"orderType":"0","orderNo":"201710300032","already":null,"validDate":"2017-11-15","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"30","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300032","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":187,"secondCustomerName":"武汉市晓俊物资有限公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"1","carIds":null},{"orderType":"0","orderNo":"201710300031","already":null,"validDate":"2017-11-15","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"30","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300031","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":186,"secondCustomerName":"武汉市晓俊物资有限公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"2","carIds":null},{"orderType":"0","orderNo":"201710300030","already":null,"validDate":"2017-11-15","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"26","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300030","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":185,"secondCustomerName":"武汉浦森电气有限公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"1","carIds":null},{"orderType":"0","orderNo":"201710300029","already":null,"validDate":"2017-11-08","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"30","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300029","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":184,"secondCustomerName":"武汉钢铁有限公司武汉销售分公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"1","carIds":null},{"orderType":"0","orderNo":"201710300028","already":null,"validDate":"2017-11-15","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"武汉钢铁有限公司武汉销售分公司","total":null,"price":"30","userIds":null,"customerId":3,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300028","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":183,"secondCustomerName":"武汉嘉铁贸易有限公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"2","carIds":null},{"orderType":"0","orderNo":"201710300027","already":null,"validDate":"2017-11-15","createPerson":"管理员","weight":null,"remark":"出库","takePerson":null,"customerName":"天津武钢华北销售有限公司","total":null,"price":"26","userIds":null,"customerId":9,"userNames":null,"productOutList":[{"batchNo":null,"orderNo":"201710300027","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}],"id":182,"secondCustomerName":"武汉中大恒通投资有限公司","processPerson":null,"productType":null,"createDate":"2017-10-30","status":"1","carIds":null}]
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
             * pageTotal : 3
             * start : 0
             * pageSize : 10
             * countTotal : 30
             * end : 10
             * page : 1
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
             * orderType : 0
             * orderNo : 201711020003
             * already : null
             * validDate : 2017-11-02
             * createPerson : 管理员
             * weight : 1.452
             * remark :
             * takePerson : null
             * customerName : 北京宝钢北方贸易有限公司
             * total : null
             * price : 0
             * userIds : null
             * customerId : 2
             * userNames : null
             * productOutList : [{"batchNo":null,"orderNo":"201711020003","orderItem":null,"specification":null,"positionCode":null,"pieces":0,"sheets":null,"identification":null,"steelGrade":null,"outPlateNumber":null,"id":null,"proName":null,"netWgt":null,"status":null}]
             * id : 191
             * secondCustomerName : test
             * processPerson : null
             * productType : 1
             * createDate : 2017-11-02
             * status : 1
             * carIds : null
             */
            private String orderType;
            private String orderNo;
            private String already;
            private String validDate;
            private String createPerson;
            private String weight;
            private String remark;
            private String takePerson;
            private String customerName;
            private String total;
            private String price;
            private String userIds;
            private int customerId;
            private String userNames;
            private List<ProductOutListEntity> productOutList;
            private int id;
            private String secondCustomerName;
            private String processPerson;
            private String productType;
            private String createDate;
            private String status;
            private String carIds;

            public void setOrderType(String orderType) {
                this.orderType = orderType;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public void setAlready(String already) {
                this.already = already;
            }

            public void setValidDate(String validDate) {
                this.validDate = validDate;
            }

            public void setCreatePerson(String createPerson) {
                this.createPerson = createPerson;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public void setTakePerson(String takePerson) {
                this.takePerson = takePerson;
            }

            public void setCustomerName(String customerName) {
                this.customerName = customerName;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public void setUserIds(String userIds) {
                this.userIds = userIds;
            }

            public void setCustomerId(int customerId) {
                this.customerId = customerId;
            }

            public void setUserNames(String userNames) {
                this.userNames = userNames;
            }

            public void setProductOutList(List<ProductOutListEntity> productOutList) {
                this.productOutList = productOutList;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setSecondCustomerName(String secondCustomerName) {
                this.secondCustomerName = secondCustomerName;
            }

            public void setProcessPerson(String processPerson) {
                this.processPerson = processPerson;
            }

            public void setProductType(String productType) {
                this.productType = productType;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setCarIds(String carIds) {
                this.carIds = carIds;
            }

            public String getOrderType() {
                return orderType;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public String getAlready() {
                return already;
            }

            public String getValidDate() {
                return validDate;
            }

            public String getCreatePerson() {
                return createPerson;
            }

            public String getWeight() {
                return weight;
            }

            public String getRemark() {
                return remark;
            }

            public String getTakePerson() {
                return takePerson;
            }

            public String getCustomerName() {
                return customerName;
            }

            public String getTotal() {
                return total;
            }

            public String getPrice() {
                return price;
            }

            public String getUserIds() {
                return userIds;
            }

            public int getCustomerId() {
                return customerId;
            }

            public String getUserNames() {
                return userNames;
            }

            public List<ProductOutListEntity> getProductOutList() {
                return productOutList;
            }

            public int getId() {
                return id;
            }

            public String getSecondCustomerName() {
                return secondCustomerName;
            }

            public String getProcessPerson() {
                return processPerson;
            }

            public String getProductType() {
                return productType;
            }

            public String getCreateDate() {
                return createDate;
            }

            public String getStatus() {
                return status;
            }

            public String getCarIds() {
                return carIds;
            }

            public class ProductOutListEntity {
                /**
                 * batchNo : null
                 * orderNo : 201711020003
                 * orderItem : null
                 * specification : null
                 * positionCode : null
                 * pieces : 0
                 * sheets : null
                 * identification : null
                 * steelGrade : null
                 * outPlateNumber : null
                 * id : null
                 * proName : null
                 * netWgt : null
                 * status : null
                 */
                private String batchNo;
                private String orderNo;
                private String orderItem;
                private String specification;
                private String positionCode;
                private int pieces;
                private String sheets;
                private String identification;
                private String steelGrade;
                private String outPlateNumber;
                private String id;
                private String proName;
                private String netWgt;
                private String status;

                public void setBatchNo(String batchNo) {
                    this.batchNo = batchNo;
                }

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

                public void setPieces(int pieces) {
                    this.pieces = pieces;
                }

                public void setSheets(String sheets) {
                    this.sheets = sheets;
                }

                public void setIdentification(String identification) {
                    this.identification = identification;
                }

                public void setSteelGrade(String steelGrade) {
                    this.steelGrade = steelGrade;
                }

                public void setOutPlateNumber(String outPlateNumber) {
                    this.outPlateNumber = outPlateNumber;
                }

                public void setId(String id) {
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

                public String getBatchNo() {
                    return batchNo;
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

                public int getPieces() {
                    return pieces;
                }

                public String getSheets() {
                    return sheets;
                }

                public String getIdentification() {
                    return identification;
                }

                public String getSteelGrade() {
                    return steelGrade;
                }

                public String getOutPlateNumber() {
                    return outPlateNumber;
                }

                public String getId() {
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
