package com.kindol.o2o.entity;

import java.util.Date;

/*
本地账号
 */
public class LocalAuth {

    private Long localAuthId;
    private String username;
    private String password;
    private Date createTime;
    private Date lastEditTime;
    private PersonInfo personInfo;          //用户所包含的信息，通过用户信息与微信账号简历连接（外键），在关系数据库中通过外键表示对象的引用

    public Long getLocalAuthId() {
        return localAuthId;
    }

    public void setLocalAuthId(Long localAuthId) {
        this.localAuthId = localAuthId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
