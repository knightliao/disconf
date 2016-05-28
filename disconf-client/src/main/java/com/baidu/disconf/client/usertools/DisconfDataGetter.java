package com.baidu.disconf.client.usertools;

import java.util.Map;

import com.baidu.disconf.client.usertools.impl.DisconfDataGetterDefaultImpl;

/**
 * Created by knightliao on 16/5/28.
 */
public class DisconfDataGetter {

    private static IDisconfDataGetter iDisconfDataGetter = new DisconfDataGetterDefaultImpl();

    /**
     * 根据 分布式配置文件 获取该配置文件的所有数据，以 map形式呈现
     *
     * @param fileName
     *
     * @return
     */
    public static Map<String, Object> getByFile(String fileName) {
        return iDisconfDataGetter.getByFile(fileName);
    }

    /**
     * 获取 分布式配置文件 获取该配置文件 中 某个配置项 的值
     *
     * @param fileName
     * @param fileItem
     *
     * @return
     */
    public static Object getByFileItem(String fileName, String fileItem) {
        return iDisconfDataGetter.getByFileItem(fileName, fileItem);
    }

    /**
     * 根据 分布式配置 获取其值
     *
     * @param itemName
     *
     * @return
     */
    public static Object getByItem(String itemName) {
        return iDisconfDataGetter.getByItem(itemName);
    }
}
