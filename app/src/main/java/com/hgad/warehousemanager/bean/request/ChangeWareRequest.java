package com.hgad.warehousemanager.bean.request;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/13.
 */
public class ChangeWareRequest extends BaseRequest {
    private String markNum;
    private String address;
    private String operUser;
    private String model;

    public ChangeWareRequest(String markNum, String address, String operUser, String model) {
        this.markNum = markNum;
        this.address = address;
        this.operUser = operUser;
        this.model = model;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.PRODUCT);
        return ip + "scanMove.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("identification", markNum);
        map.put("positionCode", address);
        map.put("userName", operUser);
        map.put("terminal", model);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
