package com.baidu.disconf2.client.test.model;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;
import com.baidu.disconf2.client.common.annotations.DisconfItem;

/**
 * 1. 分布式配置文件，fileName 是配置文件名<br/>
 * 2. static变量示例 <br/>
 * 3. 使用自动注入方式
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
    private static int varAA = 10;

    public static int getVarA() {
        return varA;
    }

    public static void setVarA(int varA) {
        ConfA.varA = varA;
    }

    public static int getVarAA() {
        return varAA;
    }

    public static void setVarAA(int varAA) {
        ConfA.varAA = varAA;
    }

}
