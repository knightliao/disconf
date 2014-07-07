package com.baidu.disconf2.web.service.sign.service;

import com.baidu.disconf2.web.service.user.bo.User;

/**
 * 
 * @author liaoqiqi
 * @version 2014-2-6
 */
public interface SignMgr {

    public User getUserByName(String name);

    boolean validate(String userPassword, String passwordToBeValidate);

    User signin(String phone);
}
