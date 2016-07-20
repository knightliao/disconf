package com.baidu.disconf.web.innerapi.zookeeper.impl;

import java.nio.charset.Charset;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.path.ZooPathMgr;
import com.baidu.disconf.core.common.zookeeper.ZookeeperMgr;
import com.baidu.disconf.web.innerapi.zookeeper.ZooKeeperDriver;
import com.baidu.disconf.web.service.zookeeper.config.ZooConfig;
import com.baidu.disconf.web.service.zookeeper.dto.ZkDisconfData;
import com.baidu.dsp.common.exception.RemoteException;

/**
 * Created by knightliao on 15/1/14.
 */
public class ZookeeperDriverImpl implements ZooKeeperDriver, InitializingBean, DisposableBean {

    protected static final Logger LOG = LoggerFactory.getLogger(ZookeeperDriverImpl.class);

    @Autowired
    private ZooConfig zooConfig;

    //
    // 是否初始化
    //
    private static boolean isInit = false;

    /**
     * 通知某个Node更新
     *
     * @param app
     * @param env
     * @param version
     * @param disConfigTypeEnum
     */
    @Override
    public void notifyNodeUpdate(String app, String env, String version, String key, String value,
                                 DisConfigTypeEnum disConfigTypeEnum) {

        //
        // 获取路径
        //
        String baseUrlString = ZooPathMgr.getZooBaseUrl(zooConfig.getZookeeperUrlPrefix(), app, env, version);

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
                ZookeeperMgr.getInstance().writePersistentUrl(path, value);
            }

        } catch (Exception e) {

            LOG.error(e.toString(), e);
            throw new RemoteException("zk.notify.error", e);
        }
    }

    /**
     * 获取分布式配置 Map
     *
     * @param app
     * @param env
     * @param version
     *
     * @return
     */
    @Override
    public Map<String, ZkDisconfData> getDisconfData(String app, String env, String version) {

        String baseUrl = ZooPathMgr.getZooBaseUrl(zooConfig.getZookeeperUrlPrefix(), app, env, version);

        Map<String, ZkDisconfData> fileMap = new HashMap<String, ZkDisconfData>();

        try {

            fileMap = getDisconfData(ZooPathMgr.getFileZooPath(baseUrl));
            Map<String, ZkDisconfData> itemMap = getDisconfData(ZooPathMgr.getItemZooPath(baseUrl));
            fileMap.putAll(itemMap);

        } catch (KeeperException e) {
            LOG.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }

        return fileMap;
    }

    /**
     * 获取分布式配置 Map
     *
     * @param app
     * @param env
     * @param version
     *
     * @return
     */
    @Override
    public ZkDisconfData getDisconfData(String app, String env, String version, DisConfigTypeEnum disConfigTypeEnum,
                                        String keyName) {

        String baseUrl = ZooPathMgr.getZooBaseUrl(zooConfig.getZookeeperUrlPrefix(), app, env, version);

        try {

            ZookeeperMgr zooKeeperMgr = ZookeeperMgr.getInstance();
            ZooKeeper zooKeeper = zooKeeperMgr.getZk();

            if (disConfigTypeEnum.equals(DisConfigTypeEnum.FILE)) {

                return getDisconfData(ZooPathMgr.getFileZooPath(baseUrl), keyName, zooKeeper);

            } else if (disConfigTypeEnum.equals(DisConfigTypeEnum.ITEM)) {

                return getDisconfData(ZooPathMgr.getItemZooPath(baseUrl), keyName, zooKeeper);
            }

        } catch (KeeperException e) {
            LOG.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 广度搜索法：搜索分布式配置对应的两层数据
     *
     * @return
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    private Map<String, ZkDisconfData> getDisconfData(String path) throws KeeperException, InterruptedException {

        Map<String, ZkDisconfData> ret = new HashMap<String, ZkDisconfData>();

        ZookeeperMgr zooKeeperMgr = ZookeeperMgr.getInstance();
        ZooKeeper zooKeeper = zooKeeperMgr.getZk();

        if (zooKeeper.exists(path, false) == null) {
            return ret;
        }

        List<String> children = zooKeeper.getChildren(path, false);
        for (String firstKey : children) {

            ZkDisconfData zkDisconfData = getDisconfData(path, firstKey, zooKeeper);
            if (zkDisconfData != null) {
                ret.put(firstKey, zkDisconfData);
            }
        }

        return ret;
    }

    /**
     * 获取指定 配置数据
     *
     * @return
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    private ZkDisconfData getDisconfData(String path, String keyName, ZooKeeper zooKeeper)
            throws KeeperException, InterruptedException {

        String curPath = path + "/" + keyName;

        if (zooKeeper.exists(curPath, false) == null) {
            return null;
        }

        ZkDisconfData zkDisconfData = new ZkDisconfData();
        zkDisconfData.setKey(keyName);

        List<String> secChiList = zooKeeper.getChildren(curPath, false);
        List<ZkDisconfData.ZkDisconfDataItem> zkDisconfDataItems = new ArrayList<ZkDisconfData.ZkDisconfDataItem>();

        // list
        for (String secKey : secChiList) {

            // machine
            ZkDisconfData.ZkDisconfDataItem zkDisconfDataItem = new ZkDisconfData.ZkDisconfDataItem();
            zkDisconfDataItem.setMachine(secKey);

            String thirdPath = curPath + "/" + secKey;

            // value
            byte[] data = zooKeeper.getData(thirdPath, null, null);
            if (data != null) {
                zkDisconfDataItem.setValue(new String(data, CHARSET));
            }

            // add
            zkDisconfDataItems.add(zkDisconfDataItem);
        }

        zkDisconfData.setData(zkDisconfDataItems);

        return zkDisconfData;
    }

    /**
     * 返回groupName结点向下的所有zookeeper信息
     *
     * @param
     */
    @Override
    public List<String> getConf(String groupName) {

        ZookeeperMgr zooKeeperMgr = ZookeeperMgr.getInstance();
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

    private void getConf(ZooKeeper zk, String groupName, String displayName, List<String> retList)
            throws KeeperException, InterruptedException {
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
            Collections.sort(children, Collator.getInstance(java.util.Locale.CHINA));
            for (String child : children) {

                String nextName = "";

                if (!"/".equals(groupName)) {

                    nextName = groupName + "/" + child;

                } else {
                    nextName = groupName + "/" + child;
                }

                String node = StringUtils.substringAfterLast(nextName, "/");

                getConf(zk, groupName + "/" + child, node, retList);
            }

        } catch (KeeperException.NoNodeException e) {
            LOG.error("Group " + groupName + " does not exist\n");
        }

    }

    @Override
    public void destroy() throws Exception {

        ZookeeperMgr.getInstance().release();
    }

    @Override
    public synchronized void afterPropertiesSet() throws Exception {

        if (!isInit) {

            ZookeeperMgr.getInstance().init(zooConfig.getZooHosts(), zooConfig.getZookeeperUrlPrefix(), false);
            isInit = true;
        }
    }
}
