package com.baidu.disconf.web.service.user.service;

/**
 * @author knightliao
 */
public interface AuthMgr {

    boolean verifyApp4CurrentUser(Long appId);
}
