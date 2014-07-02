package com.baidu.disconf2.web.service.user.dto;

import java.io.Serializable;

import com.baidu.ub.common.generic.bo.BaseObject;

/**
 * 
 * @author liaoqiqi
 * @version 2013-11-26
 */
public class Visitor extends BaseObject<Integer> implements Serializable {

    private static final long serialVersionUID = 5621993194031128338L;

    //
    // uc's name
    //
    private String loginUserName;

    // role
    private int roleId;

    /**
     * @return the loginUserId
     */
    public Integer getLoginUserId() {
        return getId();
    }

    /**
     * @param loginUserId
     *            the loginUserId to set
     */
    public void setLoginUserId(Integer loginUserId) {
        setId(loginUserId);
    }

    /**
     * @return the loginUserName
     */
    public String getLoginUserName() {
        return loginUserName;
    }

    /**
     * @param loginUserName
     *            the loginUserName to set
     */
    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

}
