package com.baidu.disconf2.client.common.model;

import java.util.List;

import com.baidu.disconf2.client.common.inter.IDisconfUpdate;

/**
 * 记录配置更新时 或 主备切换时的回调函数
 * 
 * @author liaoqiqi
 * @version 2014-5-22
 */
public class DisconfCommonCallbackModel {

    // 所有用户自己的回调函数(配置更新时)
    private List<IDisconfUpdate> disconfConfUpdates;

    // 所有用户自己的回调函数(主备切换时)
    private List<IDisconfUpdate> disconfUpdatesActiveBackups;

    public List<IDisconfUpdate> getDisconfConfUpdates() {
        return disconfConfUpdates;
    }

    public void setDisconfConfUpdates(List<IDisconfUpdate> disconfConfUpdates) {
        this.disconfConfUpdates = disconfConfUpdates;
    }

    public List<IDisconfUpdate> getDisconfUpdatesActiveBackups() {
        return disconfUpdatesActiveBackups;
    }

    public void setDisconfUpdatesActiveBackups(
            List<IDisconfUpdate> disconfUpdatesActiveBackups) {
        this.disconfUpdatesActiveBackups = disconfUpdatesActiveBackups;
    }

}
