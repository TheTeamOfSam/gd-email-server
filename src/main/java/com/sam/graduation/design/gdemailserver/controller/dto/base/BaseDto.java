package com.sam.graduation.design.gdemailserver.controller.dto.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author sam199510 273045049@qq.com
 * @version 创建时间：2018/1/29 14:48:02
 */
public class BaseDto {

    @JsonProperty("created_time")
    private Date createdTime;

    @JsonProperty("last_modified_time")
    private Date lastModifiedTime;

    @JsonProperty("is_delete")
    private Byte isDelete;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}
