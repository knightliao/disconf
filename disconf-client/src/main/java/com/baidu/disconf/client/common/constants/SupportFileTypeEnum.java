package com.baidu.disconf.client.common.constants;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 支持的文件后缀类型
 * 
 * @author knightliao
 * 
 */
public enum SupportFileTypeEnum {

    PROPERTIES(0, "properties"), XML(1, "xml");

    private int type = 0;
    private String modelName = null;

    private SupportFileTypeEnum(int type, String modelName) {
        this.type = type;
        this.modelName = modelName;
    }

    /**
     * 
     * 根据文件名返回其文件后缀ENUM
     * 
     * @param supportFileTypeEnum
     * @return
     */
    public static SupportFileTypeEnum getByFileName(String fileName) {

        String extension = FilenameUtils.getExtension(fileName);
        if (StringUtils.isEmpty(extension)) {
            return null;
        }

        for (SupportFileTypeEnum supportFileTypeEnum : SupportFileTypeEnum.values()) {

            if (extension.equals(supportFileTypeEnum.modelName)) {
                return supportFileTypeEnum;
            }
        }

        return null;
    }

    public static SupportFileTypeEnum getByType(int type) {

        int index = 0;
        for (SupportFileTypeEnum supportFileTypeEnum : SupportFileTypeEnum.values()) {

            if (type == index) {
                return supportFileTypeEnum;
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
