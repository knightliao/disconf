package com.baidu.disconf2.callback;

import java.util.List;

import com.baidu.disconf2.inter.IDisconfSysUpdate;
import com.baidu.disconf2.model.DisconfCenterCallbackObject;

/**
 * 当配置更新时，系统会自动 调用此回调函数<br/>
 * 这个函数是系统调用的，当有配置更新时，便会进行回调
 * 
 * @author liaoqiqi
 * @version 2014-5-16
 */
public class DisconfSysUpdateCallback implements IDisconfSysUpdate {

    /**
     * 这里要根据配置信息
     */
    public void reload(
            List<DisconfCenterCallbackObject> disconfCenterCallbackObjects)
            throws Exception {

    }

}
