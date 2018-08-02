package com.baidu.disconf.web.service.user.service;

import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.disconf.web.service.user.form.UserAddForm;
import com.baidu.disconf.web.service.user.form.UserListForm;
import com.baidu.disconf.web.service.user.form.UserModifyForm;
import com.baidu.disconf.web.service.user.form.UserProfileForm;
import com.baidu.disconf.web.service.user.vo.UserListVo;
import com.baidu.disconf.web.service.user.vo.VisitorVo;
import com.baidu.ub.common.db.DaoPageResult;

/**
 * @author liaoqiqi
 * @version 2013-11-28
 */
public interface UserMgr {

    /**
     * 获取用户的基本信息（登录用户）
     *
     * @return
     */
    Visitor getVisitor(Long userId);

    VisitorVo getCurVisitor();

    UserListVo getUser(Long userId);
    User getUserByName(String name);

    /**
     * @return
     */
    Long create(UserAddForm userAddForm);


    boolean delete(Long userId);
    void update(UserModifyForm userModifyForm);
    void updateProfile(UserProfileForm userProfileForm);
    /**
     * @return
     */
    DaoPageResult<UserListVo> getUserList(UserListForm userListForm);
    /**
     * 为某个user添加一个app
     *
     * @param userId
     */
    String addOneAppForUser(Long userId, int appId);

    /**
     * 修改密码
     *
     * @param newPassword
     */
    void modifyPassword(Long userId, String newPassword);
}
