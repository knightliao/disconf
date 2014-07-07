package com.baidu.disconf.web.service.user.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dao.UserDao;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.disconf.web.service.user.service.UserInnerMgr;
import com.baidu.disconf.web.service.user.service.UserMgr;
import com.baidu.disconf.web.service.user.vo.VisitorVo;
import com.baidu.ub.common.commons.ThreadContext;
import com.baidu.ub.common.log.AopLogFactory;

/**
 * 
 * @author liaoqiqi
 * @version 2013-12-5
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserMgrImpl implements UserMgr {

    protected final static Logger LOG = AopLogFactory
            .getLogger(UserMgrImpl.class);

    @Autowired
    private UserInnerMgr userInnerMgr;

    @Autowired
    private UserDao userDao;

    @Override
    public Visitor getVisitor(Integer userId) {

        return userInnerMgr.getVisitor(userId);
    }

    @Override
    public VisitorVo getCurVisitor() {

        Visitor visitor = ThreadContext.getSessionVisitor();
        if (visitor == null) {
            return null;
        }

        VisitorVo visitorVo = new VisitorVo();
        visitorVo.setId(visitor.getId());
        visitorVo.setName(visitor.getLoginUserName());

        return visitorVo;
    }

    /**
     * 创建
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Integer create(User user) {

        user = userDao.create(user);
        return user.getId();
    }

    /**
	 * 
	 */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void create(List<User> users) {

        userDao.create(users);
    }

    @Override
    public List<User> getAll() {

        return userDao.findAll();
    }

    @Override
    public User getUser(Integer userId) {

        return userDao.get(userId);
    }

}
