package com.hgad.warehousemanager.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/13.
 */
@DatabaseTable(tableName = IpInfo.TABLE_NAME)
public class IpInfo implements Serializable {

    public static final String TABLE_NAME = "t_ip_info";
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "ip")
    private String ip;
    @DatabaseField(columnName = "isDefault")
    private boolean isDefault;

    public IpInfo() {
    }

    public IpInfo(String name, String ip, boolean isDefault) {
        this.name = name;
        this.ip = ip;
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "IpInfo{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
