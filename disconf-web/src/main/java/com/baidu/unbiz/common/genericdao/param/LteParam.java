package com.baidu.unbiz.common.genericdao.param;

/**
 * 封装小于等於的参数
 *
 * @author Darwin(Tianxin)
 */
public class LteParam {
    Object value;

    public LteParam(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}