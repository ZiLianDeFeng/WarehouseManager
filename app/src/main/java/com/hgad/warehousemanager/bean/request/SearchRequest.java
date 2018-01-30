package com.hgad.warehousemanager.bean.request;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/18.
 */
public class SearchRequest extends BaseRequest {
    private String markNum;
    private String orderItem;
    private String proName;
    private String positionCode;
    private int pageNum;

    public SearchRequest(String markNum, String orderItem, String proName, String positionCode, int pageNum) {
        this.markNum = markNum;
        this.orderItem = orderItem;
        this.proName = proName;
        this.positionCode = positionCode;
        this.pageNum = pageNum;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.PRODUCT);
        return ip + "queryHistory.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        if (markNum != null) {
            map.put("identification", markNum);
        }
        if (orderItem != null) {
            map.put("orderItem", orderItem);
        }
        if (proName != null) {
            map.put("proName", proName);
        }
        if (positionCode != null) {
            map.put("positionCode", positionCode);
        }
        map.put("pageNo", pageNum + "");
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
