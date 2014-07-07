package com.baidu.disconf.web.innerapi.zookeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.path.ZooPathMgr;
import com.baidu.disconf.core.common.zookeeper.ZookeeperMgr;
import com.baidu.disconf.web.service.zookeeper.config.ZooConfig;
import com.baidu.dsp.common.exception.RemoteException;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-24
 */
@Service
public class ZooKeeperDriver implements InitializingBean, DisposableBean {

    protected static final Logger LOG = LoggerFactory
            .getLogger(ZooKeeperDriver.class);

    @Autowired
    private ZooConfig zooConfig;

    /**
     * 通知某个Node更新
     * 
     * @param app
     * @param env
     * @param version
     * @param disConfigTypeEnum
     */
    public void notifyNodeUpdate(String app, String env, String version,
            String key, String value, DisConfigTypeEnum disConfigTypeEnum) {

        //
        // 获取路径
        //
        String baseUrlString = ZooPathMgr.getZooBaseUrl(
                zooConfig.getZookeeperUrlPrefix(), app, env, version);

        String path = "";
        if (disConfigTypeEnum.equals(DisConfigTypeEnum.ITEM)) {

            path = ZooPathMgr.getItemZooPath(baseUrlString);
        } else {
            path = ZooPathMgr.getFileZooPath(baseUrlString);
        }

        try {

            path = ZooPathMgr.joinPath(path, key);

            boolean isExist = ZookeeperMgr.getInstance().exists(path);
            if (!isExist) {
                LOG.error(path + " not exist.");
                throw new RemoteException("zk.notify.error");
            }

            //
            // 通知
            //
            ZookeeperMgr.getInstance().writePersistentUrl(path, value);

        } catch (Exception e) {

            LOG.error(e.toString(), e);
            throw new RemoteException("zk.notify.error", e);
        }
    }

    @Override
    public void destroy() throws Exception {

        ZookeeperMgr.getInstance().release();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        ZookeeperMgr.getInstance().init(zooConfig.getZooHosts(),
                zooConfig.getZookeeperUrlPrefix());
    }
}
