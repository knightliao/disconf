package com.baidu.disconf2.web.service.user.service;

import com.baidu.disconf2.web.service.user.dto.Visitor;

public interface UserInnerMgr {

    /**
     * 获取用户的基本信息（登录用户）
     * 
     * @return
     */
    Visitor getVisitor(Integer userId);
}
