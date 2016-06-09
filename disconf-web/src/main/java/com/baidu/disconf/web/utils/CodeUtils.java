package com.baidu.disconf.web.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by knightliao on 15/1/7.
 */
public class CodeUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(CodeUtils.class);

    /**
     * utf-8 转换成 unicode
     */
    public static String utf8ToUnicode(String theStr) {
        return StringEscapeUtils.escapeJava(theStr);
    }

    /**
     * unicode 转换成 utf-8
     */
    public static String unicodeToUtf8(String theStr) {
        return StringEscapeUtils.unescapeJava(theStr);
    }
}
