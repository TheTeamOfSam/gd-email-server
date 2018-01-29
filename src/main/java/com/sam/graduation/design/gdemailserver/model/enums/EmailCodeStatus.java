package com.sam.graduation.design.gdemailserver.model.enums;

import com.sam.graduation.design.gdemailserver.model.enums.base.PojoEnumable;

/**
 * @author sam199510 273045049@qq.com
 * @version 创建时间：2018/1/29 15:03:23
 */
public enum EmailCodeStatus implements PojoEnumable {

    /**
     * 状态：0（发送失败），1（发送成功但未使用），2（发送成功并已经使用）
     */
    SEND_FAIL(0) {
        @Override
        public String description() {
            return "发送失败";
        }
    },
    SEND_SUCCESS_BUT_NOT_IN_USE(1) {
        @Override
        public String description() {
            return "发送成功但未使用";
        }
    },
    SEND_SUCCESS_AND_IN_USE(2) {
        @Override
        public String description() {
            return "发送成功并且已经使用";
        }
    },;

    private int actualValue;

    private EmailCodeStatus(int actualValue) {
        this.actualValue= actualValue;
    }

    @Override
    public int value() {
        return this.actualValue;
    }

    public int getActualValue() {
        return actualValue;
    }

    public static EmailCodeStatus from(int value) {
        for (EmailCodeStatus en: EmailCodeStatus.values()) {
            if (en.getActualValue() == value) {
                return en;
            }
        }
        return EmailCodeStatus.SEND_FAIL;
    }

    public static EmailCodeStatus from(String name) {
        for (EmailCodeStatus en: EmailCodeStatus.values()){
            if (en.name().equals(name)) {
                return en;
            }
        }
        return EmailCodeStatus.SEND_FAIL;
    }
}
