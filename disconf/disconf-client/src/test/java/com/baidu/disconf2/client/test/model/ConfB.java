package com.baidu.disconf2.client.test.model;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;

/**
 * 1. 分布式配置文件，fileName 是配置文件名<br/>
 * 2. 必须是static变量
 * 
 **/
@DisconfFile(filename = ConfB.filename)
public class ConfB {

    public static final String filename = "confB.properties";
    public static final String keyA = "keyA";

    /**
     * 配置文件中的某Item
     */
    @DisconfFileItem
    private static int varA = 5;

    public static int getVarA() {
        return varA;
    }

    public static void setVarA(int varA) {
        ConfB.varA = varA;
    }

}
