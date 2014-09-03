/**
 * beidou-core#com.baidu.beidou.common.sequence.SequenceStub.java
 * 下午4:19:53 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.sequence;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.baidu.ub.common.sequence.service.SequenceService;

/**
 * 集成Sequence服务的桩
 * 
 * @author Darwin(Tianxin)
 */
public class SequenceStub {

    /**
     * sequence服务对象
     */
    private static SequenceService sequenceService = null;

    /**
     * @param sequenceService
     *            the sequenceService to set
     */
    public void setSequenceService(SequenceService sequenceService) {
        SequenceStub.sequenceService = sequenceService;
        for (Entry<String, Integer> entry : map.entrySet()) {
            sequenceService.initialSequence(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取下一个ID
     * 
     * @return 下午12:29:05 created by Darwin(Tianxin)
     */
    public static int getIntKey(String sequenceName) {
        return sequenceService.getIntKey(sequenceName);
    }

    /**
     * 获取count个ID
     * 
     * @param count
     * @return 下午12:29:25 created by Darwin(Tianxin)
     */
    public static int[] getIntKeys(String sequenceName, int count) {
        return sequenceService.getIntKeys(sequenceName, count);
    }

    /**
     * 获取下一个ID
     * 
     * @return 下午12:29:05 created by Darwin(Tianxin)
     */
    public static long getKey(String sequenceName) {
        return sequenceService.getKey(sequenceName);
    }

    /**
     * 获取count个ID
     * 
     * @param count
     * @return 2013/11/8 14:00 modify by haiyang01
     */
    public static long[] getKeys(String sequenceName, int count) {
        return sequenceService.getKeys(sequenceName, count);
    }

    /**
     * 初始化一个缓存
     * 
     * 下午3:38:46 created by Darwin(Tianxin)
     */
    public static final void initialSequence(String name, int step) {
        if (sequenceService == null) {
            map.put(name, step);
        } else {
            sequenceService.initialSequence(name, step);
        }
    }

    private static Map<String, Integer> map = new HashMap<String, Integer>();

    /**
     * 获取keyword的id
     * 
     * @return 上午9:29:26 created by Darwin(Tianxin)
     */
    public static Long getKeywordId() {
        return getKey(SequenceConstant.KEYWORDID);
    }
}
