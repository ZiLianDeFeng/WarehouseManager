package com.hgad.warehousemanager.bean.request;

import android.text.TextUtils;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/21.
 */
public class TaskRequest extends BaseRequest {
    private String type;
    private int pageNum;
    private int userId;

    public TaskRequest(String type, int pageNum, int userId) {
        this.type = type;
        this.pageNum = pageNum;
        this.userId = userId;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address,HttpConstants.PRODUCT);
        return ip + "getProInOutList.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(type)) {
            map.put("type", "'"+type+"'");
        }
        map.put("pageNum", pageNum + "");
        map.put("pageSize", "10");
        map.put("userIds", userId + "");
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}

