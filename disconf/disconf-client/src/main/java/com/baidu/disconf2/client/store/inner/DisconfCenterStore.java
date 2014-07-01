package com.baidu.disconf2.client.store.inner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.common.model.DisconfCenterFile;
import com.baidu.disconf2.client.common.model.DisconfCenterItem;

/**
 * 
 * 配置仓库
 * 
 * @author liaoqiqi
 * @version 2014-6-9
 */
public class DisconfCenterStore {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfCenterStore.class);

    // 每个配置文件一条
    // key: 配置文件名
    // value: 配置文件数据
    private Map<String, DisconfCenterFile> confFileMap = new HashMap<String, DisconfCenterFile>();

    // 每个配置Item一条
    // key: 配置项的Key
    // value: 配置项数据
    private Map<String, DisconfCenterItem> confItemMap = new HashMap<String, DisconfCenterItem>();

    // 主备切换时的Key列表
    private List<String> activeBackupKeyList;

    // 标识本机器名
    private String machineName;

    /**
     * 存储 一个配置文件
     */
    public void storeOneFile(DisconfCenterFile disconfCenterFile) {

        String fileName = disconfCenterFile.getFileName();

        if (confFileMap.containsKey(fileName)) {

            LOGGER.error("There are two same fileName!!!! " + "first: "
                    + confFileMap.get(fileName).getCls().toString()
                    + ", Second: " + disconfCenterFile.getCls().toString());
        } else {
            confFileMap.put(fileName, disconfCenterFile);
        }
    }

    /**
     * 存储 一个配置项
     */
    public void storeOneItem(DisconfCenterItem disconfCenterItem) {
        String key = disconfCenterItem.getKey();

        if (confItemMap.containsKey(key)) {

            LOGGER.error("There are two same fileName!!!! " + "first: "
                    + confItemMap.get(key).getClass().toString() + ", Second: "
                    + disconfCenterItem.getClass().toString());
        } else {
            confItemMap.put(key, disconfCenterItem);
        }
    }

    public Map<String, DisconfCenterFile> getConfFileMap() {
        return confFileMap;
    }

    public Map<String, DisconfCenterItem> getConfItemMap() {
        return confItemMap;
    }

    public List<String> getActiveBackupKeyList() {
        return activeBackupKeyList;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }
}
