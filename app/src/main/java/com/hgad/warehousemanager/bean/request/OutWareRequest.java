package com.hgad.warehousemanager.bean.request;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/16.
 */
public class OutWareRequest extends BaseRequest {

    private String markNum;
    private String username;
    private String orderNum;
    private String address;
    private String model;
    private String type;
    private String batchNo;
    private String groupId;

    public OutWareRequest(String markNum, String username, String orderNum, String address, String model, String type, String batchNo, String groupId) {
        this.markNum = markNum;
        this.username = username;
        this.orderNum = orderNum;
        this.address = address;
        this.model = model;
        this.type = type;
        this.batchNo = batchNo;
        this.groupId = groupId;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.PRODUCT);
        return ip + "scanOut.do";
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
        map.put("positionCode", address);
        map.put("orderNo", orderNum);
        map.put("terminal", model);
        map.put("batchNo", batchNo);
        if (type != null) {
            map.put("productType", type);
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
