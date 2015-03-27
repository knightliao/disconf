package com.baidu.unbiz.common.genericdao.param;

/**
 * 封装between参数
 *
 * @author Darwin(Tianxin)
 */
public class BetweenParam {
    Object start;
    Object end;

    public BetweenParam(Object start, Object end) {
        this.start = start;
        this.end = end;
    }

    /**
     * @return the start
     */
    public Object getStart() {
        return start;
    }

    /**
     * @return the end
     */
    public Object getEnd() {
        return end;
    }

}