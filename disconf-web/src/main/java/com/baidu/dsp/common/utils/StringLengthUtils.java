package com.baidu.dsp.common.utils;

import com.baidu.dsp.common.constraint.ChineseLengthConstrant;

public class StringLengthUtils {

    /**
     * 获取中文字符串长度 中文算N个字符(N由{@link ChineseLengthConstrant#cnHoldLength()}指定,
     * 默认为2),英文算一个
     *
     * @param value
     *
     * @return
     */
    public static long getChineseLength(String value, int chineseHoldLength) {
        long valueLength = 0;
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            char temp = value.charAt(i);
            /* 判断是否为中文字符 */
            if ((temp >= '\u4e00' && temp <= '\u9fa5') || (temp >= '\ufe30' && temp <= '\uffa0')) {
                /* 中文长度倍数 */
                valueLength += chineseHoldLength;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 获取中文字符串长度 中文算N个字符(N由{@link ChineseLengthConstrant#cnHoldLength()}指定,
     * 默认为2),英文算一个
     *
     * @param value
     *
     * @return
     */
    public static long getChineseLength(String value) {
        return getChineseLength(value, 2);
    }

}
