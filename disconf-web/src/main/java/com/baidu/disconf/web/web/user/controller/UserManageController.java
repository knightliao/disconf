package com.baidu.disconf.web.web.user.controller;

import com.baidu.disconf.web.service.user.form.UserAddForm;
import com.baidu.disconf.web.service.user.form.UserListForm;
import com.baidu.disconf.web.service.user.form.UserModifyForm;
import com.baidu.disconf.web.service.user.service.UserMgr;
import com.baidu.disconf.web.service.user.vo.UserListVo;
import com.baidu.dsp.common.annotation.NoAuth;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.vo.JsonObjectBase;
import com.baidu.ub.common.db.DaoPageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/user/manage")
public class UserManageController extends BaseController {

    protected static final Logger LOG = LoggerFactory.getLogger(UserManageController.class);

    @Autowired
    private UserMgr userMgr;

    @NoAuth
    @RequestMapping(value = "/get/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase getUserById(@PathVariable long userId,HttpServletRequest request) {
        UserListVo user = userMgr.getUser(userId);
        return buildSuccess(user);
    }
    @NoAuth
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase getUserList(UserListForm userListForm,HttpServletRequest request) {
        DaoPageResult<UserListVo> users = userMgr.getUserList(userListForm);

        return buildListSuccess(users);
    }

    @NoAuth
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase addUser(@Valid UserAddForm userAddForm,HttpServletRequest request) {
        long userId = userMgr.create(userAddForm);
        return buildSuccess("ok", "ok");
    }
    @NoAuth
    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonObjectBase deleteUser(@PathVariable long userId,HttpServletRequest request) {
        userMgr.delete(userId);
        return buildSuccess("ok", "ok");
    }
    @NoAuth
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase updateUser(@Valid UserModifyForm userModifyForm,HttpServletRequest request) {
        userMgr.update(userModifyForm);
        return buildSuccess("ok", "ok");
    }

}
