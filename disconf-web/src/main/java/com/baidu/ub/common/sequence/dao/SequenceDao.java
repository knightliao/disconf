/**
 * ubid#com.baidu.ubid.dao.SequenceDao.java
 * 下午12:37:40 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.sequence.dao;

/**
 * 分段分配sequence的DAO接口定义
 * 
 * @author Darwin(Tianxin)
 */
public interface SequenceDao {

    /**
     * 生成新的sequenceid
     * 
     * @param sequenceName
     * @param step
     * @return 下午4:02:03 created by Darwin(Tianxin)
     */
    long generateKeys(String sequenceName, int step);

    /**
     * 生成新的sequenceid
     * 
     * @param sequenceCode
     * @param step
     * @return 下午4:02:03 created by Darwin(Tianxin)
     */
    long generateKeys(int sequenceCode, int step);

}
