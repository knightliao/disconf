package com.baidu.disconf2.client.test.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.client.common.annotations.DisconfFileItem;

/**
 * 1. 服务B，他使用的是 ConfA，需要使用 reload 服务<br/>
 * 2. 该服务需要使用主备切换
 * 
 **/
@Service
public class ServiceB {

    @Autowired
    private ConfA confA;

    @DisconfFileItem
    public int getAValue() {

        return confA.getVarA();
    }

}
