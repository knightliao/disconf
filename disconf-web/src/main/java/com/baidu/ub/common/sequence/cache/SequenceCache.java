/**
 * ubid#com.baidu.ubid.service.SequenceCache.java
 * 下午4:31:33 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.sequence.cache;

import java.util.HashMap;
import java.util.Map;

import com.baidu.ub.common.sequence.dao.SequenceDao;

/**
 * 对北斗的SequenceId的缓存
 *
 * @author Darwin(Tianxin)
 */
public class SequenceCache {

    /**
     * 默认的缓存大小为1000个ID
     */
    private int defaultCacheSize = 1000;

    /**
     * 访问数据库的dao
     */
    private SequenceDao sequenceDao = null;

    /**
     * sequence的存储
     */
    private Map<String, SequenceCacheEntry> sequenceMap = new HashMap<String, SequenceCacheEntry>();

    /**
     * 初始化一个Sequence
     *
     * @param sequenceName
     * @param cacheSize    下午3:50:47 created by Darwin(Tianxin)
     */
    public void initialCache(String sequenceName, int cacheSize) {

        // 获取缓存
        SequenceCacheEntry entry = sequenceMap.get(sequenceName);
        if (entry == null) {
            entry = new SequenceCacheEntry(sequenceName, cacheSize);
            sequenceMap.put(sequenceName, entry);
        }
    }

    /**
     * 刷新entry，向数据库请求新的ID
     *
     * @param entry 上午9:54:29 created by Darwin(Tianxin)
     */
    private void refreshEntry(SequenceCacheEntry entry) {
        // 如果缓存是空的，则进行初始化
        if (entry.isEmpty()) {
            long newLine = sequenceDao.generateKeys(entry.getSequenceName(), entry.getCacheSize());
            entry.refresh(newLine, entry.getCacheSize());
        }
    }

    /**
     * 获取下一个id
     *
     * @param sequenceName
     *
     * @return 下午4:57:17 created by Darwin(Tianxin)
     */
    public long getValue(String sequenceName) {

        // 获取缓存，如果没有缓存则初始化缓存
        SequenceCacheEntry entry = sequenceMap.get(sequenceName);
        if (entry == null) {
            initialCache(sequenceName, defaultCacheSize);
            entry = sequenceMap.get(sequenceName);
        }

        // 获取ID,ID不合法则重新初始化缓存
        long value = entry.nextValue();
        if (value == -1) {
            refreshEntry(entry);
            value = entry.nextValue();
        }

        // 返回值依然不合法，则抛异常
        if (value == -1) {
            throw new RuntimeException("缓存初始化失败!");
        }
        return value;
    }

    /**
     * 获取下面count个id
     *
     * @param sequenceName
     * @param count
     *
     * @return 下午4:58:23 created by Darwin(Tianxin)
     */
    public long[] getValues(String sequenceName, int count) {

        // 获取缓存，如果没有缓存则初始化缓存
        SequenceCacheEntry entry = sequenceMap.get(sequenceName);
        if (entry == null) {
            initialCache(sequenceName, defaultCacheSize);
            entry = sequenceMap.get(sequenceName);
        }

        // 如果正常获取了sequence，则直接返回
        long[] values = entry.nextValues(count);
        if (values.length == count) {
            return values;
        }

        // 如果没获取到足够的ID，则需要重新初始化缓存
        refreshEntry(entry);
        int leftCount = count - values.length;
        long[] nextValues = getValues(sequenceName, leftCount);

        // 构造返回的结果
        long[] result = new long[count];
        System.arraycopy(values, 0, result, 0, values.length);
        System.arraycopy(nextValues, 0, result, values.length, nextValues.length);
        return result;
    }

    public void setSequenceDao(SequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

    public void setFileCacheDir(String fileCacheDir) {
        FileCache.setCacheDirectory(fileCacheDir);
    }
}
