package com.baidu.disconf2.client.scan.inner;

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

import com.baidu.disconf2.client.common.annotations.DisconfActiveBackupService;
import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;
import com.baidu.disconf2.client.common.annotations.DisconfItem;
import com.baidu.disconf2.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf2.client.common.constants.Constants;
import com.google.common.base.Predicate;

/**
 * 
 * 一个专门处理扫描的类
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class ScanPack {

    /**
     * 通过扫描，获取反射对象
     * 
     * @param packName
     * @return
     */
    private static Reflections getReflection(String packName) {

        Predicate<String> filter = new FilterBuilder().includePackage(
                Constants.DISCONF_PACK_NAME).includePackage(packName);

        //
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .filterInputsBy(filter)
                .setScanners(new SubTypesScanner().filterResultsBy(filter),
                        new TypeAnnotationsScanner().filterResultsBy(filter),
                        new FieldAnnotationsScanner().filterResultsBy(filter),
                        new MethodAnnotationsScanner().filterResultsBy(filter),
                        new MethodParameterScanner())
                .setUrls(ClasspathHelper.forPackage(packName)));

        return reflections;
    }

    /**
     * 扫描想要的类
     * 
     * @return
     */
    public static ScanModel scan(String packName) {

        ScanModel scanModel = new ScanModel();

        // 扫描对象
        Reflections reflections = getReflection(packName);
        scanModel.setReflections(reflections);

        //
        // 获取DisconfFile
        //
        Set<Class<?>> classdata = reflections
                .getTypesAnnotatedWith(DisconfFile.class);
        scanModel.setDisconfFileSet(classdata);

        //
        // 获取DisconfFileItem
        //
        Set<Field> af1 = reflections
                .getFieldsAnnotatedWith(DisconfFileItem.class);
        scanModel.setDisconfFileItemSet(af1);

        //
        // 获取DisconfItem
        //
        af1 = reflections.getFieldsAnnotatedWith(DisconfItem.class);
        scanModel.setDisconfItemSet(af1);

        //
        // 获取DisconfActiveBackupService
        //
        classdata = reflections
                .getTypesAnnotatedWith(DisconfActiveBackupService.class);
        scanModel.setDisconfActiveBackupServiceSet(classdata);

        //
        // 获取DisconfActiveBackupService
        //
        classdata = reflections
                .getTypesAnnotatedWith(DisconfUpdateService.class);
        scanModel.setDisconfUpdateService(classdata);

        return scanModel;
    }

}
