package com.sam.graduation.design.gdemailserver.service.email.impl;

import com.sam.graduation.design.gdemailserver.controller.dto.EmailResponseDto;
import com.sam.graduation.design.gdemailserver.dao.EmailCodeMapper;
import com.sam.graduation.design.gdemailserver.model.enums.EmailCodeStatus;
import com.sam.graduation.design.gdemailserver.model.pojo.EmailCode;
import com.sam.graduation.design.gdemailserver.service.base.BaseService;
import com.sam.graduation.design.gdemailserver.service.email.EmailCodeService;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sam199510 273045049@qq.com
 * @version 创建时间：2018/1/29 15:22:52
 */
@Service
public class EmailCodeServiceImpl extends BaseService implements EmailCodeService {

    private static final String[] characters = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "Q", "W", "E", "R",
            "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C", "V", "B", "N",
            "M", "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z",
            "x", "c", "v", "b", "n", "m"};

    @Value("${from.email.address}")
    private String fromEmailAddress;

    @Value("${from.email.password}")
    private String fromEmailPassword;

    @Value("${smtp.qq.com}")
    private String smtpQqCom;

    @Value("${mail.smtp.host}")
    private String mailSmtpHost;

    @Value("${mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${mail.smtp.ssl.enable}")
    private String mailSmtpSSLEnable;

    @Value("${mail.smtp.ssl.socketFactory}")
    private String mailSmtpSSLSocketFactory;

    @Autowired
    private EmailCodeMapper emailCodeMapper;

    @Override
    public EmailResponseDto sendEmailCode(String toEmailAddress) {
        EmailResponseDto dto = null;

        // TODO: 验证邮箱格式是否正确
        Pattern p = Pattern.compile(
                "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
        Matcher m = p.matcher(toEmailAddress);
        boolean isEmailRight = m.matches();
        if (!isEmailRight) {
            dto = new EmailResponseDto();
            dto.setSuccess(false);
            dto.setFeedbackMessage("请检查邮箱格式");
            return dto;
        }

        // TODO: 验证是否发送过于频繁
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        String toTime = sdf.format(nowTime);

        Date from = new Date(nowTime.getTime() - 300000);
        String fromTime = sdf.format(from);

        int frequentlyCount = this.emailCodeMapper.selectCountByEmailBetweenTime(toEmailAddress, fromTime, toTime);
        if (frequentlyCount > 2) {
            dto = new EmailResponseDto();
            dto.setFeedbackMessage("验证码获取过于频繁，五分钟内只允许三次！");
            dto.setSuccess(false);
            return dto;
        }

        // TODO: 先创建一个六位随机的带有大小字母和数字的验证码
        int characters_length = characters.length;
        String emailCode = "";
        for (int i = 0; i < 6; i++) {
            emailCode += characters[(int) (Math.ceil(Math.random() * characters_length) - 1)];
        }
        // TODO: 发送验证码
        boolean sendResult = this.sendRegisterEmail(toEmailAddress, emailCode);

        // TODO: 判断是否发送成功并成功保存到数据库
        if (!sendResult) {
            dto = new EmailResponseDto();
            dto.setCode(null);
            dto.setFeedbackMessage("验证码发送失败，请检查邮箱格式");
            dto.setSuccess(false);
            return dto;
        }

        // TODO: 保存数据库
        EmailCode emailCodePO = new EmailCode();
        emailCodePO.setEmail(toEmailAddress);
        emailCodePO.setCode(emailCode);
        emailCodePO.setGenerateTime(new Date());
        emailCodePO.setStatus((byte) EmailCodeStatus.SEND_SUCCESS_BUT_NOT_IN_USE.value());
        emailCodePO.setCreatedTime(new Date());
        emailCodePO.setLastModifiedTime(new Date());
        emailCodePO.setIsDelete((byte) 0);
        Date now = new Date();
        emailCodePO.setExpirationTime(new Date(now.getTime() + 300000));
        int saveResult = this.emailCodeMapper.insert(emailCodePO);

        if (saveResult == 1) {
            dto = new EmailResponseDto();
            dto.setCode(emailCode);
            dto.setFeedbackMessage("验证码发送成功");
            dto.setSuccess(true);
            return dto;
        } else {
            dto = new EmailResponseDto();
            dto.setCode(null);
            dto.setFeedbackMessage("系统异常，验证码发送失败");
            dto.setSuccess(false);
            return dto;
        }

    }

    public boolean sendRegisterEmail(String to, String code) {
        String host = smtpQqCom;
        Properties properties = System.getProperties();
        properties.setProperty(mailSmtpHost, host);
        properties.put(mailSmtpAuth, true);
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (Exception e) {
            logger.error("e:{}!", e);
        }
        sf.setTrustAllHosts(true);
        properties.put(mailSmtpSSLEnable, true);
        properties.put(mailSmtpSSLSocketFactory, sf);
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmailAddress, fromEmailPassword);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmailAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("注册邮件");
            message.setText("这是您的注册验证码：" + code + "。\n\n\n注意：过期时间为5分钟！");
            Transport.send(message);
            return true;
        } catch (Exception e) {
            logger.error("e:{}!", e);
        }
        return false;
    }

    @Override
    public EmailResponseDto sendResetPasswordEmailCode(String toEmailAddress) {
        EmailResponseDto dto = null;

        // TODO: 验证邮箱格式是否正确
        Pattern p = Pattern.compile(
                "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
        Matcher m = p.matcher(toEmailAddress);
        boolean isEmailRight = m.matches();
        if (!isEmailRight) {
            dto = new EmailResponseDto();
            dto.setSuccess(false);
            dto.setFeedbackMessage("请检查邮箱格式");
            return dto;
        }

        // TODO: 验证是否发送过于频繁
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        String toTime = sdf.format(nowTime);

        Date from = new Date(nowTime.getTime() - 300000);
        String fromTime = sdf.format(from);

        int frequentlyCount = this.emailCodeMapper.selectCountByEmailBetweenTime(toEmailAddress, fromTime, toTime);
        if (frequentlyCount > 2) {
            dto = new EmailResponseDto();
            dto.setFeedbackMessage("验证码获取过于频繁，五分钟内只允许三次！");
            dto.setSuccess(false);
            return dto;
        }

        // TODO: 先创建一个六位随机的带有大小字母和数字的验证码
        int characters_length = characters.length;
        String emailCode = "";
        for (int i = 0; i < 6; i++) {
            emailCode += characters[(int) (Math.ceil(Math.random() * characters_length) - 1)];
        }
        // TODO: 发送验证码
        boolean sendResult = this.sendResetPasswordEmail(toEmailAddress, emailCode);

        // TODO: 判断是否发送成功并成功保存到数据库
        if (!sendResult) {
            dto = new EmailResponseDto();
            dto.setCode(null);
            dto.setFeedbackMessage("验证码发送失败，请检查邮箱格式");
            dto.setSuccess(false);
            return dto;
        }

        // TODO: 保存数据库
        EmailCode emailCodePO = new EmailCode();
        emailCodePO.setEmail(toEmailAddress);
        emailCodePO.setCode(emailCode);
        emailCodePO.setGenerateTime(new Date());
        emailCodePO.setStatus((byte) EmailCodeStatus.SEND_SUCCESS_BUT_NOT_IN_USE.value());
        emailCodePO.setCreatedTime(new Date());
        emailCodePO.setLastModifiedTime(new Date());
        emailCodePO.setIsDelete((byte) 0);
        Date now = new Date();
        emailCodePO.setExpirationTime(new Date(now.getTime() + 300000));
        int saveResult = this.emailCodeMapper.insert(emailCodePO);

        if (saveResult == 1) {
            dto = new EmailResponseDto();
            dto.setCode(emailCode);
            dto.setFeedbackMessage("验证码发送成功");
            dto.setSuccess(true);
            return dto;
        } else {
            dto = new EmailResponseDto();
            dto.setCode(null);
            dto.setFeedbackMessage("系统异常，验证码发送失败");
            dto.setSuccess(false);
            return dto;
        }
    }

    public boolean sendResetPasswordEmail(String to, String code) {
        String host = smtpQqCom;
        Properties properties = System.getProperties();
        properties.setProperty(mailSmtpHost, host);
        properties.put(mailSmtpAuth, true);
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (Exception e) {
            logger.error("e:{}!", e);
        }
        sf.setTrustAllHosts(true);
        properties.put(mailSmtpSSLEnable, true);
        properties.put(mailSmtpSSLSocketFactory, sf);
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmailAddress, fromEmailPassword);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmailAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("重置密码邮件");
            message.setText("这是您的重置密码的邮箱验证码：" + code + "。\n\n\n注意：过期时间为5分钟！");
            Transport.send(message);
            return true;
        } catch (Exception e) {
            logger.error("e:{}!", e);
        }
        return false;
    }

    @Override
    public EmailResponseDto checkEmailCode(String emailAddress, String emailCode) {
        EmailResponseDto dto = null;

        // TODO: 验证邮箱格式是否正确
        Pattern p = Pattern.compile(
                "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
        Matcher m = p.matcher(emailAddress);
        boolean isEmailRight = m.matches();
        if (!isEmailRight) {
            dto = new EmailResponseDto();
            dto.setSuccess(false);
            dto.setFeedbackMessage("请检查邮箱格式");
            return dto;
        }

        EmailCode emailCodePO = this.emailCodeMapper.selectByEmailAndCodeOrderByGenerateTimeDesc(emailAddress);


        // TODO: 验证邮箱状态
        if (emailCodePO.getStatus() == EmailCodeStatus.SEND_SUCCESS_AND_IN_USE.value()) {
            dto = new EmailResponseDto();
            dto.setFeedbackMessage("此邮箱已经通过验证");
            dto.setSuccess(false);
            return dto;
        } else if (emailCodePO.getStatus() == EmailCodeStatus.SEND_SUCCESS_BUT_NOT_IN_USE.value()) {
            // TODO: 验证验证是否正确
            if (!emailCode.equals(emailCodePO.getCode())) {
                dto = new EmailResponseDto();
                dto.setFeedbackMessage("邮箱验证码不正确，请重新输入！");
                dto.setSuccess(false);
                return dto;
            }

            // TODO: 验证验证码是否已过期
            if ((new Date()).after(emailCodePO.getExpirationTime())) {
                dto = new EmailResponseDto();
                dto.setFeedbackMessage("此验证码已过期，请重新获取！");
                dto.setSuccess(false);
                return dto;
            }

            // TODO: 未过期，说明已经符合条件允许，可以通过验证
            emailCodePO.setStatus((byte) EmailCodeStatus.SEND_SUCCESS_AND_IN_USE.value());
            emailCodePO.setLastModifiedTime(new Date());
            int updateStatusResult = this.emailCodeMapper.updateByPrimaryKeySelective(emailCodePO);

            if (updateStatusResult == 1) {
                dto = new EmailResponseDto();
                dto.setSuccess(true);
                dto.setFeedbackMessage("恭喜，验证通过");
                return dto;
            } else {
                dto = new EmailResponseDto();
                dto.setSuccess(false);
                dto.setFeedbackMessage("验证失败");
                return dto;
            }

        } else {
            dto = new EmailResponseDto();
            dto.setSuccess(false);
            dto.setFeedbackMessage("验证失败，请再次重试！");
            return dto;
        }
    }
}
