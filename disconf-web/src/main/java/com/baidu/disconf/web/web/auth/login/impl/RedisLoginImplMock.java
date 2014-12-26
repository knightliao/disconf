package com.baidu.disconf.web.web.auth.login.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.constant.UserConstant;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.disconf.web.service.user.service.UserMgr;
import com.baidu.disconf.web.web.auth.login.RedisLogin;
import com.baidu.ub.common.commons.ThreadContext;

/**
 * @author liaoqiqi
 * @version 2014-2-4
 */
public class RedisLoginImplMock implements RedisLogin {

    @Autowired
    private UserMgr userMgr;

    /**
     * 校验Redis是否登录
     */
    @Override
    public Visitor isLogin(HttpServletRequest request) {

        Long userId = 1L;

        User user = userMgr.getUser(userId);

        Visitor visitor = new Visitor();
        visitor.setId(userId);
        visitor.setLoginUserId(userId);
        visitor.setLoginUserName(user.getName());

        return visitor;
    }

    @Override
    public void login(HttpServletRequest request, User user, int expireTime) {

        Visitor visitor = new Visitor();

        visitor.setId(user.getId());
        visitor.setLoginUserId(user.getId());
        visitor.setLoginUserName(user.getName());

        //
        // 更新session
        //
        updateSessionVisitor(request.getSession(), visitor);
    }

    @Override
    public void updateSessionVisitor(HttpSession session, Visitor visitor) {

        if (visitor != null) {
            // 更新
            session.setAttribute(UserConstant.USER_KEY, visitor);
        } else {

            // 删除
            session.removeAttribute(UserConstant.USER_KEY);
        }

        ThreadContext.putSessionVisitor(visitor);
    }

    @Override
    public void logout(HttpServletRequest request) {

        // 更新session
        updateSessionVisitor(request.getSession(), null);
    }
}
