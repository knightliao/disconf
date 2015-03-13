package com.baidu.unbiz.common.genericdao.param;

/**
 * 反向参数
 *
 * @author Darwin(Tianxin)
 */
public class NotParam {
    Object value;

    public NotParam(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}