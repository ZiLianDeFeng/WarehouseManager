package com.hgad.warehousemanager.bean.request;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/25.
 */
public class CreateGroupRequest extends BaseRequest {
    private String createUserId;
    private String createUserName;
    private String groupName;

    public CreateGroupRequest(String createUserId, String createUserName, String groupName) {
        this.createUserId = createUserId;
        this.createUserName = createUserName;
        this.groupName = groupName;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.GROUP);
        return ip + "saveGroup.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("createUserId", createUserId);
        map.put("createUserName", createUserName);
        map.put("groupName", groupName);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
