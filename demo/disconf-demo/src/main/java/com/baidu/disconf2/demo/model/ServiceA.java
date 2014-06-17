package com.baidu.disconf2.demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 服务A，他使用的是 ConfA，使用配置时是直接使用，并没有将配置注入到服务里
 * 
 * @author liaoqiqi
 * @version 2014-5-16
 */
@Service
public class ServiceA {

    @Autowired
    private ConfA confA;

    public int calcMoney() {
        return confA.getVarA() * 10;
    }

}
