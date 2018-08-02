package com.baidu.disconf.web.web.role.controller;

import com.baidu.disconf.web.service.role.bo.Role;
import com.baidu.disconf.web.service.role.service.RoleMgr;
import com.baidu.dsp.common.annotation.NoAuth;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.vo.JsonObjectBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author chenhui
 * @version 2016-1-29
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/role")
public class RoleController extends BaseController {

    protected static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleMgr roleMgr;

    /**
     * list
     *
     * @return
     */
    @NoAuth
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase list() {

        List<Role> roleList = roleMgr.findAll();

        return buildListSuccess(roleList, roleList.size());
    }

}
