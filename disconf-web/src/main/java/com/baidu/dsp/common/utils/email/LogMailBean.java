package com.baidu.dsp.common.utils.email;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.web.config.ApplicationPropertyConfig;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.ub.common.commons.ThreadContext;

/**
 * @author liaoqiqi
 * @version 2014-4-8
 */
@Service
public class LogMailBean {

    protected static final Logger LOG = LoggerFactory.getLogger(LogMailBean.class);

    /**
     * 发送报警邮件中标题的最大长度，255
     */
    public static final int ALARM_MAIL_TITLE_LENGTH = 255;

    /**
     * 发送报警邮件中邮件标题信息
     */
    public static final String ALARM_MAIL_TITLE = "fatal message mail ";

    @Autowired
    private ApplicationPropertyConfig emailProperties;

    @Autowired
    private MailBean mailBean;

    /**
     * 得到系统当前的运行时间，并格式化
     *
     * @return
     */
    private static String getSystemDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        return sdf.format(date);
    }

    /**
     *
     */
    public void sendLogExceptionEmail(String message, Throwable e) {

        StringBuffer titleBuffer = new StringBuffer();
        StringBuffer logInfo = new StringBuffer();
        StringBuffer mailInfo = new StringBuffer();

        //
        // 确定 标题
        //
        Visitor visitor = ThreadContext.getSessionVisitor();

        if (null != visitor) {
            titleBuffer.append("Current Login UcId: " + visitor.getId());
            titleBuffer.append(" ");
        }

        if (message != null) {

            titleBuffer.append(message);

        } else if (e != null && e.getMessage() != null) {

            titleBuffer.append(e.getMessage());
        }

        String title = titleBuffer.toString();

        String systemDate = getSystemDate();
        logInfo.append(systemDate);
        logInfo.append("\t");
        logInfo.append(title);
        mailInfo.append(systemDate);
        mailInfo.append("\t");
        mailInfo.append(title);

        //
        // 出错内容
        //

        if (null != e) {
            logInfo.append("\n");
            logInfo.append(getExceptionInfo(e, systemDate, "\n", "\t"));
            mailInfo.append("<br>");
            mailInfo.append(getExceptionInfo(e, systemDate, "<br>", "\t"));
        }

        sendErrorMail(mailInfo.toString(), title);
    }

    /**
     * 发送HTML邮箱
     *
     * @return
     */
    public boolean sendHtmlEmail(String toEmail, String title, String content) {

        LOG.info("send to " + toEmail);
        LOG.info("title: " + title);
        LOG.info("content" + content);

        if (StringUtils.isBlank(toEmail)) {
            return false;
        }

        String localName = "";
        Visitor visitor = ThreadContext.getSessionVisitor();
        if (visitor != null) {
            LOG.info(visitor.toString());
            localName += visitor.getLoginUserName() + " ";
        }

        try {

            InetAddress addr = InetAddress.getLocalHost();
            localName += addr.getHostName().toString();

        } catch (UnknownHostException e) {

            LOG.warn("When send alarm mail,we can't get hostname", e);
        }

        String mailTitle = localName + "/" + getSystemDate();

        int len = 0;
        int lenLimit = ALARM_MAIL_TITLE_LENGTH;
        if (title != null) {
            len = title.length();
            if (len > lenLimit) {
                len = lenLimit;
            }
            mailTitle += title.substring(0, len);
        }

        String mailTo = toEmail;
        String mailFrom = emailProperties.getFromEmail();
        String[] mailToList = mailTo.split(";");

        if (content == null) {

            return false;

        } else {

            try {
                mailBean.sendHtmlMail(mailFrom, mailToList, mailTitle, content);
            } catch (Exception e) {
                LOG.error("When send alarm mail,we can't send it", e);
                return false;
            }
        }

        return true;
    }

    /**
     * 获取异常信息
     */
    private String getExceptionInfo(Throwable e, String systemDate, String newLineToken, String tabToken) {

        StringBuffer info = new StringBuffer();
        info.append(systemDate);
        info.append(tabToken);
        info.append("cause by: ");
        info.append(e.getClass().getName() + "--");
        info.append(e.getMessage());
        info.append(newLineToken);
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            info.append(systemDate);
            info.append(tabToken);
            info.append(stackTraceElement);
            info.append(newLineToken);
        }
        if (null != e.getCause() && e.getCause() instanceof Exception) {
            info.append(getExceptionInfo((Exception) e.getCause(), systemDate, newLineToken, tabToken));
        }
        return info.toString();
    }

    /**
     * 发送报警邮件，邮件名称，发送人，接收人在constants中定义
     *
     * @param content 邮件内容
     *
     * @return
     */
    private boolean sendErrorMail(String content, String title) {

        String localName = "";

        try {

            InetAddress addr = InetAddress.getLocalHost();
            localName = addr.getHostName().toString();

        } catch (UnknownHostException e) {

            LOG.warn("When send alarm mail,we can't get hostname", e);
        }

        String mailTitle = localName + " /" + ALARM_MAIL_TITLE + "/" + getSystemDate();

        int len = 0;
        int lenLimit = ALARM_MAIL_TITLE_LENGTH;
        if (title != null) {
            len = title.length();
            if (len > lenLimit) {
                len = lenLimit;
            }
            mailTitle += title.substring(0, len);
        }

        String mailTo = emailProperties.getEmailReceiver();
        String mailFrom = emailProperties.getFromEmail();
        String[] mailToList = mailTo.split(";");

        if (content == null) {

            return false;

        } else {

            try {
                mailBean.sendHtmlMail(mailFrom, mailToList, mailTitle, content);
            } catch (Exception e) {
                LOG.warn("When send alarm mail,we can't send it", e);
                return false;
            }
        }
        return true;
    }
}
