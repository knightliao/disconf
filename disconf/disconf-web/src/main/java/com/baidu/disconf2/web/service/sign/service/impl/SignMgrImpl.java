package com.baidu.disconf2.web.service.sign.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.disconf2.web.service.sign.dao.SignDao;
import com.baidu.disconf2.web.service.sign.service.SignMgr;
import com.baidu.disconf2.web.service.user.dao.UserDao;
import com.baidu.ub.common.log.AopLogFactory;

/**
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

}
