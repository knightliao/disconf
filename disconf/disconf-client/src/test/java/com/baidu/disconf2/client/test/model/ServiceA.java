package com.baidu.disconf2.client.test.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 服务A，他使用的是 ConfA
 * 
 * @author liaoqiqi
 * @version 2014-5-16
 */
@Service
public class ServiceA {

    @Autowired
    private ConfA confA;

    public int calcMoneyA() {
        return confA.getVarA() * 10;
    }

    public int calcMoneyA2() {
        return confA.getVarA() * 20;
    }
}
