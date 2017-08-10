package com.hgad.warehousemanager.net;

import java.util.Map;

/**
 * Created by Administrator on 2016/7/24.
 * <p/>
 * 请求地址，请求方式，请求参数
 */
public abstract class BaseRequest {

    public enum HttpMethod {
        GET, POST;
    }

    public boolean noCache() {

        return false;
    }

    public abstract String getUrl();

    public abstract HttpMethod getHttpMethod();

//        return HttpMethod.POST;

    public abstract Map<String, String> getParams();

    public abstract Map<String, String> getHeaders();
}
