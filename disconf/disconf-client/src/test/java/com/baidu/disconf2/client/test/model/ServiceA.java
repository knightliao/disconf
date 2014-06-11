package com.baidu.disconf2.client.test.model;

/**
 * 服务A，他使用的是 ConfA，使用配置时是直接使用，并没有将配置注入到服务里
 * 
 * @author liaoqiqi
 * @version 2014-5-16
 */
public class ServiceA {

    public int calcMoneyA() {
        return ConfA.getVarA() * 10;
    }

    public int calcMoneyB() {
        return ConfB.getInstance().getVarB() * 100;
    }

}
