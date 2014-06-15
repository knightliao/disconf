package com.baidu.disconf2.core.test.zookeeper;

import java.util.List;

import org.apache.zookeeper.KeeperException;

import com.baidu.disconf2.core.common.zookeeper.inner.ConnectionWatcher;

public class DeleteGroup extends ConnectionWatcher {

    public void delete(String groupName) throws KeeperException,
            InterruptedException {

        String path = "/" + groupName;

        try {
            List<String> children = zk.getChildren(path, false);
            for (String child : children) {
                zk.delete(path + "/" + child, -1);
            }
            zk.delete(path, -1);
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist\n", groupName);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws Exception {

        DeleteGroup deleteGroup = new DeleteGroup();
        deleteGroup
                .connect("10.48.57.42:8581,10.48.57.42:8582,10.48.57.42:8583");
        deleteGroup.delete("disconf/dsp_demo_1_0_0_0_online/file");
        deleteGroup.delete("disconf/dsp_demo_1_0_0_0_online/item");
        deleteGroup.close();
    }
}
// ^^ DeleteGroup
