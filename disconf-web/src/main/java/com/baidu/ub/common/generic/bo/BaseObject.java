/**
 * adx-common#com.baidu.ub.generic.bo.BaseObject.java
 * 下午6:46:03 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.generic.bo;

import java.io.Serializable;

/**
 * 所有bo的父类
 * 
 * @author Darwin(Tianxin)
 */
public class BaseObject<KEY extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    public BaseObject() {
    }

    /**
     * 主键字段
     */
    protected KEY id;

    public KEY getId() {
        return id;
    }

    public void setId(KEY id) {
        this.id = id;
    }

}
