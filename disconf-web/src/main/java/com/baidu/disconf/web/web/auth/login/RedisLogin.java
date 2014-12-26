package com.baidu.disconf.web.web.auth.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dto.Visitor;

/**
 * @author liaoqiqi
 * @version 2014-2-4
 */
public interface RedisLogin {

    /**
     * 判断是否登录
     *
     * @param request
     *
     * @return
     */
    Visitor isLogin(HttpServletRequest request);

    /**
     * 登录
     *
     * @param request
     */
    void login(HttpServletRequest request, User user, int expireTime);

    /**
     * 更新session数据
     *
     * @param session
     * @param visitor
     */
    void updateSessionVisitor(HttpSession session, Visitor visitor);

    void logout(HttpServletRequest request);
}
