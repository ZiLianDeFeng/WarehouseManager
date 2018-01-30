package com.hgad.warehousemanager.bean.request;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/22.
 */
public class ForUpRequest extends BaseRequest {
    private int orderId;
    private String state;
    private String orderType;

    public ForUpRequest(int orderId, String state, String orderType) {
        this.orderId = orderId;
        this.state = state;
        this.orderType = orderType;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.PRODUCT);
        return ip + "saveOut.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", orderId + "");
        if (state != null) {
            map.put("status", state);
        }
        if (orderType != null) {
            map.put("orderType", orderType);
        }
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
