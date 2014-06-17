package com.baidu.disconf2.demo.model;

import org.springframework.stereotype.Service;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;

/**
 * 1. 分布式配置文件，fileName 是配置文件名<br/>
 * 
 **/
@Service
@DisconfFile(filename = ConfB.filename)
public class ConfB {

    public static final String filename = "confB.properties";

    private int varB;

    /**
     * 配置文件中的某Item
     */
    @DisconfFileItem
    public int getVarB() {
        return varB;
    }

    public void setVarB(int varB) {
        this.varB = varB;
    }
}
