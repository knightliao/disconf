package com.example.disconf.demo.service.callbacks;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.example.disconf.demo.config.TestXmlConfig;

/**
 * 如果你的回调类里没有 进行autowired ，那么你的回调类可以是非 spring托管的
 */
@DisconfUpdateService(classes = {TestXmlConfig.class}, confFileKeys = {"testJson.json"})
public class TestXmlConfigCallback implements IDisconfUpdate {

    public void reload() throws Exception {

        System.out.println("now i'm at xml update callback ");
    }

}
