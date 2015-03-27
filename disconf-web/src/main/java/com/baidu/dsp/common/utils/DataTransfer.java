package com.baidu.dsp.common.utils;

/**
 * 数据转接接口
 *
 * @author liaoqiqi
 * @version 2014-2-20
 */
public interface DataTransfer<ENTITYFROM, ENTITYTO> {

    /**
     * 转换规则定义
     *
     * @param inputList
     *
     * @return
     */
    public ENTITYTO transfer(ENTITYFROM input);
}
