package com.baidu.disconf2.client.test.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.client.common.annotations.DisconfItem;

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
    private int varAA = 10;

    @Autowired
    private ConfA confA;

    public int calcMoneyA() {
        return confA.getVarA() * 10;
    }

    public int calcMoneyA2() {
        return confA.getVarA() * 20;
    }

    @DisconfItem(key = ConfA.keyA)
    public int getVarAA() {
        return varAA;
    }

    public void setVarAA(int varAA) {
        this.varAA = varAA;
    }

}
