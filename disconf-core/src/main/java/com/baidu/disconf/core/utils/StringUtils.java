package com.baidu.disconf.core.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author liaoqiqi
 * 
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(StringUtils.class);

    /**
     * 
     * @param source
     * @param token
     * @return
     */
    public static List<String> parseStringToStringList(String source,
            String token) {

        if (StringUtils.isBlank(source) || StringUtils.isEmpty(token)) {
            return null;
        }

        List<String> result = new ArrayList<String>();

        String[] units = source.split(token);
        for (String unit : units) {
            result.add(unit);
        }
        return result;
    }

    /**
     * 
     * @param source
     * @param token
     * @return
     */
    public static List<Integer> parseStringToIntegerList(String source,
            String token) {

        if (StringUtils.isBlank(source) || StringUtils.isEmpty(token)) {
            return null;
        }

        List<Integer> result = new ArrayList<Integer>();
        String[] units = source.split(token);
        for (String unit : units) {
            result.add(Integer.valueOf(unit));
        }

        return result;
    }

    /**
     * 
     * @param source
     * @param token
     * @return
     */
    public static List<Long> parseStringToLongList(String source, String token) {

        if (StringUtils.isBlank(source) || StringUtils.isEmpty(token)) {
            return null;
        }

        List<Long> result = new ArrayList<Long>();
        String[] units = source.split(token);
        for (String unit : units) {
            result.add(Long.valueOf(unit));
        }

        return result;
    }

    public static boolean isFieldLengthOK(String content, int length) {
        if (content == null || "".equals(content)) {
            return true;
        }

        try {
            byte[] b = content.getBytes("GBK");

            return b.length <= length;

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("", e);
            return false;
        }
    }

    public static String makeStrFromCollection(List<String> list, String delim) {

        if (list == null || delim == null) {
            return null;
        }

        if (list.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            builder.append(delim);
            builder.append(list.get(i));
        }

        return builder.toString();
    }

    /**
     * 名字编码
     * 
     * @param fileName
     * @return
     */
    public static String fileNameEncode(String fileName) {

        if (fileName == null)
            return null;
        try {
            return new String(fileName.getBytes("gbk"), "ISO-8859-1");
        } catch (Exception ex) {
            return fileName;
        }
    }

    public static Long parse2Long(String s) {
        try {
            return Long.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer parse2Integer(String s) {
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }
}
