package com.kindol.o2o.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户授权token，由此实体类获取用户实体类
 * @JsonProperty 用于直接由json串填充到成员变量中
 */
public class UserAccessToken {

    //获取到的凭证
    @JsonProperty("access_token")
    private String accessToken;

    //凭证有效时间，单位：秒
    @JsonProperty("expires_in")
    private String expiresIn;

    //表示更新令牌，用于获取下一次访问令牌
    @JsonProperty("refresh_token")
    private String refreshToken;

    //该用户在此公众号下的唯一id
    @JsonProperty("openid")
    private String openId;

    //权限范围
    @JsonProperty("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "UserAccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", openId='" + openId + '\'' +
                '}';
    }
}
