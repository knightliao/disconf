package com.baidu.disconf2.demo.model;

import org.springframework.stereotype.Service;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;
import com.baidu.disconf2.client.common.annotations.DisconfItem;

/**
 * 1. 分布式配置文件，fileName 是配置文件名<br/>
 * 
 **/
@Service
@DisconfFile(filename = ConfA.filename)
public class ConfA {

    public static final String filename = "confA.properties";
    public static final String keyA = "confAItem";


    private int varA;


    private int confAItem;

    /**
     * 配置文件中的某Item
     */
    @DisconfFileItem
    public int getVarA() {
        return varA;
    }

    public void setVarA(int varA) {
        this.varA = varA;
    }

    /**
     * 1. 分布式配置项，keyB是其全局Key名<br/>
     */
    @DisconfItem(key = ConfA.keyA)
    public int getConfAItem() {
        return confAItem;
    }

    public void setConfAItem(int confAItem) {
        this.confAItem = confAItem;
    }
}
