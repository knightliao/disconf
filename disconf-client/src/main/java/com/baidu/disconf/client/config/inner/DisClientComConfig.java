package com.baidu.disconf.client.config.inner;

import com.baidu.disconf.client.common.model.InstanceFingerprint;
import com.baidu.disconf.core.common.utils.ZooUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.UUID;

/**
 * 一些通用的数据
 *
 * @author liaoqiqi
 * @version 2014-7-1
 */
public class DisClientComConfig {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisClientComConfig.class);

    protected static final DisClientComConfig INSTANCE = new DisClientComConfig();

    public static DisClientComConfig getInstance() {
        return INSTANCE;
    }

    private DisClientComConfig() {
    }

    /**
     * 初始化实例指纹<br/>
     * 以IP和PORT为指紋，如果找不到则以本地IP为指纹
     */
    private synchronized void initInstanceFingerprint() {
        // 单例
        if (instanceFingerprint != null) {
            return;
        }
        Properties properties = System.getProperties();

        int port = 0;

        String VCAP_APP_HOST = properties.getProperty("VCAP_APP_HOST");
        if (VCAP_APP_HOST == null) {
            VCAP_APP_HOST = System.getenv("VCAP_APP_HOST");
        }

        // get host
        String host = ZooUtils.getNonLocalIp(VCAP_APP_HOST);
        if (host == null) {

            InetAddress addr;
            try {
                addr = InetAddress.getLocalHost();
                host = addr.getHostName();
            } catch (UnknownHostException e) {
                LOGGER.info("");
            }
        } else {
            // get port
            try {
                port = Integer.parseInt(VCAP_APP_HOST);
            } catch (Exception e) {
                LOGGER.info("");
            }
        }

        instanceFingerprint = new InstanceFingerprint(host, port, UUID.randomUUID().toString());
        // 初始化后打印指纹信息
        LOGGER.info("Finer print: " + DisClientComConfig.getInstance().getInstanceFingerprint());
    }

    private InstanceFingerprint instanceFingerprint;

    /**
     * 获取指纹
     */
    public String getInstanceFingerprint() {
        // 单例
        if (instanceFingerprint == null) {
            initInstanceFingerprint();
        }
        return instanceFingerprint.getHost() + "_" + String.valueOf(instanceFingerprint.getPort()) + "_" + instanceFingerprint.getUuid();
    }
}
