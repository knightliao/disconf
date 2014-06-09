package com.baidu.disconf2.client.scan.inner;

import java.lang.reflect.Field;
import java.util.Set;

import org.reflections.Reflections;

/**
 * 扫描存储的对象
 * 
 * @author liaoqiqi
 * @version 2014-6-9
 */
public class ScanModel {

    private Reflections reflections;

    private Set<Class<?>> disconfFileSet;

    private Set<Field> disconfFileItemSet;

    private Set<Field> disconfItemSet;

    private Set<Class<?>> disconfActiveBackupServiceSet;

    private Set<Class<?>> disconfUpdateService;

    public Set<Class<?>> getDisconfFileSet() {
        return disconfFileSet;
    }

    public void setDisconfFileSet(Set<Class<?>> disconfFileSet) {
        this.disconfFileSet = disconfFileSet;
    }

    public Set<Field> getDisconfFileItemSet() {
        return disconfFileItemSet;
    }

    public void setDisconfFileItemSet(Set<Field> disconfFileItemSet) {
        this.disconfFileItemSet = disconfFileItemSet;
    }

    public Set<Field> getDisconfItemSet() {
        return disconfItemSet;
    }

    public void setDisconfItemSet(Set<Field> disconfItemSet) {
        this.disconfItemSet = disconfItemSet;
    }

    public Set<Class<?>> getDisconfActiveBackupServiceSet() {
        return disconfActiveBackupServiceSet;
    }

    public void setDisconfActiveBackupServiceSet(
            Set<Class<?>> disconfActiveBackupServiceSet) {
        this.disconfActiveBackupServiceSet = disconfActiveBackupServiceSet;
    }

    public Set<Class<?>> getDisconfUpdateService() {
        return disconfUpdateService;
    }

    public void setDisconfUpdateService(Set<Class<?>> disconfUpdateService) {
        this.disconfUpdateService = disconfUpdateService;
    }

    public Reflections getReflections() {
        return reflections;
    }

    public void setReflections(Reflections reflections) {
        this.reflections = reflections;
    }

}
