package com.baidu.unbiz.common.genericdao.bo;

/**
 * @version create on 2014年8月13日 下午5:39:32
 */
public enum InsertOption {

    LOW_PRIORITY, DELAYED, HIGH_PRIORITY, IGNORE;

    public String toString() {
        return this.name().toLowerCase();
    }

}
