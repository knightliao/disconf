package com.baidu.disconf2.core;

import java.util.List;
import java.util.Map;

import com.baidu.disconf2.model.DisconfCenterFile;
import com.baidu.disconf2.model.DisconfCenterItem;

/**
 * 整体的配置服务中心
 * 
 * @author liaoqiqi
 * @version 2014-5-16
 */
public class DisconfCenterCore {

    // 配置文件
    // key: 配置文件名
    // value: 配置文件数据
    private static Map<String, DisconfCenterFile> confFile;

    // 配置Item
    // key: 配置项的Key
    // value: 配置项数据
    private static Map<String, DisconfCenterItem> confItem;

    // 主备切换时的Key列表
    private static List<String> activeBackupKeyList;

    /**
     * 
     * @param fileName
     * @param keyName
     * @return
     */
    public static Object getConfigFile(String fileName, String keyName) {
        return null;
    }

    /**
     * 
     * @param keyName
     * @return
     */
    public static Object getConfigItem(String keyName) {

        return null;
    }

}
