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
public class OutOrderListRequest extends BaseRequest {
    private int pageNum;
    private String state;
    private String orderNum;

    public OutOrderListRequest(int pageNum, String state, String orderNum) {
        this.pageNum = pageNum;
        this.state = state;
        this.orderNum = orderNum;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.PRODUCT);
        return ip + "getScanOutOrderList.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNum + "");
        if (state != null) {
            map.put("status", state);
        }
        if (orderNum != null) {
            map.put("orderNo", orderNum);
        }
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
