package com.baidu.disconf.web.service.user.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 新建用户表单
 *
 * @author chenhui
 * @version 2014-7-3
 */
public class UserModifyForm {
    private long userId;
    @NotNull(message = "user.name.empty")
    @NotEmpty(message = "user.name.empty")
    private String name;
    public final static String NAME = "name";

    private String ownApps;

    @NotNull(message = "role.empty")
    private Integer roleId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnApps() {
        return ownApps;
    }

    public void setOwnApps(String ownApps) {
        this.ownApps = ownApps;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
