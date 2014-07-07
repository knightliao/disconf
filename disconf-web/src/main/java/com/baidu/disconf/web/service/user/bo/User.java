package com.baidu.disconf.web.service.user.bo;

import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.dao.DB;
import com.baidu.ub.common.generic.annotation.Column;
import com.baidu.ub.common.generic.annotation.Table;
import com.baidu.ub.common.generic.bo.BaseObject;

/**
 * 用户表
 * 
 * @author liaoqiqi
 * @version 2013-11-28
 */
@Table(db = DB.DB_NAME, name = "user", keyColumn = Columns.USER_ID)
public class User extends BaseObject<Integer> {

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

    @Override
    public String toString() {
        return "User [name=" + name + ", token=" + token + ", password="
                + password + "]";
    }

}
