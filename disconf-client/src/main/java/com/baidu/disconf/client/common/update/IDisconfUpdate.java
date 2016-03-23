package com.baidu.disconf.client.common.update;

/**
 * 当配置更新 时，用户可以实现此接口，用以来实现回调函数
 *
 * @author liaoqiqi
 * @version 2014-5-20
 */
public interface IDisconfUpdate {

    void reload() throws Exception;
}
