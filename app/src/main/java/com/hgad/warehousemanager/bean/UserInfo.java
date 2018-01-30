package com.hgad.warehousemanager.bean;

import com.hgad.warehousemanager.bean.response.GetMemberListResponse;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/27.
 */

@DatabaseTable(tableName = UserInfo.TABLE_NAME)
public class UserInfo implements Serializable {

    public static final String TABLE_NAME = "t_user_info";
    @DatabaseField(id = true)
    private int id;

    @DatabaseField(columnName = "userName")
    private String userName;

    @DatabaseField(columnName = "password")
    private String password;

    @DatabaseField(columnName = "age")
    private int age;

    @DatabaseField(columnName = "sex")
    private int sex;

    @DatabaseField(columnName = "realName")
    private String realName;

    private boolean isChecked;

    public UserInfo() {
    }

    public UserInfo(int id, String realName, boolean isChecked) {
        this.id = id;
        this.realName = realName;
        this.isChecked = isChecked;
    }

    public void setData(GetMemberListResponse.DataEntity dataEntity) {
        this.id = dataEntity.getId();
        this.realName = dataEntity.getRealname();
        this.userName = dataEntity.getUsername();
        this.isChecked = false;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
