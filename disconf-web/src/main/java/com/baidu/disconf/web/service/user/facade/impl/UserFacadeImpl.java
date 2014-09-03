package com.baidu.disconf.web.service.user.facade.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.web.service.user.facade.UserFacade;
import com.baidu.disconf.web.service.user.service.UserMgr;
import com.baidu.ub.common.log.AopLogFactory;

/**
 * 
 * @author liaoqiqi
 * @version 2014-1-13
 */
@Service
public class UserFacadeImpl implements UserFacade {

    protected final static Logger log = AopLogFactory
            .getLogger(UserFacadeImpl.class);

    @Autowired
    private UserMgr userMgr;

}
