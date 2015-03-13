package com.baidu.ub.common.dbmanage.router;

import org.apache.log4j.Logger;
import org.springframework.context.support.ApplicationObjectSupport;

import com.baidu.ub.common.dbmanage.datasource.VirtualDataSource;
import com.baidu.ub.common.dbmanage.rule.DBShardingRule;

public class IDRouter extends ApplicationObjectSupport implements Router {

    private static Logger log = Logger.getLogger(IDRouter.class);
    private String prefix;
    private DBShardingRule dbShardingRule;

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @param dbShardingRule
     */
    public void setDbShardingRule(DBShardingRule dbShardingRule) {
        this.dbShardingRule = dbShardingRule;
    }

    /**
     * @see Router#getTargetDataSource(long)
     */
    public String getTargetDataSourceKey(int userid, boolean readMaster) {
        String vdbkey = prefix;
        String dbkey = null;
        String dbCode = null;
        // userid=0的情况放到下一层处理,需要支持返回dbCode为null的情况,当sharding num为1的时候，需要返回null
        dbCode = dbShardingRule.calculateDatabaseNo(userid);
        if (dbCode != null) {
            vdbkey = new StringBuilder(prefix).append("_").append(dbCode).toString();
        }

        VirtualDataSource vdb = (VirtualDataSource) getApplicationContext().getBean(vdbkey, VirtualDataSource.class);

        if (vdb != null) {
            dbkey = vdb.getDataSourceKey(readMaster);
            return dbkey;
        } else {
            String message = "Can't find dbkey mapping to user" + userid;
            RuntimeException t = new IllegalArgumentException(message);
            log.error(message, t);
            throw t;
        }
    }

}
