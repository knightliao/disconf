package com.baidu.ub.common.dbmanage.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

public class SimpleDBShardingRuleImpl implements DBShardingRule {

    private static Logger log = Logger.getLogger(SimpleDBShardingRuleImpl.class);

    protected Random randomprovider = new Random();
    private int tableShardingLength = 6;
    private int shardingNum = 1;
    private List<String> dbCodeCollection;

    public String calculateDatabaseNo(int userid) {
        if (shardingNum == 1) {
            return null;
        }

        int useridcode = 0;
        if (userid <= 0) {
            throw new RuntimeException("No userid contexted,so you can't know how to routing!!");
            //考虑随机路由到其中的某个库
            //useridcode = randomprovider.nextInt(shardingNum);
        } else {
            useridcode = (int) ((userid >>> tableShardingLength) & (shardingNum - 1));
        }
        return dbCodeCollection.get(useridcode);
    }

    /**
     * @param shardingNum the shardingNum to set
     */
    public void setShardingNum(int shardingNum) {
        if ((shardingNum & (shardingNum - 1)) != 0) {
            String message = "shardingNum should be a power of 2, like 4,8,16,32";
            RuntimeException t = new IllegalArgumentException(message);
            log.error(message, t);
            throw t;
        }
        this.shardingNum = shardingNum;
        setDbCodeCollection();
    }

    /**
     * 初始化dbcode的集合,依赖于数据库的shardingNum
     */
    private void setDbCodeCollection() {
        dbCodeCollection = new ArrayList<String>(shardingNum);
        int codeLength = Integer.toBinaryString(shardingNum - 1).length();
        for (int i = 0; i < shardingNum; i++) {
            String dbcode = Integer.toBinaryString(i);
            for (int j = dbcode.length(); j < codeLength; j++) {
                dbcode = "0" + dbcode;
            }
            dbCodeCollection.add(i, dbcode);
        }
    }

}
