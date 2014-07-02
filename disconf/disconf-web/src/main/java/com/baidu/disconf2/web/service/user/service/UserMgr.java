package com.baidu.disconf2.web.service.user.service;

import java.util.List;

import com.baidu.disconf2.web.service.user.bo.User;
import com.baidu.disconf2.web.service.user.dto.Visitor;

/**
 * 
 * @author liaoqiqi
 * @version 2013-11-28
 */
public interface UserMgr {

    /**
     * 获取用户的基本信息（登录用户）
     * 
     * @return
     */
    Visitor getVisitor(Integer userId);

    User getUser(Integer userId);

    /**
     * 
     * @return
     */
    Integer create(User user);

    /**
     * 
     * @param user
     */
    void create(List<User> user);

    /**
     * 
     * @return
     */
    List<User> getAll();

}
