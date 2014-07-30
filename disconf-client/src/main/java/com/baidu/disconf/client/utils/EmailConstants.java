package com.baidu.disconf.client.utils;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-1
 */
public final class EmailConstants {

    private EmailConstants() {

    }

    /**
     * 发送邮件中附件的最大大小，2M
     */
    public static final long MAX_MAILFILE_SIZE = 2000000;

    /**
     * 发送报警邮件中标题的最大长度，255
     */
    public static final int ALARM_MAIL_TITLE_LENGTH = 255;

    /**
     * 发送报警邮件中邮件标题信息
     */
    public static final String ALARM_MAIL_TITLE = "fatal message mail ";

}
