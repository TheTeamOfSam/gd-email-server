package com.sam.graduation.design.gdemailserver.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sam199510 273045049@qq.com
 * @version 创建时间：2018/1/29 15:30:43
 */
public class EmailResponseDto {

    @JsonProperty("is_success")
    private boolean isSuccess;

    @JsonProperty("feedback_message")
    private String feedbackMessage;

    @JsonProperty("code")
    private String code;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
