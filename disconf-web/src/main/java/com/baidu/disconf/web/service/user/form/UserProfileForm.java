package com.baidu.disconf.web.service.user.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 新建用户表单
 *
 * @author chenhui
 * @version 2014-7-3
 */
public class UserProfileForm {
    private long userId;
    @NotNull(message = "user.password.empty")
    @NotEmpty(message = "user.password.empty")
    private String password;
    public final static String PASSWORD = "password";

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
