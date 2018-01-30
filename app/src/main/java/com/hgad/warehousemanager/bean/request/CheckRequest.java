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
public class CheckRequest extends BaseRequest {
    private String markNum;
    private String proName;
    private String address;
    private String actPosition;
    private String type;

    public CheckRequest(String markNum, String actPosition, String type) {
        this.markNum = markNum;
        this.actPosition = actPosition;
        this.type = type;
    }

    public CheckRequest(String markNum, String proName, String address, String actPosition, String type) {
        this.markNum = markNum;
        this.proName = proName;
        this.address = address;
        this.actPosition = actPosition;
        this.type = type;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.PRODUCT);
        return ip + "saveWrong.do";
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
        if (proName != null) {
            map.put("proName", proName);
        }
        map.put("positionCode", address == null ? "" : address);
        map.put("actPosition", actPosition);
        if (type != null) {
            map.put("type", type);
        }
//        map.put("status", "0");
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
