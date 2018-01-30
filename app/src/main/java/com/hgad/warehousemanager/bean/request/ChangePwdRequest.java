package com.hgad.warehousemanager.bean.request;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/20.
 */
public class ChangePwdRequest extends BaseRequest {
    private String username;
    private String password;
    private String newPwd;

    public ChangePwdRequest(String username, String password, String newPwd) {
        this.username = username;
        this.password = password;
        this.newPwd = newPwd;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.SYSTEM);
        return ip + "saveNewPwd.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("newPwd", newPwd);
        map.put("password", password);
        map.put("username", username);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
