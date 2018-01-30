package com.hgad.warehousemanager.bean.request;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/26.
 */
public class DeletMemberRequest extends BaseRequest {
    private String groupId;
    private String userName;
    private String userId;

    public DeletMemberRequest(String groupId, String userName, String userId) {
        this.groupId = groupId;
        this.userName = userName;
        this.userId = userId;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.GROUP);
        return ip + "delGroupMember.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        map.put("userName", userName);
        map.put("userId", userId);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
