package com.baidu.disconf2.client.scan.inner;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.baidu.disconf2.client.common.constants.Constants;
import com.google.common.base.Predicate;

/**
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
    public static Reflections getReflection(String packName) {

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
}
