package com.hgad.warehousemanager.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2017/6/27.
 */

@DatabaseTable(tableName = UserInfo.TABLE_NAME)
public class UserInfo {

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
    private int realName;

    public UserInfo() {
    }

    public UserInfo(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public int getRealName() {
        return realName;
    }

    public void setRealName(int realName) {
        this.realName = realName;
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

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", realName=" + realName +
                '}';
    }
}
