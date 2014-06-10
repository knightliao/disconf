package com.baidu.disconf2.client.test.scan.inner;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.scan.inner.ScanModel;
import com.baidu.disconf2.client.scan.inner.ScanPack;
import com.baidu.disconf2.utils.ScanPrinterUtils;

public class ScanPackTestCase {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ScanPackTestCase.class);

    @Test
    public void scan() {

        String packName = "com.baidu.disconf2.client";

        try {

            ScanModel scanModel = ScanPack.scan(packName);

            // PRINT SCAN STORE
            ScanPrinterUtils.printSotreMap(scanModel.getReflections());

            // disconf file item
            LOGGER.info("=============DISCONF FILE ITEM===================");
            Set<Field> fields = scanModel.getDisconfFileItemFieldSet();
            ScanPrinterUtils.printFileItem(fields);
            Assert.assertEquals(2, fields.size());

            // disconf file item
            LOGGER.info("=============DISCONF FILE===================");
            Map<Class<?>, Set<Field>> fileMap = scanModel
                    .getDisconfFileItemMap();
            ScanPrinterUtils.printFileMap(fileMap);
            Assert.assertEquals(2, fileMap.size());

            // disconf item
            LOGGER.info("=============DISCONF ITEM===================");
            fields = scanModel.getDisconfItemFieldSet();
            ScanPrinterUtils.printItem(scanModel.getDisconfItemFieldSet());
            Assert.assertEquals(1, fields.size());

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
            Assert.assertEquals(1, classSet.size());

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
