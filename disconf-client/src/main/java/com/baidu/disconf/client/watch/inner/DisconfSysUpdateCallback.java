package com.baidu.disconf.client.watch.inner;

import com.baidu.disconf.client.common.inter.IDisconfSysUpdate;
import com.baidu.disconf.client.core.DisconfCoreMgr;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;

/**
 * 当配置更新时，系统会自动 调用此回调函数<br/>
 * 这个函数是系统调用的，当有配置更新时，便会进行回调
 * 
 * @author liaoqiqi
 * @version 2014-5-16
 */
public class DisconfSysUpdateCallback implements IDisconfSysUpdate {

    /**
     * 
     */
    @Override
    public void reload(DisConfigTypeEnum disConfigTypeEnum, String keyName)
            throws Exception {

        //
        // 配置文件更新
        //
        if (disConfigTypeEnum.equals(DisConfigTypeEnum.FILE)) {

            // 更新配置数据仓库
            DisconfCoreMgr.updateOneConfFile(keyName);

            // 调用用户的回调函数列表
            DisconfCoreMgr.callOneConfFile(keyName);

        } else {

            //
            // 配置项更新
            //

            // 更新配置数据仓库
            DisconfCoreMgr.updateOneConfItem(keyName);

            // 调用用户的回调函数列表
            DisconfCoreMgr.callOneConfItem(keyName);
        }

    }
}
