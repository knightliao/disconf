/**
 * ubid#com.baidu.ubid.service.SequenceServiceImpl.java
 * 下午12:35:39 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.sequence.service.impl;

import com.baidu.ub.common.sequence.cache.SequenceCache;
import com.baidu.ub.common.sequence.service.SequenceService;

/**
 * Sequence的实现
 *
 * @author Darwin(Tianxin)
 */
public class SequenceServiceImpl implements SequenceService {

    /**
     * sequence的cache
     */
    private SequenceCache sequenceCache = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.ubid.service.SequenceService#getIntKey(java.lang.String)
     */
    public int getIntKey(String sequenceName) {
        return Long.valueOf(getKey(sequenceName)).intValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.ubid.service.SequenceService#getIntKeys(java.lang.String,
     * int)
     */
    public int[] getIntKeys(String sequenceName, int count) {
        long[] keys = getKeys(sequenceName, count);
        int[] iKeys = new int[keys.length];
        for (int i = 0; i < keys.length; i++) {
            iKeys[i] = Long.valueOf(keys[i]).intValue();
        }
        return iKeys;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.ubid.service.SequenceService#getKey(java.lang.String)
     */
    public synchronized long getKey(String sequenceName) {
        return sequenceCache.getValue(sequenceName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.ubid.service.SequenceService#getKeys(java.lang.String,
     * int)
     */
    public synchronized long[] getKeys(String sequenceName, int count) {
        return sequenceCache.getValues(sequenceName, count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baidu.beidou.common.sequence.service.SequenceService#initialSequence
     * (java.lang.String, int)
     */
    public void initialSequence(String name, int step) {
        sequenceCache.initialCache(name, step);
    }

    public void setSequenceCache(SequenceCache sequenceCache) {
        this.sequenceCache = sequenceCache;
    }
}
