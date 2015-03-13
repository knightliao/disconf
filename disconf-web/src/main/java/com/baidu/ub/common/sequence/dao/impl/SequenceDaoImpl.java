/**
 * ubid#com.baidu.ubid.dao.SequenceDaoImpl.java
 * 下午3:55:53 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.sequence.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.baidu.ub.common.sequence.dao.SequenceDao;

/**
 * 生成SequenceId的Dao实现
 *
 * @author Darwin(Tianxin)
 */
public class SequenceDaoImpl implements SequenceDao {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.ubid.dao.SequenceDao#generateKeys(java.lang.String, int)
     */
    public long generateKeys(String sequenceName, int step) {

        // 参数校验
        if (sequenceName == null || sequenceName.length() == 0 || step <= 0) {
            throw new RuntimeException("sequence not exist : " + sequenceName);
        }

        // 构造SQL
        String querySQL = "select beidoucap.get_next_values (?, ?)";

        // 执行操作
        Long value = jdbcTemplate.queryForLong(querySQL, new Object[] {sequenceName, step});
        if (value == null || value <= 0) {
            throw new RuntimeException("sequence not exist : " + sequenceName);
        }
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.ubid.dao.SequenceDao#generateKeys(java.lang.String, int)
     */
    public long generateKeys(int sequenceCode, int step) {

        // 参数校验
        if (sequenceCode <= 0 || step <= 0) {
            throw new RuntimeException("sequence not exist : code is " + sequenceCode);
        }

        // 构造SQL
        String querySQL = "select nextid from beidoucap.sequenceid where id=? for update";
        String increaseSQL = "update beidoucap.sequenceid set nextid=nextid+? where id=?";

        // 执行操作
        Long value = jdbcTemplate.queryForLong(querySQL, new Object[] {sequenceCode});
        if (value == null || value <= 0) {
            throw new RuntimeException("sequence not exist : code is " + sequenceCode);
        }

        // 更新数据库中缓存的数量
        jdbcTemplate.update(increaseSQL, new Object[] {step, sequenceCode});
        return value;
    }
}
