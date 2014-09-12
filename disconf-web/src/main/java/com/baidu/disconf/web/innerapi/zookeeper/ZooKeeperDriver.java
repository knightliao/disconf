package com.baidu.disconf.web.innerapi.zookeeper;

import java.nio.charset.Charset;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
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

                LOG.info(path + " not exist. not update ZK.");

            } else {
                //
                // 通知
                //
                ZookeeperMgr.getInstanceWithCheck().writePersistentUrl(path,
                        value);
            }

        } catch (Exception e) {

            LOG.error(e.toString(), e);
            throw new RemoteException("zk.notify.error", e);
        }
    }

    /**
     * 返回groupName结点向下的所有zookeeper信息
     * 
     * @param root
     */
    public List<String> getConf(String groupName) {

        ZookeeperMgr zooKeeperMgr = ZookeeperMgr.getInstanceWithCheck();
        ZooKeeper zooKeeper = zooKeeperMgr.getZk();

        List<String> retList = new ArrayList<String>();
        try {
            getConf(zooKeeper, groupName, groupName, retList);
        } catch (KeeperException e) {
            LOG.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
        return retList;
    }

    private static final Charset CHARSET = Charset.forName("UTF-8");

    private void getConf(ZooKeeper zk, String groupName, String displayName,
            List<String> retList) throws KeeperException, InterruptedException {
        try {

            StringBuffer sb = new StringBuffer();

            int pathLength = StringUtils.countMatches(groupName, "/");
            for (int i = 0; i < pathLength - 2; ++i) {
                sb.append("\t");
            }

            List<String> children = zk.getChildren(groupName, false);

            if (!"/".equals(groupName)) {

                sb.append("|----" + displayName);
                Stat stat = new Stat();
                byte[] data = zk.getData(groupName, null, stat);

                if (data != null && children.size() == 0) {
                    sb.append("\t" + new String(data, CHARSET));
                }
            } else {
                sb.append(groupName);
            }
            retList.add(sb.toString());

            //
            //
            //
            Collections.sort(children,
                    Collator.getInstance(java.util.Locale.CHINA));
            int i = 1;
            for (String child : children) {

                String nextName = "";

                if (!"/".equals(groupName)) {

                    nextName = groupName + "/" + child;

                } else {
                    nextName = groupName + "/" + child;
                }

                String node = StringUtils.substringAfterLast(nextName, "/");
                if (node.contains(".properties")) {
                    node = i + ": " + node;
                }

                getConf(zk, groupName + "/" + child, node, retList);

                ++i;
            }

        } catch (KeeperException.NoNodeException e) {
            LOG.info("Group %s does not exist\n", groupName);
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
