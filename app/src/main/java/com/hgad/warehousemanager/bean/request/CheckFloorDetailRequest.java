package com.hgad.warehousemanager.bean.request;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/9.
 */
public class CheckFloorDetailRequest extends BaseRequest {


    private String orderId;
    private String columnCode;

    public CheckFloorDetailRequest(String orderId, String columnCode) {
        this.orderId = orderId;
        this.columnCode = columnCode;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.PRODUCT);
        return ip + "getChkDtlList.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        if (orderId != null) {
            map.put("orderId", orderId);
        }
        if (columnCode != null) {
            map.put("columnCode", columnCode);
        }
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
