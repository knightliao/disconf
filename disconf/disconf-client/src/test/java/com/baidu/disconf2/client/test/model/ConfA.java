package com.baidu.disconf2.client.test.model;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;
import com.baidu.disconf2.client.common.annotations.DisconfItem;

/**
 * 1. 分布式配置文件，fileName 是配置文件名<br/>
 * 2. static变量的示例
 * 
 * 
 **/
@DisconfFile(filename = ConfA.filename)
public class ConfA {

    public static final String filename = "confA.properties";
    public static final String keyA = "keyA";

    /**
     * 配置文件中的某Item
     */
    @DisconfFileItem(key = "varA", defaultValue = "5")
    private static int varA;

    /**
     * 1. 分布式配置项，keyB是其全局Key名<br/>
     */
    @DisconfItem(key = ConfA.keyA, defaultValue = "10")
    private static int varB;

    public static int getVarA() {
        return varA;
    }

    public static void setVarA(int varA) {
        ConfA.varA = varA;
    }

    public static int getVarB() {
        return varB;
    }

    public static void setVarB(int varB) {
        ConfA.varB = varB;
    }

}
