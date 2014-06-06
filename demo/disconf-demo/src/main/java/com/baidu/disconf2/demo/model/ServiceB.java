package com.baidu.disconf2.demo.model;

import org.springframework.stereotype.Service;

/**
 * 1. 服务B，他使用的是 ConfA，使用配置是直接注入到服务中，<br/>
 * 这样需要使用 reload 服务<br/>
 * 2. 该服务需要使用主备切换
 * 
 **/
@Service
public class ServiceB {

    private ModelB modelB = null;

    public ServiceB() {
        modelB = new ModelB(ConfA.getVarA());
    }

    public int getAValue() {

        return modelB.getBb();
    }

}
