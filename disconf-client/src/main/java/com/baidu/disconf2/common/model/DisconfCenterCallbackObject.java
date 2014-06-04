package com.baidu.disconf2.common.model;

import com.baidu.disconf2.common.constants.DisConfigTypeEnum;

/**
 * 收到消息时的回调数据
 * 
 * @author liaoqiqi
 * @version 2014-5-20
 */
public class DisconfCenterCallbackObject {

    //
    // 收到消息的类型
    //
    private DisConfigTypeEnum disConfigTypeEnum;

    //
    // 配置文件数据
    //
    private DisconfCenterFile disconfCenterFile;

    //
    // 配置项数据
    //
    private DisconfCenterItem disconfCenterItem;

    public DisConfigTypeEnum getDisConfigTypeEnum() {
        return disConfigTypeEnum;
    }

    public void setDisConfigTypeEnum(DisConfigTypeEnum disConfigTypeEnum) {
        this.disConfigTypeEnum = disConfigTypeEnum;
    }

    public DisconfCenterFile getDisconfCenterFile() {
        return disconfCenterFile;
    }

    public void setDisconfCenterFile(DisconfCenterFile disconfCenterFile) {
        this.disconfCenterFile = disconfCenterFile;
    }

    public DisconfCenterItem getDisconfCenterItem() {
        return disconfCenterItem;
    }

    public void setDisconfCenterItem(DisconfCenterItem disconfCenterItem) {
        this.disconfCenterItem = disconfCenterItem;
    }

}
