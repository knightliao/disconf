package com.baidu.disconf2.common.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.disconf2.common.constants.DisConfigTypeEnum;

/**
 * 配置文件表示
 * 
 * @author liaoqiqi
 * @version 2014-5-20
 */
public class DisconfCenterFile {

    // -----key: 配置文件中的项名
    // -----value: 默认值
    private Map<String, String> keyMaps = new HashMap<String, String>();

    // 配置文件类
    private Class<Object> cls;

    // 域列表
    private List<Field> fields = new ArrayList<Field>();

    // 文件名
    private String fileName;

    // 通用配置
    private DisConfCommonModel disConfCommonModel;

    // 回调函数
    private DisconfCommonCallbackModel disconfCommonCallbackModel;

    // 配置占用类型
    private DisConfigTypeEnum disConfigTypeEnum;

    public Map<String, String> getKeyMaps() {
        return keyMaps;
    }

    public void setKeyMaps(Map<String, String> keyMaps) {
        this.keyMaps = keyMaps;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public DisConfCommonModel getDisConfCommonModel() {
        return disConfCommonModel;
    }

    public void setDisConfCommonModel(DisConfCommonModel disConfCommonModel) {
        this.disConfCommonModel = disConfCommonModel;
    }

    public Class<Object> getCls() {
        return cls;
    }

    public void setCls(Class<Object> cls) {
        this.cls = cls;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DisConfigTypeEnum getDisConfigTypeEnum() {
        return disConfigTypeEnum;
    }

    public void setDisConfigTypeEnum(DisConfigTypeEnum disConfigTypeEnum) {
        this.disConfigTypeEnum = disConfigTypeEnum;
    }

    public DisconfCommonCallbackModel getDisconfCommonCallbackModel() {
        return disconfCommonCallbackModel;
    }

    public void setDisconfCommonCallbackModel(
            DisconfCommonCallbackModel disconfCommonCallbackModel) {
        this.disconfCommonCallbackModel = disconfCommonCallbackModel;
    }

}
