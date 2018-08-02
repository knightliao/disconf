package com.baidu.disconf.web.service.user.service.impl;

import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.service.AppMgr;
import com.baidu.disconf.web.service.role.bo.Role;
import com.baidu.disconf.web.service.role.service.RoleMgr;
import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dao.UserDao;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.disconf.web.service.user.form.UserAddForm;
import com.baidu.disconf.web.service.user.form.UserListForm;
import com.baidu.disconf.web.service.user.form.UserModifyForm;
import com.baidu.disconf.web.service.user.form.UserProfileForm;
import com.baidu.disconf.web.service.user.service.UserInnerMgr;
import com.baidu.disconf.web.service.user.service.UserMgr;
import com.baidu.disconf.web.service.user.vo.UserListVo;
import com.baidu.disconf.web.service.user.vo.VisitorVo;
import com.baidu.disconf.web.service.sign.utils.SignUtils;
import com.baidu.disconf.web.utils.SHAUtil;
import com.baidu.dsp.common.exception.FieldException;
import com.baidu.dsp.common.utils.DataTransfer;
import com.baidu.dsp.common.utils.ServiceUtil;
import com.baidu.ub.common.commons.ThreadContext;
import com.baidu.ub.common.db.DaoPageResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    private RoleMgr roleMgr;
    @Autowired
    private AppMgr appMgr;

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
        visitorVo.setRole(visitor.getRoleName());
        return visitorVo;
    }

    /**
     * 创建
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Long create(UserAddForm userAddForm) {
        User userTemp = getUserByName(userAddForm.getName());
        if (userTemp != null) {
            throw new FieldException(userAddForm.NAME, "user.exist", null);
        }
        User user = new User();
        user.setName(userAddForm.getName());
        try {
            user.setPassword(SHAUtil.shaEncode(userAddForm.getPassword()));
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            throw new FieldException(userAddForm.PASSWORD, "password.error", null);
        }
        user.setOwnApps(userAddForm.getOwnApps());
        user.setRoleId(userAddForm.getRoleId());
        user = userDao.create(user);
        return user.getId();
    }

    /**
     * 刪除
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean delete(Long userId) {
        return userDao.delete(userId);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(UserModifyForm userForm) {
        userDao.updateByUserForm(userForm);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateProfile(UserProfileForm userProfileForm) {
        User user = new User();
        user.setId(userProfileForm.getUserId());
        try {
            user.setPassword(SHAUtil.shaEncode(userProfileForm.getPassword()));
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            throw new FieldException(userProfileForm.PASSWORD, "password.error", null);
        }
        userDao.updateUserProfile(user);
    }
    @Override
    public DaoPageResult<UserListVo> getUserList(UserListForm userListForm) {

        final DaoPageResult<User> userList =  userDao.getUserList(userListForm.getPage());
        //
        // 进行转换
        //
        DaoPageResult<UserListVo> userListVo =
                ServiceUtil.getResult(userList, new DataTransfer<User, UserListVo>() {

                    @Override
                    public UserListVo transfer(User input) {
                        UserListVo userListVoTemp = new UserListVo();
                        userListVoTemp.setId(input.getId());
                        userListVoTemp.setName(input.getName());
                        userListVoTemp.setRoleId(input.getRoleId());
                        Role role = roleMgr.get(input.getRoleId());
                        if (role != null){
                            userListVoTemp.setRole(role.getRoleName());
                        }
                        if(StringUtils.isNotEmpty(input.getOwnApps())){
                            List<String> appNameList = new ArrayList();
                            List<Long> appIdList = new ArrayList<Long>();
                            String[] appIdArr = input.getOwnApps().split(",");
                            for(String appId:appIdArr){
                                App app = appMgr.getById(Long.parseLong(appId));
                                if(app!=null) {
                                    appIdList.add(app.getId());
                                    appNameList.add(app.getName());
                                }
                            }
                            userListVoTemp.setOwnAppIdList(appIdList);
                            userListVoTemp.setOwnAppList(appNameList);
                        }

                        return userListVoTemp;
                    }
                });
        return userListVo;
    }

    @Override
    public UserListVo getUser(Long userId) {
        User user = userDao.get(userId);
        UserListVo userListVo = new UserListVo();
        userListVo.setId(user.getId());
        userListVo.setName(user.getName());
        userListVo.setRoleId(user.getRoleId());
        Role role = roleMgr.get(user.getRoleId());
        if (role != null){
            userListVo.setRole(role.getRoleName());
        }
        if(StringUtils.isNotEmpty(user.getOwnApps())){
            List<String> appNameList = new ArrayList();
            List<Long> appIdList = new ArrayList<Long>();
            String[] appIdArr = user.getOwnApps().split(",");
            for(String appId:appIdArr){
                App app = appMgr.getById(Long.parseLong(appId));
                if(app!=null) {
                    appIdList.add(app.getId());
                    appNameList.add(app.getName());
                }
            }
            userListVo.setOwnAppIdList(appIdList);
            userListVo.setOwnAppList(appNameList);
        }

        return userListVo;
    }
    /**
     * 根据 用户名获取用户信息
     */
    @Override
    public User getUserByName(String name) {

        return userDao.getUserByName(name);
    }
    /**
     * @param userId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String addOneAppForUser(Long userId, int appId) {

        User user = userDao.get(userId);
        String ownAppIds = user.getOwnApps();
        if (ownAppIds.contains(",")) {
            ownAppIds = ownAppIds + "," + appId;

        } else {
            ownAppIds = String.valueOf(appId);
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

}
