package com.baidu.unbiz.common.genericdao.param;

/**
 * 加法参数
 */
public class IncrParam {

    Number value;

    public IncrParam(Number value) {
        this.value = value;
    }

    public Number getValue() {
        return value;
    }
}