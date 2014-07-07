package com.baidu.disconf.web.service.user.service;

import com.baidu.disconf.web.service.user.dto.Visitor;

public interface UserInnerMgr {

    /**
     * 获取用户的基本信息（登录用户）
     * 
     * @return
     */
    Visitor getVisitor(Integer userId);
}
