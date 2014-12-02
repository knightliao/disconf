package com.baidu.disconf.web.service.user.constant;

import org.slf4j.Logger;

import com.baidu.ub.common.log.AopLogFactory;

/**
 * 
 * @author liaoqiqi
 * @version 2014-1-13
 */
public class UserConstant {

    protected final static Logger LOG = AopLogFactory.getLogger(UserConstant.class);

    /**
     * 是白名单用户
     */
    public static final int IS_WHITE_USER = 1;

    /**
     * 不是白名单用户
     */
    public static final int IS_NOT_WHITE_USER = 0;

    /**
     * 在session中缓存visitor的key
     */
    public static final String USER_KEY = "user_key";

    /**
     * 系统更新的数据统一用这个
     */
    public static final String USER_SYSTEM = "SYSTEM";

    //
    //
    //
    public static final String USER_APP_SEP = ",";

}
