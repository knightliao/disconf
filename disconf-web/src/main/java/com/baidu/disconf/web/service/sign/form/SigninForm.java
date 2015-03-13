package com.baidu.disconf.web.service.sign.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.baidu.dsp.common.form.RequestFormBase;

/**
 * 登录
 *
 * @author liaoqiqi
 * @version 2014-2-5
 */
public class SigninForm extends RequestFormBase {

    /**
     *
     */
    private static final long serialVersionUID = 565717265045704403L;

    /**
     * 姓名
     */
    @NotNull(message = "name.empty")
    @NotEmpty(message = "name.empty")
    private String name;
    public static final String Name = "name";

    /**
     * 密码
     */
    @NotEmpty(message = "password.empty")
    @NotNull(message = "password.empty")
    private String password;
    public static final String PASSWORD = "password";

    /**
     * 记住自己 0：不记住 1记住
     */
    @NotNull
    @Range(min = 0, max = 1)
    private Integer remember;
    public static final String REMEMBER = "remember";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRemember() {
        return remember;
    }

    public void setRemember(Integer remember) {
        this.remember = remember;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
