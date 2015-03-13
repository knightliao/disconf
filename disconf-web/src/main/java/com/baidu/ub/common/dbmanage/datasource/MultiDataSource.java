package com.baidu.ub.common.dbmanage.datasource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.baidu.ub.common.commons.ThreadContext;

public class MultiDataSource extends AbstractRoutingDataSource {

    private static final Logger log = Logger.getLogger(MultiDataSource.class);

    static {
        ThreadContext.putContext("Bootstrap", Boolean.TRUE);
    }

    private MultiDataSourceKeyContext multiDataSourceKeyContext;

    @Override
    protected Object determineCurrentLookupKey() {
        String key = "";
        try {
            key = multiDataSourceKeyContext.getKey();
        } catch (Throwable e) {
            logTrace(e);
        }
        return key;
    }

    private void logTrace(Throwable e) {

        StackTraceElement[] stackTraces = e.getStackTrace();
        if (Boolean.TRUE.equals(ThreadContext.getContext("Bootstrap"))) {
            for (int i = 4; i < stackTraces.length; i++) {// 过滤掉启动的时候，初始化的时候的一次连接没有key，如果有bean去连库数目大于1的库，就需要打印log
                String infos = stackTraces[i].getClassName();
                if (infos.contains("com.baidu.dsp")) {
                    log.error("get data source key fail,will use default data source", e);
                    break;
                }
            }
        } else {// 不是启动的时候报的找不到key，直接打印log和堆栈
            log.error("get data source key fail,will use default data source", e);
            return;
        }
        return;
    }

    public MultiDataSourceKeyContext getMultiDataSourceKeyContext() {
        return multiDataSourceKeyContext;
    }

    public void setMultiDataSourceKeyContext(MultiDataSourceKeyContext multiDataSourceKeyContext) {
        this.multiDataSourceKeyContext = multiDataSourceKeyContext;
    }

}
