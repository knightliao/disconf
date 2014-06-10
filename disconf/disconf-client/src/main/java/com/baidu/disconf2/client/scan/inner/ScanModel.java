package com.baidu.disconf2.client.scan.inner;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.baidu.disconf2.client.common.inter.IDisconfUpdate;

/**
 * 扫描存储的对象
 * 
 * @author liaoqiqi
 * @version 2014-6-9
 */
public class ScanModel {

    private Reflections reflections;

    //
    // 配置文件
    private Set<Class<?>> disconfFileClassSet;

    private Set<Field> disconfFileItemFieldSet;

    //
    // 配置文件MAP, KEY为配置文件
    private Map<Class<?>, Set<Field>> disconfFileItemMap;

    //
    // 配置ITEM
    private Set<Field> disconfItemFieldSet;

    //
    //
    private Set<Class<?>> disconfActiveBackupServiceClassSet;

    //
    // 更新 回调函数
    private Set<Class<?>> disconfUpdateService;
    // 配置及它影响的回调函数
    private Map<String, List<IDisconfUpdate>> disconfUpdateServiceInverseIndexMap;

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

    public Set<Field> getDisconfFileItemFieldSet() {
        return disconfFileItemFieldSet;
    }

    public void setDisconfFileItemFieldSet(Set<Field> disconfFileItemFieldSet) {
        this.disconfFileItemFieldSet = disconfFileItemFieldSet;
    }

    public Map<Class<?>, Set<Field>> getDisconfFileItemMap() {
        return disconfFileItemMap;
    }

    public void setDisconfFileItemMap(
            Map<Class<?>, Set<Field>> disconfFileItemMap) {
        this.disconfFileItemMap = disconfFileItemMap;
    }

    public Set<Field> getDisconfItemFieldSet() {
        return disconfItemFieldSet;
    }

    public void setDisconfItemFieldSet(Set<Field> disconfItemFieldSet) {
        this.disconfItemFieldSet = disconfItemFieldSet;
    }

    public Set<Class<?>> getDisconfActiveBackupServiceClassSet() {
        return disconfActiveBackupServiceClassSet;
    }

    public void setDisconfActiveBackupServiceClassSet(
            Set<Class<?>> disconfActiveBackupServiceClassSet) {
        this.disconfActiveBackupServiceClassSet = disconfActiveBackupServiceClassSet;
    }

    public Set<Class<?>> getDisconfUpdateService() {
        return disconfUpdateService;
    }

    public void setDisconfUpdateService(Set<Class<?>> disconfUpdateService) {
        this.disconfUpdateService = disconfUpdateService;
    }

    public Map<String, List<IDisconfUpdate>> getDisconfUpdateServiceInverseIndexMap() {
        return disconfUpdateServiceInverseIndexMap;
    }

    public void setDisconfUpdateServiceInverseIndexMap(
            Map<String, List<IDisconfUpdate>> disconfUpdateServiceInverseIndexMap) {
        this.disconfUpdateServiceInverseIndexMap = disconfUpdateServiceInverseIndexMap;
    }

}
