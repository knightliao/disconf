package com.baidu.disconf.web.web.auth.constant;

/**
 * @author liaoqiqi
 * @version 2013-11-28
 */
public class LoginConstant {

    /**
     * 存在Redis中的过期时间 30分钟
     */
    public static final int SESSION_EXPIRE_TIME = 1800;

    // 30天
    public static final int SESSION_EXPIRE_TIME2 = 3600 * 24 * 30;

    public static final String XONE_COOKIE_NAME_STRING = "DISCONF";

    /**
     * 在Cookie的时间，一年
     */
    public static final int XONE_COOKIE_AGE = 60 * 60 * 24 * 365;

}
