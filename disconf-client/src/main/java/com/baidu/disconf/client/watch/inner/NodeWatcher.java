package com.baidu.disconf.client.watch.inner;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.config.inner.DisClientComConfig;
import com.baidu.disconf.client.core.processor.DisconfCoreProcessor;
import com.baidu.disconf.core.common.constants.Constants;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.path.ZooPathMgr;
import com.baidu.disconf.core.common.zookeeper.ZookeeperMgr;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 结点监控器
 *
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class NodeWatcher implements Watcher {

    protected static final Logger LOGGER = LoggerFactory.getLogger(NodeWatcher.class);

    private String monitorPath = "";
    private String keyName = "";
    private DisConfigTypeEnum disConfigTypeEnum;
    private DisconfSysUpdateCallback disconfSysUpdateCallback;
    private boolean debug;
    private String nodeChilName;

    private DisconfCoreProcessor disconfCoreMgr;

    /**
     */
    public NodeWatcher(DisconfCoreProcessor disconfCoreMgr, String monitorPath, String keyName,
                       DisConfigTypeEnum disConfigTypeEnum, DisconfSysUpdateCallback disconfSysUpdateCallback,
                       boolean debug) {

        super();
        this.debug = debug;
        this.disconfCoreMgr = disconfCoreMgr;
        this.monitorPath = monitorPath;
        this.keyName = keyName;
        this.disConfigTypeEnum = disConfigTypeEnum;
        this.disconfSysUpdateCallback = disconfSysUpdateCallback;
    }
    
    
    /**
     */
    public NodeWatcher(DisconfCoreProcessor disconfCoreMgr, String monitorPath, String keyName,
                       DisConfigTypeEnum disConfigTypeEnum, DisconfSysUpdateCallback disconfSysUpdateCallback,
                       boolean debug,String nodeChilName) {

        super();
        this.debug = debug;
        this.disconfCoreMgr = disconfCoreMgr;
        this.monitorPath = monitorPath;
        this.keyName = keyName;
        this.disConfigTypeEnum = disConfigTypeEnum;
        this.disconfSysUpdateCallback = disconfSysUpdateCallback;
        this.nodeChilName = nodeChilName;
    }

    /**
     */
    public void monitorMaster() {

        //
        // 监控
        //
        Stat stat = new Stat();
        try {

            ZookeeperMgr.getInstance().read(monitorPath, this, stat);

        } catch (InterruptedException e) {

            LOGGER.info(e.toString());

        } catch (KeeperException e) {
            LOGGER.error("cannot monitor " + monitorPath, e);
        }

        LOGGER.debug("monitor path: (" + monitorPath + "," + keyName + "," + disConfigTypeEnum.getModelName() +
                ") has been added!");
    }

    /**
     * 回调函数
     */
    @Override
    public void process(WatchedEvent event) {

        //
        // 结点更新时
        //
    	LOGGER.info("eventType:"+event.getType());
        if (event.getType() == EventType.NodeDataChanged) {
        	
            try {

                LOGGER.info("============GOT UPDATE EVENT " + event.toString() + ": (" + monitorPath + "," + keyName
                        + "," + disConfigTypeEnum.getModelName() + ")======================");

                // 调用回调函数, 回调函数里会重新进行监控
                callback(event);

            } catch (Exception e) {

                LOGGER.error("monitor node exception. " + monitorPath, e);
            }
        }

        //
        // 结点断开连接，这时不要进行处理
        //
        if (event.getState() == KeeperState.Disconnected) {

            if (!debug) {
                LOGGER.warn("============GOT Disconnected EVENT " + event.toString() + ": (" + monitorPath + ","
                        + keyName + "," + disConfigTypeEnum.getModelName() + ")======================");
            } else {
                LOGGER.debug("============DEBUG MODE: GOT Disconnected EVENT " + event.toString() + ": (" +
                        monitorPath +
                        "," +
                        keyName +
                        "," + disConfigTypeEnum.getModelName() + ")======================");
            }
        }

        //
        // session expired，需要重新关注哦
        //
        if (event.getState() == KeeperState.Expired) {

            if (!debug) {

                LOGGER.error("============GOT Expired  " + event.toString() + ": (" + monitorPath + "," + keyName
                        + "," + disConfigTypeEnum.getModelName() + ")======================");

                // 重新连接
                ZookeeperMgr.getInstance().reconnect();

                callback();
            } else {
                LOGGER.debug("============DEBUG MODE: GOT Expired  " + event.toString() + ": (" + monitorPath + ","
                        + "" + keyName + "," + disConfigTypeEnum.getModelName() + ")======================");
            }
        }
    }

    /**
     *
     */
    private void callback() {

        try {

            // 调用回调函数, 回调函数里会重新进行监控
            try {
                disconfSysUpdateCallback.reload(disconfCoreMgr, disConfigTypeEnum, keyName);
            } catch (Exception e) {
                LOGGER.error(e.toString(), e);
            }

        } catch (Exception e) {

            LOGGER.error("monitor node exception. " + monitorPath, e);
        }
    }
    
    
    private void callback(WatchedEvent event) {

        try {
        	
        	ZookeeperMgr zookeeperMgr = ZookeeperMgr.getInstance();
        	
        	String nodeName =  DisClientComConfig.getInstance().getInstanceFingerprint();
        	
        	if(nodeName!=null){
        	  String path = ZooPathMgr.joinPath(event.getPath(), nodeName);
              Boolean flag = zookeeperMgr.exists(path);
        	 
        	  if(flag == true){
        		  
        		  //判断本次更新 自己是否在更新目录里面。
        		  byte [] data = ZookeeperMgr.getInstance().getZk().getData(path,null, null); //不监控
       			  String s = new String(data);
       			  Gson gson = new Gson();
       			  JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
       			  
       			  JsonElement  jsonElement =  jsonObject.get(Constants.NODE_UPDATE_FLAG);
       			  String status = jsonElement.getAsString();
       			  
       			  LOGGER.info("nodeChilName="+ nodeChilName +",nodeName="+nodeName +", sataus="+ status+",data="+jsonObject );
        		  
       			  if(status.equals(Constants.STATUS_UPDATE)){
       				  
       				  // 调用回调函数, 回调函数里会重新进行监控
       				  try {
       					  disconfSysUpdateCallback.reload(disconfCoreMgr, disConfigTypeEnum, keyName);
       					  
       					  //回调完成后修改自己的状态为 初始状态，等待下次更新
       					  LOGGER.info("回调成功，更新节点为初始状态，等待下次更新");
       					 
       					  
       				  } catch (Exception e) {
       					  LOGGER.error(e.toString(), e);
       				  }
       			  }else{
       				  this.monitorMaster();  //如果本次更新没有自己重新监控
       			  }
        		  
        	  }
        	}

        } catch (Exception e) {

            LOGGER.error("monitor node exception. " + monitorPath, e);
        }
    }
}
