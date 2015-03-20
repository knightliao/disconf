package com.baidu.disconf.client.store;

import java.util.List;
import java.util.Set;

import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterBaseModel;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.baidu.disconf.client.store.processor.model.DisconfValue;

/**
 * 仓库算子
 *
 * @author liaoqiqi
 * @version 2014-8-4
 */
public interface DisconfStoreProcessor {

    /**
     * 获取指定配置Data
     *
     * @return
     */
    DisconfCenterBaseModel getConfData(String key);

    /**
     * 获取所有配置Key列表
     *
     * @return
     */
    Set<String> getConfKeySet();

    /**
     * 增加回调函数
     *
     * @param iDisconfUpdateList
     */
    void addUpdateCallbackList(String keyName, List<IDisconfUpdate> iDisconfUpdateList);

    /**
     * 获取回调函数列表
     *
     * @return
     */
    List<IDisconfUpdate> getUpdateCallbackList(String keyName);

    /**
     * 获取配置的通用数据结构
     *
     * @return
     */
    DisConfCommonModel getCommonModel(String keyName);

    /**
     * 是否有这个配置
     *
     * @return
     */
    boolean hasThisConf(String keyName);

    /**
     * 将对象object中的数据注入配置中
     */
    void inject2Instance(Object object, String keyName);

    /**
     * 当是配置文件时，有两个参数<br/>
     * 当是配置项时，只有一个参数 ，第二个参数忽略
     *
     * @param fileName
     * @param keyName
     *
     * @return
     */
    Object getConfig(String fileName, String keyName);

    /**
     * 将配置数据注入到仓库
     *
     * @param fileName
     */
    void inject2Store(String fileName, DisconfValue disconfValue);

    /**
     * 批量添加配置
     */
    void transformScanData(List<DisconfCenterBaseModel> disconfCenterBaseModels);

    /**
     * 添加配置
     *
     * @param disconfCenterBaseModel
     */
    public void transformScanData(DisconfCenterBaseModel disconfCenterBaseModel);

    /**
     * 获取配置仓库的表示
     *
     * @return
     */
    String confToString();

    /**
     * 排除某个配置
     */
    void exclude(Set<String> keySet);
}
