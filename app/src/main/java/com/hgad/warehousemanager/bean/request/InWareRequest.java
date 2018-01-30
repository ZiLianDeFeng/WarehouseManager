package com.hgad.warehousemanager.bean.request;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/13.
 */
public class InWareRequest extends BaseRequest {

    private String markNum;
    private String username;
    private String orderItem;
    private String positionCode;
    private String sheets;
    private String netWgt;
    private String specification;
    private String proName;
    private String status;
    private String steelGrade;
    private String model;
    private String count;
    private String type;
    private String groupId;

    public InWareRequest(String markNum, String username, String positionCode, String count, String type, String model, String status) {
        this.markNum = markNum;
        this.username = username;
        this.positionCode = positionCode;
        this.count = count;
        this.type = type;
        this.model = model;
        this.status = status;
    }

    public InWareRequest(String markNum, String username, String orderItem, String positionCode, String sheets, String netWgt, String specification, String proName, String status, String steelGrade, String model, String count, String type, String groupId) {
        this.markNum = markNum;
        this.username = username;
        this.orderItem = orderItem;
        this.positionCode = positionCode;
        this.sheets = sheets;
        this.netWgt = netWgt;
        this.specification = specification;
        this.proName = proName;
        this.status = status;
        this.steelGrade = steelGrade;
        this.model = model;
        this.count = count;
        this.type = type;
        this.groupId = groupId;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.PRODUCT);
        return ip + "scanIn.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("identification", markNum);
        map.put("userName", username);
        if (sheets != null) {
            map.put("sheets", sheets);
        }
        if (netWgt != null) {
            map.put("netWgt", netWgt);
        }
        if (specification != null) {
            map.put("specification", specification);
        }
        if (proName != null) {
            map.put("proName", proName);
        }
        map.put("status", status);
        if (orderItem != null) {
            map.put("orderItem", orderItem);
        }
        map.put("positionCode", positionCode);
        if (steelGrade != null) {
            map.put("steelGrade", steelGrade);
        }
        map.put("orderNo", "");
        map.put("type", type);
        map.put("terminal", model);
        if (count != null) {
            map.put(Constants.COUNT, count);
        } else {
            map.put(Constants.COUNT, "1");
        }
        if (groupId != null) {
            map.put("groupId", groupId);
        }
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
