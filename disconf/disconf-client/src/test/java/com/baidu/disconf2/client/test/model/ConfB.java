package com.baidu.disconf2.client.test.model;

import org.springframework.stereotype.Service;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;

/**
 * 1. 分布式配置文件，fileName 是配置文件名<br/>
 * 2. 使用Bean托管
 * 
 **/

@DisconfFile(filename = ConfB.filename)
@Service
public class ConfB {

    public static final String filename = "confB.properties";
    public static final String keyA = "keyA";

    private int varB = 5;

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
