package com.baidu.disconf2.client.core;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.common.model.DisconfCenterFile;
import com.baidu.disconf2.client.fetcher.FetcherMgr;
import com.baidu.disconf2.client.store.DisconfStoreMgr;
import com.baidu.utils.ConfigLoaderUtils;

/**
 * 管理 下载、注入、Watch三模块
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class DisconfCoreMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfCoreMgr.class);

    /**
     * 1. 获取远程的所有配置数据<br/>
     * 2. 注入到仓库中，并注入到CONF实体中
     */
    public static void run() {

        //
        // 处理配置文件
        //
        processConfFile();
    }

    /**
     * 处理配置文件
     */
    private static void processConfFile() {

        Map<String, DisconfCenterFile> disConfCenterFileMap = DisconfStoreMgr
                .getInstance().getConfFileMap();

        /**
         * 配置文件列表处理
         */
        for (String fileName : disConfCenterFileMap.keySet()) {

            LOGGER.info("==============\tstart to process disconf file: "
                    + fileName + "\t=============================");

            DisconfCenterFile disconfCenterFile = disConfCenterFileMap
                    .get(fileName);

            String url = disconfCenterFile.getRemoteServerUrl();

            //
            // 下载配置
            //
            String filePath = "";
            Properties properties = null;
            try {

                filePath = FetcherMgr.downloadFileFromServer(url, fileName);

                // 读取配置
                properties = ConfigLoaderUtils.loadConfig(filePath);

            } catch (Exception e) {
                LOGGER.error("cannot use remote configuration: " + fileName, e);
                continue;
            }
            LOGGER.info("download ok.");

            //
            // 注入到仓库中
            //
            Set<Entry<Object, Object>> set = properties.entrySet();
            for (Entry<Object, Object> entry : set) {
                LOGGER.info(entry.toString());
            }
            DisconfStoreMgr.getInstance()
                    .injectFile2Store(fileName, properties);
            LOGGER.info("inject ok.");
        }

    }
}
