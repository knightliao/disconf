package com.baidu.disconf.core.common.zookeeper.inner;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZK打印
 *
 * @author liaoqiqi
 * @version 2014-7-7
 */
public class PrintZookeeperTree extends ConnectionWatcher {

    protected static final Logger LOGGER = LoggerFactory.getLogger(PrintZookeeperTree.class);

    private static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * @param
     */
    public PrintZookeeperTree() {
        super(true);
    }

    public void list(String groupName) throws KeeperException, InterruptedException {

        try {

            StringBuffer sb = new StringBuffer();

            int pathLength = StringUtils.countMatches(groupName, "/");
            for (int i = 0; i < pathLength - 1; ++i) {
                sb.append("\t");
            }

            if (groupName != "/") {
                String node = StringUtils.substringAfterLast(groupName, "/");
                sb.append("|----" + node);
                Stat stat = new Stat();
                byte[] data = zk.getData(groupName, null, stat);

                if (data != null) {
                    sb.append("\t" + new String(data, CHARSET));
                }
                if (stat != null) {
                    sb.append("\t" + stat.getEphemeralOwner());
                }
            } else {
                sb.append(groupName);
            }

            System.out.println(sb.toString());

            List<String> children = zk.getChildren(groupName, false);
            for (String child : children) {
                if (groupName != "/") {
                    list(groupName + "/" + child);
                } else {
                    list(groupName + child);
                }
            }

        } catch (KeeperException.NoNodeException e) {
            LOGGER.info("Group %s does not exist\n", groupName);
        }
    }

    public static void main(String[] args) throws Exception {

        if (args == null || args.length != 1) {
            LOGGER.error("PrintZookeeperTree argu error!");
            System.exit(2);
        }

        PrintZookeeperTree printZookeeperTree = new PrintZookeeperTree();
        printZookeeperTree.connect(args[0]);

        Thread.sleep(2000);

        System.out.println("\n\n==================");

        printZookeeperTree.list("/");

        System.out.println("\n\n==================");

        printZookeeperTree.close();

        System.out.println("\n\n==================");

    }
}
