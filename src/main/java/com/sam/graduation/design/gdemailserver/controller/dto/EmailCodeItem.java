package com.sam.graduation.design.gdemailserver.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sam.graduation.design.gdemailserver.controller.dto.base.BaseDto;
import com.sam.graduation.design.gdemailserver.model.pojo.EmailCode;

import java.util.Date;

/**
 * @author sam199510 273045049@qq.com
 * @version 创建时间：2018/1/29 14:45:44
 */
public class EmailCodeItem extends BaseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("code")
    private String code;

    @JsonProperty("generate_time")
    private Date generateTime;

    @JsonProperty("expiration_time")
    private Date expirationTime;

    @JsonProperty("status")
    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public EmailCode to() {
        EmailCode emailCode = new EmailCode();
        emailCode.setId(this.id);
        emailCode.setEmail(this.email);
        emailCode.setCode(this.code);
        emailCode.setGenerateTime(this.generateTime);
        emailCode.setExpirationTime(this.expirationTime);
        emailCode.setStatus(this.status);
        return emailCode;
    }

    public void from(EmailCode emailCode) {
        this.id = emailCode.getId();
        this.email = emailCode.getEmail();
        this.code = emailCode.getCode();
        this.generateTime = emailCode.getGenerateTime();
        this.expirationTime = emailCode.getExpirationTime();
        this.status = emailCode.getStatus();
    }

}
