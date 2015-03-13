/**
 * unbiz-genericdao#com.baidu.unbiz.common.genericdao.sequence.SequenceGenerator.java
 * 上午11:15:18 created by Darwin(Tianxin)
 */
package com.baidu.unbiz.common.genericdao.sequence;

/**
 * id生成器
 *
 * @author Darwin(Tianxin)
 */
public interface SequenceGenerator {

    /**
     * @param sequenceName
     *
     * @return 上午11:27:22 created by Darwin(Tianxin)
     */
    Integer getIntKey(String sequenceName);

    /**
     * @param sequenceName
     *
     * @return 上午11:27:34 created by Darwin(Tianxin)
     */
    Long getKey(String sequenceName);

    /**
     * @param sequenceName
     * @param size         上午11:30:02 created by Darwin(Tianxin)
     */
    void initialSequence(String sequenceName, int size);

}
