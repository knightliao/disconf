package com.baidu.disconf2.client.test.model;

import org.springframework.stereotype.Service;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;
import com.baidu.disconf2.client.common.annotations.DisconfItem;

/**
 * 1. 分布式配置文件，fileName 是配置文件名<br/>
 * 2. 使用Spring Bean托管方式
 * 
 **/
@Service
@DisconfFile(filename = ConfA.filename)
public class ConfA {

    public static final String filename = "confA.properties";
    public static final String keyA = "keyA";

    /**
     * 配置文件中的某Item
     */
    private int varA = 15;
    
    /**
     * 配置文件中的某Item
     */
    private int varA2 = 25;

    /**
     * 1. 分布式配置项，keyB是其全局Key名<br/>
     */
    private int varAA = 10;

    @DisconfFileItem
    public int getVarA() {
        return varA;
    }

    public void setVarA(int varA) {
        this.varA = varA;
    }

    @DisconfItem(key = ConfA.keyA)
    public int getVarAA() {
        return varAA;
    }

    public void setVarAA(int varAA) {
        this.varAA = varAA;
    }
    
    @DisconfFileItem
    public int getVarA2() {
        return varA2;
    }

    public void setVarA2(int varA2) {
        this.varA2 = varA2;
    }

}
