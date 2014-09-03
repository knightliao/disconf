/**
 * adx-common#com.baidu.ub.log.AopLogFactory.java
 * 下午7:49:23 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * 切面的LogFactory
 * 
 * @author Darwin(Tianxin)
 */
public class AopLogFactory {

    /**
     * 获取一个日志记录实例
     * 
     * @param clazz
     * @return 下午7:49:35 created by Darwin(Tianxin)
     */
    public static Logger getLogger(Class<?> clazz) {

        final Logger core = LoggerFactory.getLogger(clazz);

        return new Logger() {

            @Override
            public void warn(Marker marker, String format, Object arg1,
                    Object arg2) {
                if (ThreadLog.isWarnEnabled())
                    core.error(marker, format, arg1, arg2);
                else
                    core.warn(marker, format, arg1, arg2);
            }

            @Override
            public void warn(Marker marker, String msg, Throwable t) {
                if (ThreadLog.isWarnEnabled())
                    core.error(marker, msg, t);
                else
                    core.warn(marker, msg, t);
            }

            @Override
            public void warn(Marker marker, String format, Object[] argArray) {
                if (ThreadLog.isWarnEnabled())
                    core.error(marker, format, argArray);
                else
                    core.warn(marker, format, argArray);
            }

            @Override
            public void warn(Marker marker, String format, Object arg) {
                if (ThreadLog.isWarnEnabled())
                    core.error(marker, format, arg);
                else
                    core.warn(marker, format, arg);
            }

            @Override
            public void warn(String format, Object arg1, Object arg2) {
                if (ThreadLog.isWarnEnabled())
                    core.error(format, arg1, arg2);
                else
                    core.warn(format, arg1, arg2);
            }

            @Override
            public void warn(Marker marker, String msg) {
                if (ThreadLog.isWarnEnabled())
                    core.error(marker, msg);
                core.warn(marker, msg);
            }

            @Override
            public void warn(String msg, Throwable t) {
                if (ThreadLog.isWarnEnabled())
                    core.error(msg, t);
                else
                    core.warn(msg, t);
            }

            @Override
            public void warn(String format, Object[] argArray) {
                if (ThreadLog.isWarnEnabled())
                    core.warn(format, argArray);
                else
                    core.warn(format, argArray);
            }

            @Override
            public void warn(String format, Object arg) {
                if (ThreadLog.isWarnEnabled())
                    core.error(format, arg);
                else
                    core.warn(format, arg);
            }

            @Override
            public void warn(String msg) {
                if (ThreadLog.isWarnEnabled())
                    core.error(msg);
                else
                    core.warn(msg);
            }

            @Override
            public void trace(Marker marker, String format, Object arg1,
                    Object arg2) {
                if (ThreadLog.isTraceEnabled())
                    core.error(marker, format, arg1, arg2);
                else
                    core.trace(marker, format, arg1, arg2);
            }

            @Override
            public void trace(Marker marker, String msg, Throwable t) {
                if (ThreadLog.isTraceEnabled())
                    core.error(marker, msg, t);
                else
                    core.trace(marker, msg, t);
            }

            @Override
            public void trace(Marker marker, String format, Object[] argArray) {
                if (ThreadLog.isTraceEnabled())
                    core.error(marker, format, argArray);
                else
                    core.trace(marker, format, argArray);
            }

            @Override
            public void trace(Marker marker, String format, Object arg) {
                if (ThreadLog.isTraceEnabled())
                    core.error(marker, format, arg);
                else
                    core.trace(marker, format, arg);
            }

            @Override
            public void trace(String format, Object arg1, Object arg2) {
                if (ThreadLog.isTraceEnabled())
                    core.error(format, arg1, arg2);
                else
                    core.trace(format, arg1, arg2);
            }

            @Override
            public void trace(Marker marker, String msg) {
                if (ThreadLog.isTraceEnabled())
                    core.error(marker, msg);
                else
                    core.trace(marker, msg);
            }

            @Override
            public void trace(String msg, Throwable t) {
                if (ThreadLog.isTraceEnabled())
                    core.error(msg, t);
                else
                    core.trace(msg, t);
            }

            @Override
            public void trace(String format, Object[] argArray) {
                if (ThreadLog.isTraceEnabled())
                    core.error(format, argArray);
                else
                    core.trace(format, argArray);
            }

            @Override
            public void trace(String format, Object arg) {
                if (ThreadLog.isTraceEnabled())
                    core.error(format, arg);
                else
                    core.trace(format, arg);
            }

            @Override
            public void trace(String msg) {
                if (ThreadLog.isTraceEnabled())
                    core.error(msg);
                else
                    core.trace(msg);
            }

            @Override
            public boolean isWarnEnabled(Marker marker) {
                return core.isWarnEnabled();
            }

            @Override
            public boolean isWarnEnabled() {
                return core.isWarnEnabled();
            }

            @Override
            public boolean isTraceEnabled(Marker marker) {
                return core.isTraceEnabled(marker);
            }

            @Override
            public boolean isTraceEnabled() {
                return core.isTraceEnabled();
            }

            @Override
            public boolean isInfoEnabled(Marker marker) {
                return core.isInfoEnabled(marker);
            }

            @Override
            public boolean isInfoEnabled() {
                return core.isInfoEnabled();
            }

            @Override
            public boolean isErrorEnabled(Marker marker) {
                return core.isErrorEnabled(marker);
            }

            @Override
            public boolean isErrorEnabled() {
                return core.isErrorEnabled();
            }

            @Override
            public boolean isDebugEnabled(Marker marker) {
                return core.isDebugEnabled(marker);
            }

            @Override
            public boolean isDebugEnabled() {
                return core.isDebugEnabled();
            }

            @Override
            public void info(Marker marker, String format, Object arg1,
                    Object arg2) {
                if (ThreadLog.isInfoEnabled())
                    core.error(marker, format, arg1, arg2);
                else
                    core.info(marker, format, arg1, arg2);
            }

            @Override
            public void info(Marker marker, String msg, Throwable t) {
                if (ThreadLog.isInfoEnabled())
                    core.error(marker, msg, t);
                else
                    core.info(marker, msg, t);
            }

            @Override
            public void info(Marker marker, String format, Object[] argArray) {
                if (ThreadLog.isInfoEnabled())
                    core.error(marker, format, argArray);
                else
                    core.info(marker, format, argArray);
            }

            @Override
            public void info(Marker marker, String format, Object arg) {
                if (ThreadLog.isInfoEnabled())
                    core.error(marker, format, arg);
                else
                    core.info(marker, format, arg);
            }

            @Override
            public void info(String format, Object arg1, Object arg2) {
                if (ThreadLog.isInfoEnabled())
                    core.error(format, arg1, arg2);
                else
                    core.info(format, arg1, arg2);
            }

            @Override
            public void info(Marker marker, String msg) {
                if (ThreadLog.isInfoEnabled())
                    core.error(marker, msg);
                else
                    core.info(marker, msg);
            }

            @Override
            public void info(String msg, Throwable t) {
                if (ThreadLog.isInfoEnabled())
                    core.error(msg, t);
                else
                    core.info(msg, t);
            }

            @Override
            public void info(String format, Object[] argArray) {
                if (ThreadLog.isInfoEnabled())
                    core.error(format, argArray);
                else
                    core.info(format, argArray);
            }

            @Override
            public void info(String format, Object arg) {
                if (ThreadLog.isInfoEnabled())
                    core.error(format, arg);
                else
                    core.info(format, arg);
            }

            @Override
            public void info(String msg) {
                if (ThreadLog.isInfoEnabled())
                    core.error(msg);
                else
                    core.info(msg);
            }

            @Override
            public String getName() {
                return core.getName();
            }

            @Override
            public void error(Marker marker, String format, Object arg1,
                    Object arg2) {
                core.error(marker, format, arg1, arg2);
            }

            @Override
            public void error(Marker marker, String msg, Throwable t) {
                core.error(marker, msg, t);
            }

            @Override
            public void error(Marker marker, String format, Object[] argArray) {
                core.error(marker, format, argArray);
            }

            @Override
            public void error(Marker marker, String format, Object arg) {
                core.error(marker, format, arg);
            }

            @Override
            public void error(String format, Object arg1, Object arg2) {
                core.error(format, arg1, arg2);
            }

            @Override
            public void error(Marker marker, String msg) {
                core.error(marker, msg);
            }

            @Override
            public void error(String msg, Throwable t) {
                core.error(msg, t);
            }

            @Override
            public void error(String format, Object[] argArray) {
                core.error(format, argArray);
            }

            @Override
            public void error(String format, Object arg) {
                core.error(format, arg);
            }

            @Override
            public void error(String msg) {
                core.error(msg);
            }

            @Override
            public void debug(Marker marker, String format, Object arg1,
                    Object arg2) {
                if (ThreadLog.isDebugEnabled())
                    core.error(marker, format, arg1, arg2);
                else
                    core.debug(marker, format, arg1, arg2);
            }

            @Override
            public void debug(Marker marker, String msg, Throwable t) {
                if (ThreadLog.isDebugEnabled())
                    core.error(marker, msg, t);
                else
                    core.debug(marker, msg, t);
            }

            @Override
            public void debug(Marker marker, String format, Object[] argArray) {
                if (ThreadLog.isDebugEnabled())
                    core.error(marker, format, argArray);
                else
                    core.debug(marker, format, argArray);
            }

            @Override
            public void debug(Marker marker, String format, Object arg) {
                if (ThreadLog.isDebugEnabled())
                    core.error(marker, format, arg);
                core.debug(marker, format, arg);
            }

            @Override
            public void debug(String format, Object arg1, Object arg2) {
                if (ThreadLog.isDebugEnabled())
                    core.error(format, arg1, arg2);
                else
                    core.debug(format, arg1, arg2);

            }

            @Override
            public void debug(Marker marker, String msg) {
                if (ThreadLog.isDebugEnabled())
                    core.error(marker, msg);
                else
                    core.debug(marker, msg);
            }

            @Override
            public void debug(String msg, Throwable t) {
                if (ThreadLog.isDebugEnabled())
                    core.error(msg, t);
                core.debug(msg, t);
            }

            @Override
            public void debug(String format, Object[] argArray) {
                if (ThreadLog.isDebugEnabled())
                    core.error(format, argArray);
                else
                    core.debug(format, argArray);
            }

            @Override
            public void debug(String format, Object arg) {
                if (ThreadLog.isDebugEnabled())
                    core.error(format, arg);
                else
                    core.debug(format, arg);
            }

            @Override
            public void debug(String msg) {
                if (ThreadLog.isDebugEnabled())
                    core.error(msg);
                else
                    core.debug(msg);
            }
        };
    }

}
