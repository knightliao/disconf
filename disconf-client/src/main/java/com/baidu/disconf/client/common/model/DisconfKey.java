package com.baidu.disconf.client.common.model;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;

/**
 * 用于标识一个配置文件或配置项
 *
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfKey {

    public DisconfKey(DisConfigTypeEnum disConfigTypeEnum, String key) {
        super();
        this.disConfigTypeEnum = disConfigTypeEnum;
        this.key = key;
    }

    // 类型
    private DisConfigTypeEnum disConfigTypeEnum;

    // 文件名或配置项名
    private String key;

    public DisConfigTypeEnum getDisConfigTypeEnum() {
        return disConfigTypeEnum;
    }

    public void setDisConfigTypeEnum(DisConfigTypeEnum disConfigTypeEnum) {
        this.disConfigTypeEnum = disConfigTypeEnum;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "DisconfKey [disConfigTypeEnum=" + disConfigTypeEnum + ", key=" + key + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((disConfigTypeEnum == null) ? 0 : disConfigTypeEnum.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DisconfKey other = (DisconfKey) obj;
        if (disConfigTypeEnum != other.disConfigTypeEnum) {
            return false;
        }
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

}
