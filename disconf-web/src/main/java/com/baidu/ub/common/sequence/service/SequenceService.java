/**
 * ubid#com.baidu.ubid.service.SequenceService.java
 * 下午12:27:33 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.sequence.service;

/**
 * 生成唯一ID的服务接口定义
 *
 * @author Darwin(Tianxin)
 */
public interface SequenceService {

    /**
     * 获取下一个ID
     *
     * @return 下午12:29:05 created by Darwin(Tianxin)
     */
    int getIntKey(String sequenceName);

    /**
     * 获取count个ID
     *
     * @param count
     *
     * @return 下午12:29:25 created by Darwin(Tianxin)
     */
    int[] getIntKeys(String sequenceName, int count);

    /**
     * 获取下一个ID
     *
     * @return 下午12:29:05 created by Darwin(Tianxin)
     */
    long getKey(String sequenceName);

    /**
     * 获取count个ID
     *
     * @param count
     *
     * @return 下午12:29:25 created by Darwin(Tianxin)
     */
    long[] getKeys(String sequenceName, int count);

    /**
     * 初始化一个缓存
     *
     * @param name
     * @param step 下午3:39:47 created by Darwin(Tianxin)
     */
    void initialSequence(String name, int step);

}
