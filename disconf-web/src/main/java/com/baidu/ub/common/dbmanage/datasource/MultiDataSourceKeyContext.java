package com.baidu.ub.common.dbmanage.datasource;

import org.apache.log4j.Logger;

import com.baidu.ub.common.commons.ThreadContext;
import com.baidu.ub.common.dbmanage.router.Router;

public class MultiDataSourceKeyContext {

    private static final Logger log = Logger.getLogger(MultiDataSourceKeyContext.class);

    private String dbName;

    private Router router;

    public void setKey(Integer userid, boolean readMaster) {
        String dbkey = router.getTargetDataSourceKey(userid, readMaster);
        setKey(dbkey);
    }

    /**
     * 设置方法调用时使用的key,存入到threadlocal中
     */
    public void setKey(String key) {
        if (key == null) {
            clearKey();
        } else {
            ThreadContext.putContext(dbName, key);
        }
        log.debug("set data source key[" + key + "]");
    }

    /**
     * 获得当前数据源key,如果threadlocal中没有数据源，则返回对应用户的主库连接
     */
    public String getKey() {
        String dbkey = ThreadContext.getContext(dbName);
        if (dbkey == null) {
            Integer userid = ThreadContext.getShardKey();
            if (userid == null) {
                userid = 0;
                // throw new
                // RuntimeException("No userid contexted,so you can't know how to routing!!");
            }
            dbkey = router.getTargetDataSourceKey(userid, true);

            StackTraceElement[] elements = Thread.currentThread().getStackTrace();

            logTrace(dbkey, elements);

        }
        return dbkey;
    }

    private void logTrace(String dbkey, StackTraceElement[] elements) {
        if (elements == null || elements.length == 0) {
            return;
        }
        for (StackTraceElement ste : elements) {
            if (ste.getClassName().indexOf("service.impl") > 0) {
                log.debug("no transaction data source Key[" + dbkey + "," + ste.getClassName() + "." +
                              ste.getMethodName() + ",line number:" + ste.getLineNumber() + "]");
                return;
            }
        }
        for (StackTraceElement ste : elements) {
            log.debug("no transaction data source Key[" + dbkey + "," + ste.getClassName() + "." + ste.getMethodName() +
                          ",line number:" + ste.getLineNumber() + "]");
        }
        return;
    }

    /**
     * 获得当前dataSourceContext中实际保存的数据源key,没有就返回null
     */
    public String getActualContextKey() {
        // String dbkey = DB_KEY.get();
        String dbkey = ThreadContext.getContext(dbName);
        log.debug("get data source Key[" + dbkey + "]");
        return dbkey;
    }

    /**
     * 清除设置的key
     */
    public void clearKey() {
        ThreadContext.remove(dbName);
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

}
