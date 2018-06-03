package com.kindol.o2o.entity;

import java.util.Date;

public class Area {

    /*
    全部都用引用类型，防止出现默认赋值，空就是空
     */
    private Integer areaId;
    private String areaName;
    private Integer priority;       //权重，用于展示时的排序用
    private Date createTime;
    private Date lastEditTime;      //更新事件

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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
}
