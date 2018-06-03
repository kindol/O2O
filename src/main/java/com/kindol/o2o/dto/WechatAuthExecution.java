package com.kindol.o2o.dto;

import com.kindol.o2o.entity.WeChatAuth;
import com.kindol.o2o.enums.WechatAuthStateEnum;

import java.util.List;

public class WechatAuthExecution {

    //结果状态
    private int state;
    private String stateInfo;

    private int count;
    private WeChatAuth weChatAuth;
    private List<WeChatAuth> weChatAuthList;

    public WechatAuthExecution() {
    }

    // 失败的构造器
    public WechatAuthExecution(WechatAuthStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    // 成功的构造器
    public WechatAuthExecution(WechatAuthStateEnum stateEnum, WeChatAuth wechatAuth) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.weChatAuth = wechatAuth;
    }

    // 成功的构造器
    public WechatAuthExecution(WechatAuthStateEnum stateEnum,
                               List<WeChatAuth> wechatAuthList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.weChatAuthList = wechatAuthList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public WeChatAuth getWeChatAuth() {
        return weChatAuth;
    }

    public void setWeChatAuth(WeChatAuth weChatAuth) {
        this.weChatAuth = weChatAuth;
    }

    public List<WeChatAuth> getWeChatAuthList() {
        return weChatAuthList;
    }

    public void setWeChatAuthList(List<WeChatAuth> weChatAuthList) {
        this.weChatAuthList = weChatAuthList;
    }
}
