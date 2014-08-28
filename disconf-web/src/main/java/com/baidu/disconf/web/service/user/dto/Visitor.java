package com.baidu.disconf.web.service.user.dto;

import java.io.Serializable;

import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * 
 * @author liaoqiqi
 * @version 2013-11-26
 */
public class Visitor extends BaseObject<Long> implements Serializable {

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
    public Long getLoginUserId() {
        return getId();
    }

    /**
     * @param loginUserId
     *            the loginUserId to set
     */
    public void setLoginUserId(Long loginUserId) {
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
