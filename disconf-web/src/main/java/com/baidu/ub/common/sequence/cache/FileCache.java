/**
 * beidou-core#com.baidu.beidou.common.sequence.cache.FileCache.java
 * 下午2:47:10 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.sequence.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.baidu.ub.common.utils.FileUtils;

/**
 * 序列化到本地文件的sequence缓存
 * 
 * @author Darwin(Tianxin)
 */
class FileCache implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @param sequenceName
     * @param fileCacheValue
     * @param dbMaxValue
     */
    FileCache(String sequenceName, long fileCacheValue, long dbMaxValue) {
        this.sequenceName = sequenceName;
        this.fileCacheValue = fileCacheValue;
        this.dbMaxValue = dbMaxValue;
    }

    /**
     * 序列名称
     */
    private String sequenceName;

    /**
     * 文件系统中的缓存水位
     */
    private long fileCacheValue;

    /**
     * 数据库中的缓存水位
     */
    private long dbMaxValue;

    String getSequenceName() {
        return sequenceName;
    }

    long getFileCacheValue() {
        return fileCacheValue;
    }

    long getDbMaxValue() {
        return dbMaxValue;
    }

    /**
     * @param sequenceName
     * @return 下午2:53:10 created by Darwin(Tianxin)
     */
    final static FileCache readFileCache(String sequenceName) {
        File fcache = new File(cacheDirectory, sequenceName);
        if (!fcache.exists()) {
            return null;
        }

        // 读取缓存
        FileCache cache = null;
        InputStream is = null;
        try {
            is = new FileInputStream(fcache);
            byte[] bytes = new byte[(int) fcache.length()];
            is.read(bytes);
            cache = FileCache.parse(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeInputStream(is);
        }

        // 如果文件损坏，则删除文件
        if (cache == null)
            fcache.delete();
        return cache;
    }

    /**
     * @param fileCache
     *            下午2:56:26 created by Darwin(Tianxin)
     */
    final static void writeFile2Cache(FileCache fileCache) {
        File fcache = new File(cacheDirectory, fileCache.getSequenceName());
        OutputStream os = null;
        try {
            os = new FileOutputStream(fcache);
            os.write(fileCache.toByte());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeOutputStream(os);
        }
    }

    public static File cacheDirectory = null;

    /**
     * @param sCacheDirectory
     *            下午12:37:29 created by Darwin(Tianxin)
     */
    public static void setCacheDirectory(String sCacheDirectory) {
        cacheDirectory = new File(sCacheDirectory);
        if (!cacheDirectory.exists()) {
            cacheDirectory.mkdirs();
        }
    }

    /**
     * 解析字符串为一个对象
     * 
     * @param content
     * @return 上午10:04:38 created by Darwin(Tianxin)
     */
    private static FileCache parse(byte[] content) {

        String s = new String(content);
        int fstIndex = s.indexOf('-');
        if (fstIndex <= 0) {
            return null;
        }
        int sndIndex = s.indexOf('-', fstIndex + 1);
        if (sndIndex <= 0) {
            return null;
        }

        try {
            String name = s.substring(0, fstIndex);
            String dbMax = s.substring(fstIndex + 1, sndIndex);
            String fileMax = s.substring(sndIndex + 1);
            long dbMaxValue = Long.parseLong(dbMax);
            long fileCacheValue = Long.parseLong(fileMax);
            return new FileCache(name, fileCacheValue, dbMaxValue);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将对象序列化为一个字节数组
     * 
     * @return 上午10:05:46 created by Darwin(Tianxin)
     */
    private byte[] toByte() {
        StringBuilder sb = new StringBuilder();
        sb.append(sequenceName);
        sb.append('-').append(dbMaxValue);
        sb.append('-').append(fileCacheValue);
        return sb.toString().getBytes();
    }

}
