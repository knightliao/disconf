package com.baidu.ub.common.dbmanage.monitor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.jdbc.core.JdbcTemplate;

import com.baidu.ub.common.dbmanage.datasource.VirtualDataSource;

/**
 * @author wangxj
 */
public class DataSourceMonitorTask extends ApplicationObjectSupport {

    protected static final Logger log = LoggerFactory.getLogger(DataSourceMonitorTask.class);

    @Resource
    private List<VirtualDataSource> dataSourceList;

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void checkDataSource() {
        for (VirtualDataSource vdb : dataSourceList) {
            List<String> mkeys = vdb.getMasterSet();
            if (CollectionUtils.isNotEmpty(mkeys)) {
                List<String> masterKeys = new ArrayList<String>();
                for (String dbKey : mkeys) {
                    log.info("check datasource:" + dbKey);
                    DataSource ds = getApplicationContext().getBean(dbKey, DataSource.class);
                    if (isConnect(ds)) {
                        masterKeys.add(dbKey);
                    }
                }
                if (needChange(vdb.getMasterPools(), masterKeys)) {
                    log.warn("changed ds master pool:" + masterKeys);
                    vdb.setMasterPools(masterKeys);
                }
            }

            List<String> skeys = vdb.getSlaveSet();
            if (CollectionUtils.isNotEmpty(skeys)) {
                List<String> slaveKeys = new ArrayList<String>();
                for (String dbKey : skeys) {
                    log.info("check datasource:" + dbKey);
                    DataSource ds = getApplicationContext().getBean(dbKey, DataSource.class);
                    if (isConnect(ds)) {
                        slaveKeys.add(dbKey);
                    }
                }
                if (needChange(vdb.getSlavePools(), slaveKeys)) {
                    log.warn("changed ds slave pool:" + slaveKeys);
                    vdb.setSlavePools(slaveKeys);
                }
            }

        }
        log.info("check datasource done");
    }

    private boolean isConnect(DataSource dataSource) {
        jdbcTemplate.setDataSource(dataSource);
        try {
            jdbcTemplate.queryForObject("select now() as nowTime", Date.class);
        } catch (Throwable t) {
            return false;
        }
        return true;
    }

    private boolean needChange(List<String> usedList, List<String> newList) {
        if (newList == null) {
            log.error("all datasource is out of connection!!!");
            return false;
        }
        if (usedList.size() != newList.size()) {
            return true;
        }
        for (String key : usedList) {
            if (!newList.contains(key)) {
                return true;
            }
        }
        return false;
    }

    public List<VirtualDataSource> getDataSourceList() {
        return dataSourceList;
    }

    public void setDataSourceList(List<VirtualDataSource> dataSourceList) {
        this.dataSourceList = dataSourceList;
    }

}
