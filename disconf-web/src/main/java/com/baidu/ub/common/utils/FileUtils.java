/**
 * beidou-core#com.baidu.beidou.common.utils.FileUtils.java
 * 下午5:26:38 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * 文件操作的方法集
 * 
 * @author Darwin(Tianxin)
 */
public class FileUtils {

    /**
     * 关闭文件流
     * 
     * @param w
     *            上午10:27:27 created by Darwin(Tianxin)
     */
    public final static void closeWriter(Writer w) {
        if (w != null)
            try {
                w.close();
            } catch (Exception e) {
            }
    }

    /**
     * 关闭文件流
     * 
     * @param r
     *            上午10:27:27 created by Darwin(Tianxin)
     */
    public final static void closeReader(Reader r) {
        if (r != null)
            try {
                r.close();
            } catch (Exception e) {
            }
    }

    /**
     * 关闭文件流
     * 
     * @param os
     *            上午10:27:27 created by Darwin(Tianxin)
     */
    public final static void closeOutputStream(OutputStream os) {
        if (os != null)
            try {
                os.close();
            } catch (Exception e) {
            }
    }

    /**
     * 关闭文件流
     * 
     * @param is
     *            上午10:27:27 created by Darwin(Tianxin)
     */
    public final static void closeInputStream(InputStream is) {
        if (is != null)
            try {
                is.close();
            } catch (Exception e) {
            }
    }
}
