package com.baidu.disconf.client.test.scan.inner;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.scan.inner.ScanStatic;
import com.baidu.disconf.client.scan.inner.model.ScanStaticModel;
import com.baidu.disconf.client.test.common.BaseTestCase;
import com.baidu.disconf.utils.ScanPrinterUtils;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class ScanPackTestCase extends BaseTestCase {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ScanPackTestCase.class);

    @Test
    public void scan() {

        String packName = "com.baidu.disconf2.client.test";

        try {

            ScanStaticModel scanModel = ScanStatic.scan(packName);

            // PRINT SCAN STORE
            ScanPrinterUtils.printSotreMap(scanModel.getReflections());

            // disconf file item
            LOGGER.info("=============DISCONF FILE ITEM===================");
            Set<Method> methods = scanModel.getDisconfFileItemMethodSet();
            ScanPrinterUtils.printFileItemMethod(methods);
            Assert.assertEquals(2, methods.size());

            // disconf file item
            LOGGER.info("=============DISCONF FILE===================");
            Map<Class<?>, Set<Method>> fileMap = scanModel
                    .getDisconfFileItemMap();
            Assert.assertEquals(1, fileMap.size());

            // disconf item
            LOGGER.info("=============DISCONF ITEM===================");
            methods = scanModel.getDisconfItemMethodSet();
            ScanPrinterUtils.printFileItemMethod(methods);
            Assert.assertEquals(1, methods.size());

            // Active backup
            LOGGER.info("=============DISCONF ACTIVE BACKUP===================");
            Set<Class<?>> classSet = scanModel
                    .getDisconfActiveBackupServiceClassSet();
            ScanPrinterUtils.printActiveBackup(classSet);
            Assert.assertEquals(0, classSet.size());

            // Update service
            LOGGER.info("=============DISCONF Update service===================");
            classSet = scanModel.getDisconfUpdateService();
            ScanPrinterUtils.printUpdateFile(classSet);
            Assert.assertEquals(2, classSet.size());

        } catch (Exception e) {

            Assert.assertTrue(false);
        }
    }
}
