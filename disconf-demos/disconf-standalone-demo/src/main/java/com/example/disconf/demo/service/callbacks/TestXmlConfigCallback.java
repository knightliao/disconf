package com.example.disconf.demo.service.callbacks;

import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.example.disconf.demo.config.TestXmlConfig;

@Service
@DisconfUpdateService(classes = {TestXmlConfig.class}, confFileKeys = {"testJson.json"})
public class TestXmlConfigCallback implements IDisconfUpdate {

    public void reload() throws Exception {

        System.out.println("now i'm at xml update callback ");
    }

}
