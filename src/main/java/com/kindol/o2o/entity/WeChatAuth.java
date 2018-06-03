package com.kindol.o2o.entity;

import java.util.Date;

/*
微信账号
 */
public class WeChatAuth {

    private Long wechatAuthId;          //用户本身ID
    private String openId;              //与公众号对接的ID
    private Date createTime;
    private PersonInfo personInfo;      //用户所包含的信息，通过用户信息与本地账号简历连接（外键）

    public Long getWechatAuthId() {
        return wechatAuthId;
    }

    public void setWechatAuthId(Long wechatAuthId) {
        this.wechatAuthId = wechatAuthId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
