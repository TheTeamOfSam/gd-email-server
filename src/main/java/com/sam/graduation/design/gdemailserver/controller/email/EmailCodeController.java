package com.sam.graduation.design.gdemailserver.controller.email;

import com.sam.graduation.design.gdemailserver.controller.base.BaseController;
import com.sam.graduation.design.gdemailserver.controller.dto.EmailResponseDto;
import com.sam.graduation.design.gdemailserver.service.email.EmailCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sam199510 273045049@qq.com
 * @version 创建时间：2018/1/29 15:24:11
 */
@RestController
@RequestMapping("/gdemailserver")
@Api("邮箱验证码发送及验证类")
public class EmailCodeController extends BaseController {

    @Autowired
    private EmailCodeService emailCodeService;

    @ApiOperation("邮箱验证码发送接口")
    @RequestMapping(value = "/email/code/@send", method = RequestMethod.POST)
    public EmailResponseDto emailCodeSend(
            @RequestParam("email") String email
    ) {
        EmailResponseDto dto = this.emailCodeService.sendEmailCode(email);
        return dto;
    }

    @ApiOperation("短信验证码验证接口")
    @RequestMapping(value = "/email/code/@check", method = RequestMethod.POST)
    public EmailResponseDto emailCodeCheck(
            @RequestParam("email") String email,
            @RequestParam("code") String code
    ) {
        return this.emailCodeService.checkEmailCode(email, code);
    }

}
