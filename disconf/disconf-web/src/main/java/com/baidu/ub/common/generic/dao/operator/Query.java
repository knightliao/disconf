/**
 * beidou-core-535#com.baidu.beidou.common.bo.SQLOperation.java
 * 上午11:38:16 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.generic.dao.operator;

import java.util.List;

/**
 * 封装一个sql操作
 * 
 * @author Darwin(Tianxin)
 */
public class Query {

    /**
     * 默认构造函数
     * 
     * @param sql
     * @param params
     */
    public Query(String sql, List<Object> params) {
        super();
        this.sql = sql;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getParams() {
        return params;
    }

    private String sql;

    private List<Object> params;

}
