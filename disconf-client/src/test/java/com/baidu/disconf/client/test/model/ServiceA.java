package com.baidu.disconf.client.test.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfItem;

/**
 * 服务A，他使用的是 ConfA
 *
 * @author liaoqiqi
 * @version 2014-5-16
 */
@Service
public class ServiceA {

    /**
     * 1. 分布式配置项，keyB是其全局Key名<br/>
     */
    @Value(value = "10")
    private int varAA;

    public static final String keyA = "keyA";

    @Autowired
    private ConfA confA;

    public long calcMoneyA() {
        return confA.getVarA();
    }

    public long calcMoneyA2() {
        return confA.getVarA2();
    }

    @DisconfItem(key = ServiceA.keyA)
    public int getVarAA() {
        return varAA;
    }

    public void setVarAA(int varAA) {
        this.varAA = varAA;
    }

}
