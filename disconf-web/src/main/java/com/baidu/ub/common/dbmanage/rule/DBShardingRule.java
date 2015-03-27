package com.baidu.ub.common.dbmanage.rule;

public interface DBShardingRule {

    /**
     * 根据数据库分库规则，计算数据库的sharding,如果sharding数为1，则返回null
     *
     * @param userid
     *
     * @return
     */
    String calculateDatabaseNo(int userid);

}
