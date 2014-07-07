package com.baidu.disconf2.web.service.sign.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.disconf2.web.service.sign.dao.SignDao;
import com.baidu.disconf2.web.service.sign.service.SignMgr;
import com.baidu.disconf2.web.service.sign.utils.SignUtils;
import com.baidu.disconf2.web.service.user.bo.User;
import com.baidu.disconf2.web.service.user.dao.UserDao;
import com.baidu.ub.common.log.AopLogFactory;

/**
 * 与登录登出相关的
 * 
 * @author liaoqiqi
 * @version 2014-2-6
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SignMgrImpl implements SignMgr {

    protected final static Logger LOG = AopLogFactory
            .getLogger(SignMgrImpl.class);

    @Autowired
    private SignDao signDao;

    @Autowired
    private UserDao userDao;

    /**
     * 根据 用户名获取用户
     */
    @Override
    public User getUserByName(String name) {

        return userDao.getUserByName(name);
    }

    /**
     * 验证密码是否正确
     * 
     * @param token
     * @param password
     * @return
     */
    public boolean validate(String userPassword, String passwordToBeValidate) {

        String data = SignUtils.createPassword(passwordToBeValidate);

        if (data.equals(userPassword)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 登录
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public User signin(String phone) {

        //
        // 获取用户
        //
        User user = userDao.getUserByName(phone);

        userDao.update(user);

        return user;
    }

}
