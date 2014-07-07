/**
 * beidou-core#com.baidu.beidou.common.sequence.domain.SequenceEntry.java
 * 下午3:30:48 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.sequence.cache;

/**
 * Sequence缓存的数据
 * 
 * @author Darwin(Tianxin)
 */
class SequenceCacheEntry {

    /**
     * 修改区分的大小
     * 
     * @param count2SplitCache
     *            下午2:07:22 created by Darwin(Tianxin)
     */
    public static void setCount2SplitCache(int count2SplitCache) {
        if (count2SplitCache > 0)
            SequenceCacheEntry.count2SplitCache = count2SplitCache;
    }

    /**
     * 每次获取的缓存分成多少份
     */
    private static int count2SplitCache = 10;

    /**
     * Sequence记录的名字
     */
    private String sequenceName;

    /**
     * Sequence每次缓存的数量
     */
    private int cacheSize;

    /**
     * Sequence当前的缓存最大值，要注意改值为开区间，不能使用
     */
    private long maxValue;

    /**
     * 当前Sequence的值
     */
    private long currentValue;

    /**
     * 在文件系统中分配到的cache帽
     */
    private long fileCacheTop;

    /**
     * 构造函数
     * 
     * @param sequenceName
     * @param currentValue
     * @param maxValue
     */
    SequenceCacheEntry(String sequenceName, int cacheSize) {
        this.sequenceName = sequenceName;
        this.cacheSize = cacheSize;

        loadFromFile();
    }

    /**
     * 获取下一个值
     * 
     * @return 下午4:44:49 created by Darwin(Tianxin)
     */
    synchronized long nextValue() {
        long value = currentValue < maxValue ? currentValue : -1;
        currentValue += 1;

        dispatchSerialize2File();
        return value;
    }

    /**
     * 获取下n个值
     * 
     * @param count
     * @return 下午4:56:38 created by Darwin(Tianxin)
     */
    synchronized long[] nextValues(int count) {

        // 计算当前缓存中的容量能力
        int capability = (int) (maxValue - currentValue);
        int arraySize = capability >= count ? count : capability;
        long[] values = new long[arraySize];
        for (int i = 0; i < arraySize; i++) {
            values[i] = currentValue++;
        }

        dispatchSerialize2File();
        return values;
    }

    /**
     * 刷新次缓存条目
     * 
     * @param newLine
     * @param keepCount
     *            上午9:54:36 created by Darwin(Tianxin)
     */
    synchronized void refresh(long newLine, int keepCount) {
        this.currentValue = newLine;
        this.maxValue = newLine + keepCount;
        dispatchSerialize2File();
    }

    /**
     * 获取该缓存的名字
     * 
     * @return 上午10:31:37 created by Darwin(Tianxin)
     */
    String getSequenceName() {
        return this.sequenceName;
    }

    /**
     * 获取缓存的大小
     * 
     * @return 上午10:31:41 created by Darwin(Tianxin)
     */
    int getCacheSize() {
        return this.cacheSize;
    }

    /**
     * 缓存中不在有多余的可供使用的ID
     * 
     * @return 下午1:44:06 created by Darwin(Tianxin)
     */
    boolean isEmpty() {
        return this.currentValue >= this.maxValue || this.currentValue <= 0;
    }

    /**
     * 每分配10%则向文件系统中序列化
     * 
     * 下午2:57:59 created by Darwin(Tianxin)
     */
    private void dispatchSerialize2File() {

        if (count2SplitCache <= 1)
            return;

        // 如果文件水位比较小，则重写文件水位
        if (fileCacheTop <= currentValue) {
            int partStep = cacheSize / count2SplitCache;
            fileCacheTop = maxValue
                    - ((maxValue - currentValue - 1) / partStep) * partStep;
            FileCache.writeFile2Cache(new FileCache(sequenceName, fileCacheTop,
                    maxValue));
        }

    }

    /**
     * 启动时从文件系统中加载
     * 
     * 下午2:57:42 created by Darwin(Tianxin)
     */
    private void loadFromFile() {
        FileCache fcache = FileCache.readFileCache(sequenceName);
        if (fcache == null) {
            return;
        }

        this.currentValue = fcache.getFileCacheValue();
        this.maxValue = fcache.getDbMaxValue();

        dispatchSerialize2File();
    }
}
