/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.disconf.web.service.role.service;

import java.util.List;

import com.baidu.disconf.web.service.role.bo.Role;

/**
 * @author weiwei
 * @date 2013-12-24
 */
public interface RoleMgr {

    public Role get(Integer roleId);

    public List<Role> findAll();

}
