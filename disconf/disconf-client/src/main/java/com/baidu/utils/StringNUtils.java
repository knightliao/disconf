package com.baidu.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public final class StringNUtils extends org.apache.commons.lang.StringUtils {

    // private static final Logger logger =
    // LoggerFactory.getLogger(StringUtils.class);

    private static char[] hex = "0123456789ABCDEF".toCharArray();

    private StringNUtils() {
        super();
    }

    public static String escapeNull(final Object object) {
        if (null == object) {
            return "null";
        } else {
            return object.toString();
        }
    }

    /**
     * 解析字符串为Long型List
     * 
     * @param String
     *            source 待转换的源字符串
     * @param String
     *            token 分隔源字符串的特殊字符
     * @return List<Long> 解析所得的Long型List
     * 
     * @author guojichun
     * @version 1.0.0
     */
    public static List<Long> parseStringToLongList(final String source,
            final String token) {
        if (StringNUtils.isBlank(source) || StringNUtils.isBlank(token)) {
            return null;
        }
        final List<Long> result = new ArrayList<Long>();
        final String[] units = source.split(token);
        for (String unit : units) {
            result.add(Long.valueOf(unit));
        }
        return result;
    }

    /**
     * 解析字符串为Integer型List
     * 
     * @param String
     *            source 待转换的源字符串
     * @param String
     *            token 分隔源字符串的特殊字符
     * @return List<Long> 解析所得的Integer型List
     * 
     * @author guojichun
     * @version 1.0.0
     */
    public static List<Integer> parseStringToIntegerList(final String source,
            final String token) {
        if (StringNUtils.isBlank(source) || StringNUtils.isBlank(token)) {
            return null;
        }
        final List<Integer> result = new ArrayList<Integer>();
        final String[] units = source.split(token);
        for (String unit : units) {
            result.add(Integer.valueOf(unit));
        }
        return result;
    }

    /**
     * 解析字符串为String型List
     * 
     * @param String
     *            source 待转换的源字符串
     * @param String
     *            token 分隔源字符串的特殊字符
     * @return List<Long> 解析所得的String型List
     * 
     * @author guojichun
     * @version 1.0.0
     */
    public static List<String> parseStringToStringList(final String source,
            final String token) {
        if (StringNUtils.isBlank(source) || StringNUtils.isBlank(token)) {
            return null;
        }
        final List<String> result = new ArrayList<String>();
        final String[] units = source.split(token);
        for (String unit : units) {
            result.add(unit);
        }
        return result;
    }

    /**
     * 将数组转化为指定分隔符的字符串
     * 
     * @param List
     *            list 待转换的数组
     * @param String
     *            token 分隔源字符串的特殊字符
     * @return String 转化所得的String
     * 
     * @author guojichun
     * @version 1.0.1
     */
    @SuppressWarnings("rawtypes")
    public static String parseListToString(final List list, final String token) {
        if (null == token) {
            return null;
        }
        if (list.isEmpty()) {
            return "";
        }
        final StringBuilder temp = new StringBuilder();
        for (Object unit : list) {
            temp.append(unit.toString()).append(token);
        }
        if (temp.length() > 0) {
            return temp.substring(0, temp.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * 将普通字符串转变为json转码字符串，参考了googlecode上的json插件代码
     * 
     * @param String
     *            source 待转换的源字符串
     * @return String 转码后的字符串
     * 
     * @author guojichun
     * @version 1.0.0
     */
    public static String escape4Json(final String source) {
        if (source == null) {
            return "";
        }
        final StringBuilder buf = new StringBuilder();
        final CharacterIterator it = new StringCharacterIterator(source);
        for (char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
            if (c == '"') {
                buf.append("\\\"");
            } else if (c == '\\') {
                buf.append("\\\\");
            } else if (c == '/') {
                buf.append("\\/");
            } else if (c == '\b') {
                buf.append("\\b");
            } else if (c == '\f') {
                buf.append("\\f");
            } else if (c == '\n') {
                buf.append("\\n");
            } else if (c == '\r') {
                buf.append("\\r");
            } else if (c == '\t') {
                buf.append("\\t");
            } else if (Character.isISOControl(c)) {
                unicode(buf, c);
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * Represent as unicode
     * 
     * @param c
     *            character to be encoded
     */
    private static void unicode(final StringBuilder buf, final char c) {
        buf.append("\\u");
        int n = c;
        final int magincNum = 4;
        for (int i = 0; i < magincNum; ++i) {
            final int filterValue = 0xf000;
            final int filterNum = 12;
            final int digit = (n & filterValue) >> filterNum;
            buf.append(hex[digit]);
            n <<= magincNum;
        }
    }

    /**
     * 将普通字符串转变为html实体字符转码字符串
     * 
     * @param String
     *            source 待转换的源字符串
     * @return String 转码后的字符串
     * 
     * @author guojichun
     * @version 1.0.0
     */
    public static String escape4Html(final String source) {
        if (source == null) {
            return "";
        }
        // 不使用标准的转码，因为标准转码不转单引号，转中文不符合我们的要求
        // return StringEscapeUtils.escapeHtml(source);
        final StringBuffer str = new StringBuffer();
        for (int j = 0; j < source.length(); j++) {
            final char c = source.charAt(j);
            if (c < '\200') {

                switch (c) {
                case '"':
                    str.append("&#34;");
                    break;
                case '&':
                    str.append("&#38;");
                    break;
                case '<':
                    str.append("&#60;");
                    break;
                case '>':
                    str.append("&#62;");
                    break;
                case '\'':
                    str.append("&#39;");
                    break;
                case '/':
                    str.append("&#47;");
                    break;
                case '\\':
                    str.append("&#92;");
                    break;
                default:
                    str.append(c);
                }

            } else {
                str.append(c);
            }
            // 中文不用转义
            /*
             * else if (c > '\256') { str.append("&#" + (long) c + ";"); }
             */
        }
        return str.toString();
    }

    /**
     * 将普通字符串中的中文进行转义
     * 
     * @param String
     *            source 待转换的源字符串
     * @return String 转码后的字符串
     * 
     * @author guojichun
     * @version 1.0.0
     */
    public static String escape4Chinese(final String source, final String encode)
            throws UnsupportedEncodingException {
        if (null == source) {
            return null;
        }
        int i = 0;
        int j = 0;
        final int k = 10;
        final StringBuffer localStringBuffer = new StringBuffer(source.length());
        final ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(
                k);
        OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(
                localByteArrayOutputStream, encode);
        for (int l = 0; l < source.length(); ++l) {
            final int i1 = source.charAt(l);
            if (i1 < '\256') {
                localStringBuffer.append((char) i1);
            } else {
                try {
                    if (j != 0) {
                        localOutputStreamWriter = new OutputStreamWriter(
                                localByteArrayOutputStream, encode);
                        j = 0;
                    }
                    localOutputStreamWriter.write(i1);

                    if ((i1 >= 55296) && (i1 <= 56319)
                            && (l + 1 < source.length())) {
                        final int i2 = source.charAt(l + 1);
                        if ((i2 >= 56320) && (i2 <= 57343)) {
                            localOutputStreamWriter.write(i2);
                            ++l;
                        }
                    }
                    localOutputStreamWriter.flush();
                } catch (IOException localIOException) {
                    localByteArrayOutputStream.reset();
                    break;
                }
                final byte[] arrayOfByte = localByteArrayOutputStream
                        .toByteArray();
                for (int i3 = 0; i3 < arrayOfByte.length; ++i3) {
                    localStringBuffer.append('%');
                    char c = Character.forDigit(arrayOfByte[i3] >> 4 & 0xF, 16);
                    if (Character.isLetter(c)) {
                        c = (char) (c - ' ');
                    }
                    localStringBuffer.append(c);
                    c = Character.forDigit(arrayOfByte[i3] & 0xF, 16);
                    if (Character.isLetter(c)) {
                        c = (char) (c - ' ');
                    }
                    localStringBuffer.append(c);
                }
                localByteArrayOutputStream.reset();
                i = 1;
            }
        }

        if (i != 0) {
            return localStringBuffer.toString();
        } else {

            return source;
        }
    }

    /**
     * 将double型转为两位小数的人民币数值
     * 
     * @param Double
     * @return String
     * 
     * @author modi
     * @version 1.0.0
     */
    public static String getMoney(final Double num) {

        final DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(num);
    }

    /**
     * 将数字转换为两位小数的百分数表示
     * 
     * @param Double
     *            需要表示为百分数的数字
     * @return String 代表百分数的字符串
     * 
     * @author modi
     * @version 1.0.0
     */
    public static String getPercent(final Double num) {

        final DecimalFormat df = new DecimalFormat("############0.00");
        final int percentNum = 100;
        final String rtNum = df.format(num * percentNum);

        return rtNum + "%";
    }

    /**
     * 将String中出现某一子串之处用另一子串代替
     * 
     * @param inString
     *            进行处理的字符串
     * @param oldPattern
     *            被替换的子串
     * @param newPattern
     *            替换的子串
     * @return String 被替换后的字符串
     * 
     * @author guojichun
     * @version 1.0.0
     */
    public static String replace(final String inString,
            final String oldPattern, final String newPattern) {

        if (inString == null) {
            return null;
        }
        if (oldPattern == null || newPattern == null) {
            return inString;
        }

        final StringBuffer sbuf = new StringBuffer();
        // output StringBuffer we'll build up
        int pos = 0; // Our position in the old string
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        final int patLen = oldPattern.length();
        while (index >= 0) {
            sbuf.append(inString.substring(pos, index));
            sbuf.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sbuf.append(inString.substring(pos));

        // remember to append any characters to the right of a match
        return sbuf.toString();
    }

    /**
     * 过滤字符串中csv的保留字符，目前为",|
     * 
     * @param String
     *            源字符串
     * @return String 过滤后的字符串
     * 
     * @author modi
     * @version 1.0.0
     */
    public static String parseForCsv(final String str) {

        if (str == null) {
            return null;
        } else {

            String strToBeReturn = str;
            strToBeReturn = str.replace("\"", "");
            strToBeReturn = str.replace(",", "");
            strToBeReturn = str.replace("|", "");

            return strToBeReturn;
        }
    }

    /**
     * 计算md5值
     * 
     * @author guojichun
     * @since 1.0.0
     * @param source
     * @return
     */
    public static String md5(final String source) {

        final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] bytes = source.getBytes();
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            bytes = md.digest();
            final int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < bytes.length; i++) {
                final byte b = bytes[i];
                final int magicNum = 4;
                final int magicNum2 = 0xf;
                chars[k++] = hexChars[b >>> magicNum & magicNum2];
                chars[k++] = hexChars[b & magicNum2];
            }
            return new String(chars);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查cpro的拼串参数是否合法
     * 
     * @author guojichun
     * @since 1.0.8
     * @param paramStr
     *            cpro拼串参数
     * @return 是否合法
     */
    public static boolean validCproParamStr(final String paramStr) {
        if (StringNUtils.isBlank(paramStr)) {
            return false;
        }
        if (paramStr.indexOf('\r') >= 0) {
            return false;
        }
        if (paramStr.indexOf('\n') >= 0) {
            return false;
        }
        if (paramStr.indexOf('\t') >= 0) {
            return false;
        }
        if (paramStr.indexOf(' ') >= 0) {
            return false;
        }
        return true;
    }

    /**
     * 该方法将字符串按照分隔符分开，并将每段字符串转成指定类型。支持String,Double,Float,Integer,Long
     * 
     * @param <T>
     * @param paramValue
     * @param sep
     * @param targetClazz
     * @return
     */
    public static <T> List<T> splitParameterStringValue(
            final String paramValue, final String sep,
            final Class<T> targetClazz) {
        return splitParameterStringValue(paramValue, sep, targetClazz, false);
    }

    /**
     * 该方法将字符串按照分隔符分开，并将每段字符串转成指定类型。支持String,Double,Float,Integer,Long,
     * BigDecimal
     * 
     * @param <T>
     * @param paramValue
     * @param sep
     * @param targetClazz
     * @return
     */
    public static <T> List<T> splitParameterStringValue(
            final String paramValue, final String sep,
            final Class<T> targetClazz, final boolean preserveToken) {

        if (paramValue == null) {
            return null;
        }

        final List<T> valueList = new ArrayList<T>();

        String[] valueArr = null;
        if (!preserveToken) {
            valueArr = StringNUtils.split(paramValue, sep);
        } else {

            valueArr = StringNUtils.splitPreserveAllTokens(paramValue, sep);
        }

        for (String value : valueArr) {
            T t = null;
            try {
                t = convertToValue(value, targetClazz);
            } catch (Exception e) {
                return null;
            }

            valueList.add(t);
        }

        return valueList;
    }

    @SuppressWarnings("unchecked")
    private static <T> T convertToValue(final String value,
            final Class<T> targetClazz) throws Exception {

        String valueToBeReturn = value;

        if (StringNUtils.isBlank(value)) {
            if (String.class.equals(targetClazz)) {
                return (T) valueToBeReturn;
            } else {
                valueToBeReturn = "0";
            }
        }

        if (targetClazz.equals(BigDecimal.class)) {
            return (T) new BigDecimal(valueToBeReturn);
        } else {
            final Method method = targetClazz
                    .getMethod("valueOf", String.class);
            return (T) method.invoke(null, valueToBeReturn);
        }

    }

    /**
     * 验证密码是否符合复杂度要求
     * 
     * @author guojichun
     * @since 1.0.5
     * @param password
     *            密码
     * @param patterNum
     *            要求包含几种字符
     * @return 是否合格
     */
    public static boolean isValidPassword(final String password,
            final int patterNum) {

        // add by yangchenxing since 1.1.2
        // druc要求密码长度为6-32个字符
        final int passwdMin = 6;
        final int passwdMax = 32;
        if (password.length() < passwdMin || password.length() > passwdMax) {
            return false;
        }

        String tempchar;
        boolean upperMatch = false;
        boolean lowerMatch = false;
        boolean numMatch = false;
        int matchNum = 0;
        final Pattern upperPat = Pattern.compile("^[A-Z]+$");
        final Pattern lowerPat = Pattern.compile("^[a-z]+$");
        final Pattern numPat = Pattern.compile("^[0-9]+$");
        final Pattern otherPat = Pattern.compile("^[^A-Za-z0-9]+$");

        Matcher m;
        for (int i = 0; i < password.length(); i++) {

            tempchar = password.substring(i, i + 1);
            m = upperPat.matcher(tempchar);
            if (m.find()) {
                upperMatch = true;
            }
            m = lowerPat.matcher(tempchar);
            if (m.find()) {
                lowerMatch = true;
            }
            m = numPat.matcher(tempchar);
            if (m.find()) {
                numMatch = true;
            }
            m = otherPat.matcher(tempchar);
            if (m.find()) {
                return false;
            }
        }
        if (upperMatch) {
            matchNum++;
        }
        if (lowerMatch) {
            matchNum++;
        }
        if (numMatch) {
            matchNum++;
        }
        if (matchNum < patterNum) {
            return false;
        }
        return true;
    }

    /**
     * Byte转换成十六进制字符串
     * 
     * @author zhangbi
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
