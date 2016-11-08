package com.baidu.disconf.web.service.user.service.impl;

import com.baidu.disconf.web.service.sign.utils.SignUtils;
import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dao.UserDao;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.disconf.web.service.user.service.UserInnerMgr;
import com.baidu.disconf.web.service.user.service.UserMgr;
import com.baidu.disconf.web.service.user.vo.VisitorVo;
import com.baidu.ub.common.commons.ThreadContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liaoqiqi
 * @version 2013-12-5
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserMgrImpl implements UserMgr {

    protected static final Logger LOG = LoggerFactory.getLogger(UserMgrImpl.class);

    @Autowired
    private UserInnerMgr userInnerMgr;

    @Autowired
    private UserDao userDao;

    @Override
    public Visitor getVisitor(Long userId) {

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
    public Long create(User user) {

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

    /**
     * @param userId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String addOneAppForUser(Long userId, int appId) {

        User user = getUser(userId);

        String ownAppIds = user.getOwnApps();
        //此处`admin`用户,并非指的是管理员,而是作为一个特殊的用户
        if ("admin".equals(user.getName()) && StringUtils.isNotBlank(ownAppIds)) {
            ownAppIds = StringUtils.EMPTY;
            user.setOwnApps(ownAppIds);
            userDao.update(user);
            return ownAppIds;
        }

        if (StringUtils.isBlank(ownAppIds)) {
            ownAppIds = String.valueOf(appId);
        } else {
            if (",".equals(ownAppIds.substring(ownAppIds.length() - 1))) {
                ownAppIds += appId;
            } else {
                ownAppIds += "," + appId;
            }
        }

        user.setOwnApps(ownAppIds);
        userDao.update(user);

        return ownAppIds;
    }

    /**
     * @param newPassword
     */
    @Override
    public void modifyPassword(Long userId, String newPassword) {

        String passwordWithSalt = SignUtils.createPassword(newPassword);

        User user = userDao.get(userId);
        user.setPassword(passwordWithSalt);

        userDao.update(user);
    }

    /**
     * @param userId
     *
     * @return
     */
    @Override
    public User getUser(Long userId) {

        return userDao.get(userId);
    }

}
