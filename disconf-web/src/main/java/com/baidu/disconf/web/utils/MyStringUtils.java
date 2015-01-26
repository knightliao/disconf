/*
 * Copyright (C) 2015 KNIGHT, Inc. All Rights Reserved.
 */
package com.baidu.disconf.web.utils;

/**
 * Created by knightliao on 15/1/26.
 */
public class MyStringUtils {

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
