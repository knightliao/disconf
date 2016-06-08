package com.baidu.disconf.web.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * Created by knightliao on 15/1/7.
 */
public class CodeUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(CodeUtils.class);

    public static String utf8ToUnicode(String theStr) {
        return StringEscapeUtils.escapeJava(theStr);
    }

    public static String unicodeToUtf8(String theStr) {
        return StringEscapeUtils.unescapeJava(theStr);
    }

}
