package com.baidu.unbiz.common.genericdao.param;

/**
 * 封装小于的参数
 *
 * @author Darwin(Tianxin)
 */
public class LessThanParam {
    Object value;

    public LessThanParam(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}