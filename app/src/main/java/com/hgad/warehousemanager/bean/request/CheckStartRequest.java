package com.hgad.warehousemanager.bean.request;

import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.HttpConstants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/6.
 */
public class CheckStartRequest extends BaseRequest {

    private String positionCode;
    private String userName;
    private String taskId;
    private String markNum;
    private String type;

    public CheckStartRequest(String positionCode, String userName, String taskId, String markNum, String type) {
        this.positionCode = positionCode;
        this.userName = userName;
        this.taskId = taskId;
        this.markNum = markNum;
        this.type = type;
    }

    @Override
    public String getUrl() {
        String address = SPUtils.getString(BaseApplication.getApplication().getApplicationContext(), SPConstants.IP);
        String ip = HttpConstants.ReqFormatUrl(address, HttpConstants.PRODUCT);
        return ip + "saveChk.do";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("positionCode", positionCode);
        map.put("userName", userName);
        map.put("taskId", taskId);
        if (markNum != null) {
            map.put("realIdenification", markNum);
        }
        if (type != null) {
            map.put("type", type);
        }
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}
