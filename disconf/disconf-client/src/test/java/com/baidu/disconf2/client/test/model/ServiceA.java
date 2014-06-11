package com.baidu.disconf2.client.test.model;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private ConfB confB;

    public int calcMoneyA() {
        return confA.getVarA() * 10;
    }

    public int calcMoneyB() {
        return confB.getVarB() * 100;
    }

}
