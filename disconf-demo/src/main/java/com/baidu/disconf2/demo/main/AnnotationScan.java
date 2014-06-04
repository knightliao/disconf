package com.baidu.disconf2.demo.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author liaoqiqi
 * @version 2014-5-23
 */
public class AnnotationScan {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AnnotationScan.class);

    public static void main(String[] args) {

        printAnnotation();
    }

    /**
     * ANNOTATION
     */
    public static void printAnnotation() {

        LOGGER.info("================================= PRINT ANNOTATION ======================================");

        //
        // 配置文件
        //
        ScanBaseUtils.getInstance().printFile();

        //
        // 配置文件
        //
        ScanBaseUtils.getInstance().printFileItem();

        //
        // 配置项
        //
        ScanBaseUtils.getInstance().printItem();

        //
        // 主备切换
        //
        ScanBaseUtils.getInstance().printActiveBackup();

        //
        // 配置更新
        //
        ScanBaseUtils.getInstance().printUpdateFile();
    }
}
