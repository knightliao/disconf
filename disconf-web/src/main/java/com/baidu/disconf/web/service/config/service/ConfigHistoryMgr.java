package com.baidu.disconf.web.service.config.service;

/**
 * Created by knightliao on 15/12/25.
 */
public interface ConfigHistoryMgr {

    void createOne(Long configId, String oldValue, String newValue);
}
