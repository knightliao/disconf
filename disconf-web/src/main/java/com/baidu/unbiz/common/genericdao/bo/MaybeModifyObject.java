package com.baidu.unbiz.common.genericdao.bo;

import java.io.Serializable;
import java.util.Date;

import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * 可能发生修改行为的对象
 */
public abstract class MaybeModifyObject<KEY extends Serializable> extends BaseObject<KEY> {

    private static final long serialVersionUID = 1L;

    /**
     * 创建的user
     */
    protected int addUser;

    /**
     * 修改的user
     */
    protected int modUser;

    /**
     * 创建时间
     */
    protected Date addTime;

    /**
     * 修改时间
     */
    protected Date modTime;

    public int getAddUser() {
        return addUser;
    }

    public void setAddUser(int addUser) {
        this.addUser = addUser;
    }

    public int getModUser() {
        return modUser;
    }

    public void setModUser(int modUser) {
        this.modUser = modUser;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getModTime() {
        return modTime;
    }

    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }
}
