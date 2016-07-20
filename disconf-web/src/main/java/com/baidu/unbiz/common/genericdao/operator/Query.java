package com.baidu.unbiz.common.genericdao.operator;

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

    /**
     * @param sql the sql to set
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * @param params the params to set
     */
    public void setParams(List<Object> params) {
        this.params = params;
    }

    private String sql;

    private List<Object> params;

}
