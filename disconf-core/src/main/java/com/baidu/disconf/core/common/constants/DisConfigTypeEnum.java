package com.baidu.disconf.core.common.constants;

/**
 * 配置类型(配置文件,配置项)
 *
 * @author liaoqiqi
 * @version 2014-5-16
 */
public enum DisConfigTypeEnum {

    FILE(0, "配置文件"), ITEM(1, "配置项");

    private int type = 0;
    private String modelName = null;

    private DisConfigTypeEnum(int type, String modelName) {
        this.type = type;
        this.modelName = modelName;
    }

    public static DisConfigTypeEnum getByType(int type) {

        int index = 0;
        for (DisConfigTypeEnum disConfigTypeEnum : DisConfigTypeEnum.values()) {

            if (type == index) {
                return disConfigTypeEnum;
            }

            index++;
        }

        return null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

}
