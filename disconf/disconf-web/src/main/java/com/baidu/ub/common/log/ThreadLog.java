/**
 * adx-common#com.baidu.ub.common.log.ThreadLog.java
 * 上午11:23:22 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.log;

import com.baidu.ub.common.commons.ThreadContext;

/**
 * 线程日志级别的控制
 * 
 * @author Darwin(Tianxin)
 */
public class ThreadLog {

    public final static int LEVEL_TRACE = 10;
    public final static int LEVEL_DEBUG = 20;
    public final static int LEVEL_INFO = 30;
    public final static int LEVEL_WARN = 40;
    public final static int LEVEL_ERROR = 50;

    public final static boolean isTraceEnabled() {
        return ThreadContext.getThreadLog() != null
                && ThreadContext.getThreadLog() >= LEVEL_TRACE;
    }

    public final static boolean isDebugEnabled() {
        return ThreadContext.getThreadLog() != null
                && ThreadContext.getThreadLog() >= LEVEL_DEBUG;
    }

    public final static boolean isInfoEnabled() {
        return ThreadContext.getThreadLog() != null
                && ThreadContext.getThreadLog() >= LEVEL_INFO;
    }

    public final static boolean isWarnEnabled() {
        return ThreadContext.getThreadLog() != null
                && ThreadContext.getThreadLog() >= LEVEL_WARN;
    }

    public final static boolean isErrorEnabled() {
        return ThreadContext.getThreadLog() != null
                && ThreadContext.getThreadLog() >= LEVEL_ERROR;
    }
}
