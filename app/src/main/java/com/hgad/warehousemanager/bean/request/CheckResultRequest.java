package com.hgad.warehousemanager.bean.request;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/13.
 */
public class CheckResultRequest extends BaseRequest{
    @Override
    public String getUrl() {
        String ip = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
