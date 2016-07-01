package com.baidu.disconf.client.store.inner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.model.DisconfCenterBaseModel;
import com.baidu.disconf.client.common.model.DisconfCenterFile;
import com.baidu.disconf.client.common.model.DisconfCenterItem;
import com.baidu.disconf.client.common.update.IDisconfUpdatePipeline;

/**
 * 配置仓库,是个单例
 *
 * @author liaoqiqi
 * @version 2014-6-9
 */
public class DisconfCenterStore {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfCenterStore.class);

    private DisconfCenterStore() {

    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static DisconfCenterStore instance = new DisconfCenterStore();
    }

    public static DisconfCenterStore getInstance() {
        return SingletonHolder.instance;
    }

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

    //
    private IDisconfUpdatePipeline iDisconfUpdatePipeline = null;

    // 标识本机器名
    private String machineName;

    /**
     * 存储 一个配置文件
     */
    public void storeOneFile(DisconfCenterBaseModel disconfCenterBaseModel) {

        DisconfCenterFile disconfCenterFile = (DisconfCenterFile) disconfCenterBaseModel;

        String fileName = disconfCenterFile.getFileName();

        if (confFileMap.containsKey(fileName)) {

            LOGGER.warn("There are two same fileName key!!!! " + fileName);
            DisconfCenterFile existCenterFile = confFileMap.get(fileName);

            // 如果是 同时使用了 注解式 和 非注解式 两种方式，则当修改时也要 进行 XML 式 reload
            if (disconfCenterFile.isTaggedWithNonAnnotationFile()) {
                existCenterFile.setIsTaggedWithNonAnnotationFile(true);
            }

        } else {
            confFileMap.put(fileName, disconfCenterFile);
        }
    }

    /**
     * 存储 一个配置项
     */
    public void storeOneItem(DisconfCenterBaseModel disconfCenterBaseModel) {

        DisconfCenterItem disconfCenterItem = (DisconfCenterItem) disconfCenterBaseModel;

        String key = disconfCenterItem.getKey();

        if (confItemMap.containsKey(key)) {

            LOGGER.error("There are two same item key!!!! " + "first: " + confItemMap.get(key).getClass().toString() +
                    ", Second: " + disconfCenterItem.getClass().toString());
        } else {
            confItemMap.put(key, disconfCenterItem);
        }
    }

    /**
     * 删除一个配置项
     */
    public void excludeOneItem(String key) {

        if (confItemMap.containsKey(key)) {
            confItemMap.remove(key);
        }
    }

    /**
     * 删除一个配置文件
     */
    public void excludeOneFile(String key) {

        if (confFileMap.containsKey(key)) {
            confFileMap.remove(key);
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

    public IDisconfUpdatePipeline getiDisconfUpdatePipeline() {
        return iDisconfUpdatePipeline;
    }

    public void setiDisconfUpdatePipeline(
            IDisconfUpdatePipeline iDisconfUpdatePipeline) {
        this.iDisconfUpdatePipeline = iDisconfUpdatePipeline;
    }
}
