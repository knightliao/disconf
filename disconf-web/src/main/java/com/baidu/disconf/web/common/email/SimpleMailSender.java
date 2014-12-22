package com.baidu.disconf.web.common.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单邮件发送器
 *
 * @author liaoqiqi
 * @version 2014-3-2
 */
public class SimpleMailSender {

    protected static final Logger LOG = LoggerFactory.getLogger(SimpleMailSender.class);

    /**
     * @param mailInfo
     *
     * @return
     */
    private static Message setCommon(MailSenderInfo mailInfo) throws MessagingException {

        //
        // 判断是否需要身份认证
        //
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);

        // 根据session创建一个邮件消息
        Message mailMessage = new MimeMessage(sendMailSession);

        // 创建邮件发送者地址
        Address from = new InternetAddress(mailInfo.getFromAddress());

        // 设置邮件消息的发送者
        mailMessage.setFrom(from);

        // 创建邮件的接收者地址，并设置到邮件消息中
        Address to = new InternetAddress(mailInfo.getToAddress());
        mailMessage.setRecipient(Message.RecipientType.TO, to);

        // 设置邮件消息的主题
        mailMessage.setSubject(mailInfo.getSubject());

        // 设置邮件消息发送的时间
        mailMessage.setSentDate(new Date());

        return mailMessage;

    }

    /**
     * 以文本格式发送邮件
     *
     * @param mailInfo 待发送的邮件的信息
     */
    public static boolean sendTextMail(MailSenderInfo mailInfo) {

        try {

            // 设置一些通用的数据
            Message mailMessage = setCommon(mailInfo);

            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
            mailMessage.setText(mailContent);

            // 发送邮件
            Transport.send(mailMessage);

            return true;

        } catch (MessagingException ex) {

            ex.printStackTrace();
        }

        return false;
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mailInfo 待发送的邮件信息
     */
    public static boolean sendHtmlMail(MailSenderInfo mailInfo) {

        try {

            LOG.info("send to " + mailInfo.getFromAddress());

            // 设置一些通用的数据
            Message mailMessage = setCommon(mailInfo);

            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();

            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();

            // 设置HTML内容
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);

            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);

            // 发送邮件
            Transport.send(mailMessage);

            LOG.info("send to " + mailInfo.getFromAddress() + " dnoe.");

            return true;

        } catch (MessagingException ex) {

            LOG.error(ex.toString(), ex);
        }
        return false;
    }
}