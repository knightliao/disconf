package com.baidu.disconf2.client.test.model;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;
import com.baidu.disconf2.client.common.annotations.DisconfItem;

/**
 * 1. 分布式配置文件，fileName 是配置文件名<br/>
 * 2. 必须是static变量
 * 
 **/
@DisconfFile(filename = ConfA.filename)
public class ConfA {

    public static final String filename = "confA.properties";
    public static final String keyA = "keyA";

    /**
     * 配置文件中的某Item
     */
    @DisconfFileItem
    private static int varA = 15;

    /**
     * 1. 分布式配置项，keyB是其全局Key名<br/>
     */
    @DisconfItem(key = ConfA.keyA)
    private static int varB = 10;

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
