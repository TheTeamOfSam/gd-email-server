package com.sam.graduation.design.gdemailserver.service.email.impl;

import com.sam.graduation.design.gdemailserver.controller.dto.EmailCodeItem;
import com.sam.graduation.design.gdemailserver.controller.dto.EmailResponseDto;
import com.sam.graduation.design.gdemailserver.dao.EmailCodeMapper;
import com.sam.graduation.design.gdemailserver.service.base.BaseService;
import com.sam.graduation.design.gdemailserver.service.email.EmailCodeService;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author sam199510 273045049@qq.com
 * @version 创建时间：2018/1/29 15:22:52
 */
@Service
public class EmailCodeServiceImpl extends BaseService implements EmailCodeService {

    private static final String[] characters = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C", "V", "B", "N", "M",
            "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m"};

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
    public EmailResponseDto sendEmail(String toEmailAddress) {
        EmailResponseDto dto = new EmailResponseDto();

        EmailCodeItem emailCodeItem = new EmailCodeItem();

        int characters_length = characters.length;
        String emailCode = "";
        for (int i = 0; i < 6; i++) {
            emailCode += characters[(int) (Math.ceil(Math.random() * characters_length) - 1)];
        }

        boolean sendResult = this.sendEmail(toEmailAddress, emailCode);

        if (sendResult) {
            dto.setCode(emailCode);
            dto.setFeedbackMessage("验证码发送成功");
            dto.setSuccess(true);
            return dto;
        } else {
            dto.setCode(null);
            dto.setFeedbackMessage("验证码发送失败");
            dto.setSuccess(false);
            return dto;
        }
    }

    public boolean sendEmail(String to, String code) {
        String from = fromEmailAddress;
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
            message.setText("这是您的注册验证码：" + code);
            Transport.send(message);
            return true;
        } catch (Exception e) {
            logger.error("e:{}!", e);
        }
        return false;
    }
}
