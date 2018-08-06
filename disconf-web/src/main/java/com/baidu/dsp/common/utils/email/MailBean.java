package com.baidu.dsp.common.utils.email;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.baidu.disconf.web.config.ApplicationPropertyConfig;

import java.util.Properties;

/**
 * 邮件发送公共类
 *
 * @author modi
 * @version 1.0.0
 */
@Service
public class MailBean {

    protected static final Logger LOG = LoggerFactory.getLogger(MailBean.class);

    @Autowired
    private ApplicationPropertyConfig emailProperties;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送html邮件
     *
     * @throws MessagingException
     * @throws AddressException
     */
    public void sendHtmlMail(String from, String[] to, String title, String text) throws AddressException, MessagingException {

        long start = System.currentTimeMillis();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, emailProperties.getEmailEncoding());

        InternetAddress[] toArray = new InternetAddress[to.length];
        for (int i = 0; i < to.length; i++) {
            toArray[i] = new InternetAddress(to[i]);
        }

        messageHelper.setFrom(new InternetAddress(from));
        messageHelper.setTo(toArray);
        messageHelper.setSubject(title);
        messageHelper.setText(text, true);
        mimeMessage = messageHelper.getMimeMessage();
        mailSender.send(mimeMessage);
        long end = System.currentTimeMillis();
        LOG.info("send mail start:" + start + " end :" + end);
    }

}
