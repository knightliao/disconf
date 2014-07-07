package com.baidu.disconf.core.test.zookeeper;

import java.util.List;

import org.apache.zookeeper.KeeperException;

import com.baidu.disconf.core.common.zookeeper.inner.ConnectionWatcher;

/**
 * 
 * 递归进行删除
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class DeleteGroup extends ConnectionWatcher {

    public static String hosts = "10.48.57.42:8581,10.48.57.42:8582,10.48.57.42:8583";

    public void delete(String groupName) throws KeeperException,
            InterruptedException {

        String path = groupName;

        try {
            List<String> children = zk.getChildren(path, false);
            if (children.size() == 0) {
                System.out.println("delete: " + path);
                zk.delete(path, -1);
                return;
            }

            for (String child : children) {
                delete(path + "/" + child);
            }

        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist\n", groupName);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws Exception {

        DeleteGroup deleteGroup = new DeleteGroup();
        deleteGroup.connect(hosts);
        deleteGroup.delete("/disconf");
        deleteGroup.close();
    }
}
// ^^ DeleteGroup
