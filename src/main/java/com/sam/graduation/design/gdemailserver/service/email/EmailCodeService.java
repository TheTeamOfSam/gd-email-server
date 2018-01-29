package com.sam.graduation.design.gdemailserver.service.email;

import com.sam.graduation.design.gdemailserver.controller.dto.EmailResponseDto;

/**
 * @author sam199510 273045049@qq.com
 * @version 创建时间：2018/1/29 15:22:36
 */
public interface EmailCodeService {

    EmailResponseDto sendEmail(String toEmailAddress);

}
