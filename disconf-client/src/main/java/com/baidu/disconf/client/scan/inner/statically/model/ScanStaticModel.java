package com.baidu.disconf.client.scan.inner.statically.model;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

/**
 * 扫描静态存储的对象
 * 
 * @author liaoqiqi
 * @version 2014-6-9
 */
public class ScanStaticModel {

    private Reflections reflections;

    //
    // 配置文件
    private Set<Class<?>> disconfFileClassSet;

    // 配置文件中的函数
    private Set<Method> disconfFileItemMethodSet;

    //
    // 配置文件及其函数的MAP, KEY为配置文件类
    private Map<Class<?>, Set<Method>> disconfFileItemMap;

    //
    // 配置ITEM
    private Set<Method> disconfItemMethodSet;

    //
    // 主从切换的回调函数类
    private Set<Class<?>> disconfActiveBackupServiceClassSet;

    //
    // 更新 回调函数类
    private Set<Class<?>> disconfUpdateService;

    // 只是托管的配置文件，没有注入到类中
    private Set<String> justHostFiles;

    public Reflections getReflections() {
        return reflections;
    }

    public void setReflections(Reflections reflections) {
        this.reflections = reflections;
    }

    public Set<Class<?>> getDisconfFileClassSet() {
        return disconfFileClassSet;
    }

    public void setDisconfFileClassSet(Set<Class<?>> disconfFileClassSet) {
        this.disconfFileClassSet = disconfFileClassSet;
    }

    public Map<Class<?>, Set<Method>> getDisconfFileItemMap() {
        return disconfFileItemMap;
    }

    public void setDisconfFileItemMap(Map<Class<?>, Set<Method>> disconfFileItemMap) {
        this.disconfFileItemMap = disconfFileItemMap;
    }

    public Set<Method> getDisconfItemMethodSet() {
        return disconfItemMethodSet;
    }

    public void setDisconfItemMethodSet(Set<Method> disconfItemMethodSet) {
        this.disconfItemMethodSet = disconfItemMethodSet;
    }

    public Set<Method> getDisconfFileItemMethodSet() {
        return disconfFileItemMethodSet;
    }

    public void setDisconfFileItemMethodSet(Set<Method> disconfFileItemMethodSet) {
        this.disconfFileItemMethodSet = disconfFileItemMethodSet;
    }

    public Set<Class<?>> getDisconfActiveBackupServiceClassSet() {
        return disconfActiveBackupServiceClassSet;
    }

    public void setDisconfActiveBackupServiceClassSet(Set<Class<?>> disconfActiveBackupServiceClassSet) {
        this.disconfActiveBackupServiceClassSet = disconfActiveBackupServiceClassSet;
    }

    public Set<Class<?>> getDisconfUpdateService() {
        return disconfUpdateService;
    }

    public void setDisconfUpdateService(Set<Class<?>> disconfUpdateService) {
        this.disconfUpdateService = disconfUpdateService;
    }

    @Override
    public String toString() {
        return "ScanStaticModel [reflections=" + reflections + ", disconfFileClassSet=" + disconfFileClassSet
                + ", disconfFileItemMethodSet=" + disconfFileItemMethodSet + ", disconfFileItemMap="
                + disconfFileItemMap + ", disconfItemMethodSet=" + disconfItemMethodSet
                + ", disconfActiveBackupServiceClassSet=" + disconfActiveBackupServiceClassSet
                + ", disconfUpdateService=" + disconfUpdateService + ", justHostFiles=" + justHostFiles + "]";
    }

    public Set<String> getJustHostFiles() {
        return justHostFiles;
    }

    public void setJustHostFiles(Set<String> justHostFiles) {
        this.justHostFiles = justHostFiles;
    }

}
