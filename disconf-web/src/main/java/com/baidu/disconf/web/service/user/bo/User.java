package com.baidu.disconf.web.service.user.bo;

import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.dao.DB;
import com.baidu.unbiz.common.genericdao.annotation.Column;
import com.baidu.unbiz.common.genericdao.annotation.Table;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * 用户表
 *
 * @author liaoqiqi
 * @version 2013-11-28
 */
@Table(db = DB.DB_NAME, name = "user", keyColumn = Columns.USER_ID)
public class User extends BaseObject<Long> {

    private static final long serialVersionUID = 1L;

    // 唯一
    @Column(value = Columns.NAME)
    private String name;

    // token
    @Column(value = Columns.TOKEN)
    private String token;

    // 密码
    @Column(value = Columns.PASSWORD)
    private String password;

    //
    @Column(value = Columns.OWNAPPS)
    private String ownApps;

    /**
     * 角色ID
     */
    @Column(value = Columns.ROLE_ID)
    private int roleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOwnApps() {
        return ownApps;
    }

    public void setOwnApps(String ownApps) {
        this.ownApps = ownApps;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "User{" +
                   "name='" + name + '\'' +
                   ", token='" + token + '\'' +
                   ", password='" + password + '\'' +
                   ", ownApps='" + ownApps + '\'' +
                   ", roleId=" + roleId +
                   '}';
    }
}
