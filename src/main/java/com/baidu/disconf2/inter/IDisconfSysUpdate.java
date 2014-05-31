package com.baidu.disconf2.inter;

import java.util.List;

import com.baidu.disconf2.model.DisconfCenterCallbackObject;

/**
 * 配置更新 时系统来调用
 * 
 * @author liaoqiqi
 * @version 2014-5-20
 */
public interface IDisconfSysUpdate {

    /**
     * 回调函数
     * 
     * @param disConfNodeModel
     *            一个完整的配置表示
     * @throws Exception
     */
    public void reload(
            List<DisconfCenterCallbackObject> disconfCenterCallbackObjects)
            throws Exception;
}
