package com.baidu.disconf.web.test.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * @author liaoqiqi
 * @version 2014-1-23
 */
public abstract class AbstractTestCase extends AbstractTransactionalJUnit4SpringContextTests {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractTestCase.class);

    /**
     * 业务数据库
     */
    @Autowired
    @Qualifier(value = "dataSource2")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final String TEST_DATA_SQL_BASE_PATH = "sql/testdata/1_0_0/";

    /**
     * 根据测试文件名获取测试文件位置
     *
     * @param fileName
     *
     * @return
     */
    public static String getSQLFullPath(String fileName) {
        return TEST_DATA_SQL_BASE_PATH + fileName;
    }

    @Before
    public void mockLogin() {

    }

    /**
     * 把一个bean的某个属性mock成mockTo类
     *
     * @param bean
     * @param sField
     * @param mockTo
     *
     * @return
     */
    public Object mock(Object bean, String sField, Object mockTo) {

        // 做对象的mock
        Field field = null;
        try {
            field = bean.getClass().getDeclaredField(sField);
        } catch (Exception e) {
            try {
                field = bean.getClass().getField(sField);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        field.setAccessible(true);
        Object mocked = null;
        try {
            mocked = field.get(bean);
            field.set(bean, mockTo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mocked;
    }

    @Test
    public void pass() {
    }

    /**
     * 执行SQL文件
     *
     * @throws IOException
     */
    public String execute(String sqlFileName) throws IOException {

        super.executeSqlScript(sqlFileName, true);
        URL url = ClassLoader.getSystemResource(sqlFileName);
        LOG.info("read file: " + url.toString());

        if (new File(url.toString()).exists()) {
            return FileUtils.readFileToString(new File(url.getFile()));
        }
        return null;
    }

}
