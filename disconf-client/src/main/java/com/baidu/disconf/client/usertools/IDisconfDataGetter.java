package com.baidu.disconf.client.usertools;

import java.util.Map;

/**
 * Created by knightliao on 16/5/28.
 * <p/>
 * 配置数据获取总控
 */
public interface IDisconfDataGetter {

    /**
     * 根据 分布式配置文件 获取该配置文件的所有数据，以 map形式呈现
     *
     * @param fileName
     *
     * @return
     */
    Map<String, Object> getByFile(String fileName);

    /**
     * 获取 分布式配置文件 获取该配置文件 中 某个配置项 的值
     *
     * @param fileName
     * @param fileItem
     *
     * @return
     */
    Object getByFileItem(String fileName, String fileItem);

    /**
     * 根据 分布式配置 获取其值
     *
     * @param itemName
     *
     * @return
     */
    Object getByItem(String itemName);
}
