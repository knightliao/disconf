package com.baidu.disconf2.demo.main;

import java.lang.reflect.Field;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.common.annotations.DisconfActiveBackupService;
import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;
import com.baidu.disconf2.client.common.annotations.DisconfItem;
import com.baidu.disconf2.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf2.utils.ReflectionsUtils;
import com.google.common.base.Predicate;

/**
 * 
 * @author liaoqiqi
 * @version 2014-5-26
 */
public class ScanBaseUtils {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ScanBaseUtils.class);

    private final String SYSTEM_PACK_STRING = "com.baidu.disconf2";

    private final String USER_PACK_STRING = "com.baidu.disconf2.demomodel";

    private Reflections reflections;

    public Reflections getReflections() {
        return reflections;
    }

    public void setReflections(Reflections reflections) {
        this.reflections = reflections;
    }

    protected static final ScanBaseUtils INSTANCE = new ScanBaseUtils();

    public static ScanBaseUtils getInstance() {
        return INSTANCE;
    }

    public ScanBaseUtils() {

        init();
    }

    /**
     * 
     */
    private void init() {

        Predicate<String> filter = new FilterBuilder().includePackage(
                USER_PACK_STRING).includePackage(SYSTEM_PACK_STRING);

        //
        reflections = new Reflections(new ConfigurationBuilder()
                .filterInputsBy(filter)
                .setScanners(new SubTypesScanner().filterResultsBy(filter),
                        new TypeAnnotationsScanner().filterResultsBy(filter),
                        new FieldAnnotationsScanner().filterResultsBy(filter),
                        new MethodAnnotationsScanner().filterResultsBy(filter),
                        new MethodParameterScanner())
                .setUrls(ClasspathHelper.forPackage(USER_PACK_STRING)));

        //
        // 打印出Store数据
        //
        LOGGER.info("================================= PRINT STORE INFO ======================================");

        ReflectionsUtils.printSotreMap(reflections);
    }

    /**
     * 
     */
    public void printFileItem() {

        //
        // 配置文件
        //
        LOGGER.info("======= 配置文件项 =======");
        Set<Field> af1 = reflections
                .getFieldsAnnotatedWith(DisconfFileItem.class);

        for (Field item : af1) {

            LOGGER.info(item.toString());
            DisconfFileItem disconfFileItem = item
                    .getAnnotation(DisconfFileItem.class);
            LOGGER.info("\tkey: " + disconfFileItem.key());
            LOGGER.info("\tvalue: " + disconfFileItem.defaultValue());
        }
    }

    /**
     * 
     */
    public void printFile() {

        LOGGER.info("======= 配置文件 =======");

        Set<Class<?>> classdata = reflections
                .getTypesAnnotatedWith(DisconfFile.class);

        for (Class<?> item : classdata) {

            LOGGER.info(item.toString());
            DisconfFile disconfFile = item.getAnnotation(DisconfFile.class);
            LOGGER.info("\tfile name: " + disconfFile.filename());
            LOGGER.info("\tenv: " + disconfFile.env());
            LOGGER.info("\tversion: " + disconfFile.env());
        }
    }

    /**
     * 
     */
    public void printItem() {

        LOGGER.info("======= 配置项 =======");
        Set<Field> af1 = reflections.getFieldsAnnotatedWith(DisconfItem.class);

        for (Field item : af1) {

            LOGGER.info(item.toString());
            DisconfItem disconfItem = item.getAnnotation(DisconfItem.class);
            LOGGER.info("\tkey: " + disconfItem.key());
            LOGGER.info("\tvalue: " + disconfItem.defaultValue());
            LOGGER.info("\tenv: " + disconfItem.env());
            LOGGER.info("\tversion: " + disconfItem.version());

        }
    }

    /**
     * 
     */
    public void printActiveBackup() {

        LOGGER.info("======= 主备切换 =======");
        Set<Class<?>> classdata = reflections
                .getTypesAnnotatedWith(DisconfActiveBackupService.class);

        for (Class<?> item : classdata) {

            LOGGER.info(item.toString());
        }
    }

    /**
     * 
     */
    public void printUpdateFile() {

        LOGGER.info("======= 配置更新 =======");
        Set<Class<?>> classdata = reflections
                .getTypesAnnotatedWith(DisconfUpdateService.class);

        for (Class<?> item : classdata) {

            LOGGER.info(item.toString());
        }
    }
}
