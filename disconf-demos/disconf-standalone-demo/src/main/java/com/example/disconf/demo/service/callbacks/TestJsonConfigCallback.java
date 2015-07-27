package com.example.disconf.demo.service.callbacks;

import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

@Service
@DisconfUpdateService(confFileKeys = {"testJson.json"})
public class TestJsonConfigCallback implements IDisconfUpdate {

    public void reload() throws Exception {

        System.out.println("now i'm at xml update callback ");
    }

}
