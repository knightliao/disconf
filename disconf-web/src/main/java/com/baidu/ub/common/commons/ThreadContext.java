package com.baidu.ub.common.commons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * 线程执行的上下文内容
 */
public class ThreadContext {

    /**
     * 线程上下文变量的持有者
     */
    private final static ThreadLocal<Map<String, Object>> CTX_HOLDER = new ThreadLocal<Map<String, Object>>();

    static {
        CTX_HOLDER.set(new HashMap<String, Object>());
    }

    /**
     * 添加内容到线程上下文中
     *
     * @param key
     * @param value
     */
    public final static void putContext(String key, Object value) {
        Map<String, Object> ctx = CTX_HOLDER.get();
        if (ctx == null) {
            return;
        }
        ctx.put(key, value);
    }

    /**
     * 从线程上下文中获取内容
     *
     * @param key
     */
    @SuppressWarnings("unchecked")
    public final static <T extends Object> T getContext(String key) {
        Map<String, Object> ctx = CTX_HOLDER.get();
        if (ctx == null) {
            return null;
        }
        return (T) ctx.get(key);
    }

    /**
     * 获取线程上下文
     */
    public final static Map<String, Object> getContext() {
        Map<String, Object> ctx = CTX_HOLDER.get();
        if (ctx == null) {
            return null;
        }
        return ctx;
    }

    /**
     * 删除上下文中的key
     *
     * @param key
     */
    public final static void remove(String key) {
        Map<String, Object> ctx = CTX_HOLDER.get();
        if (ctx != null) {
            ctx.remove(key);
        }
    }

    /**
     * 上下文中是否包含此key
     *
     * @param key
     *
     * @return
     */
    public final static boolean contains(String key) {
        Map<String, Object> ctx = CTX_HOLDER.get();
        if (ctx != null) {
            return ctx.containsKey(key);
        }
        return false;
    }

    /**
     * 清空线程上下文
     */
    public final static void clean() {
        CTX_HOLDER.set(null);
    }

    /**
     * 初始化线程上下文
     */
    public final static void init() {
        CTX_HOLDER.set(new HashMap<String, Object>());
    }

    /**
     * 获取用来做分库分表的key
     */
    @SuppressWarnings("unchecked")
    public final static <K extends Serializable> K getShardKey() {
        return (K) getContext(SHARD_KEY);
    }

    /**
     * 设置做分表分库的切分的key
     */
    public final static <K extends Serializable> void putShardKey(K shardKey) {
        putContext(SHARD_KEY, shardKey);
    }

    /**
     * 获取Session中的用户信息
     */
    @SuppressWarnings("unchecked")
    public final static <U extends BaseObject<?>> U getSessionVisitor() {
        return (U) getContext(VISITOR_KEY);
    }

    /**
     * 设置做分表分库的切分的key
     */
    public final static <K extends Serializable, U extends BaseObject<K>> void putSessionVisitor(U sessionVisitor) {
        putContext(VISITOR_KEY, sessionVisitor);
    }

    /**
     * 线程日志的级别
     */
    public final static void putThreadLog(Integer logLevel) {
        putContext(THREAD_LOG_KEY, logLevel);
    }

    /**
     * 获取线程日志的级别
     */
    public final static Integer getThreadLog() {
        return getContext(THREAD_LOG_KEY);
    }

    /**
     * 设置会话ID数据
     */
    public final static void putSessionId(String sessionId) {
        putContext(SESSION_KEY, sessionId);
    }

    /**
     * 获取会话ID数据
     */
    public final static String getSessionId() {
        return getContext(SESSION_KEY);
    }

    /**
     * 清空会话ID数据
     */
    public final static void removeSessionId() {
        remove(SESSION_KEY);
    }

    private final static String SHARD_KEY = "shardKey";

    /**
     * 当前Session中登陆的user
     */
    private final static String VISITOR_KEY = "sessionVisitor";

    /**
     * 线程的日志级别
     */
    private final static String THREAD_LOG_KEY = "threadLog";

    /**
     * 会话ID
     */
    private final static String SESSION_KEY = "sessionId";
}
