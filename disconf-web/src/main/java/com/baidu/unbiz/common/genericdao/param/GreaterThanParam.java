package com.baidu.unbiz.common.genericdao.param;

/**
 * 封装大于的参数
 *
 * @author Darwin(Tianxin)
 */
public class GreaterThanParam {
    Object value;

    public GreaterThanParam(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}