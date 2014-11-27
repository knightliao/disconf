package com.baidu.disconf.web.utils.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.web.common.email.MailSenderInfo;
import com.baidu.disconf.web.common.email.SimpleMailSender;

/**
 * 
 * @author liaoqiqi
 * @version 2014-3-2
 */
@Service
public class EmailService {

    @Autowired
    private EmailProperties emailProperties;

    /**
     * 发送HTML邮箱
     * 
     * @return
     */
    public boolean sendHtmlEmail(String toEmail, String subject, String content) {

        //
        // 这个类主要是设置邮件
        //
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost(emailProperties.getHost());
        mailInfo.setMailServerPort(emailProperties.getPort());
        mailInfo.setValidate(true);
        mailInfo.setUserName(emailProperties.getUser());
        mailInfo.setPassword(emailProperties.getPassword());// 您的邮箱密码

        mailInfo.setFromAddress(emailProperties.getFromEmail());
        mailInfo.setToAddress(toEmail);

        mailInfo.setSubject(subject);
        mailInfo.setContent(content);

        // 这个类主要来发送邮件
        return SimpleMailSender.sendHtmlMail(mailInfo);// 发送文体格式
    }
}
