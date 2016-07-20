package com.baidu.disconf.client.support.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 有关字符串处理的工具类。
 * <p>
 * 这个类中的每个方法都可以“安全”地处理<code>null</code>，而不会抛出<code>NullPointerException</code>。
 * </p>
 *
 * @author liaoqiqi
 * @version 2014-8-28
 */
public abstract class StringUtil {

    private static final char[] DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            .toCharArray();
    private static final char[] DIGITS_NOCASE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .toCharArray();

    /**
     * @param source
     * @param token
     *
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

    // ==========================================================================
    // 默认值函数。
    //
    // 当字符串为empty或blank时，将字符串转换成指定的默认字符串。
    // 注：判断字符串为null时，可用更通用的ObjectUtil.defaultIfNull。
    // ==========================================================================

    /**
     * @param source
     * @param token
     *
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

    /**
     * @param source
     * @param token
     *
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

    // FIXME 待补

    // ==========================================================================
    // 格式替换。
    // ==========================================================================

    /**
     * 如果字符串是<code>null</code>或空字符串<code>""</code>，则返回指定默认字符串，否则返回字符串本身。
     * <p/>
     * <p/>
     * <pre>
     * StringUtil.defaultIfEmpty(null, "default")  = "default"
     * StringUtil.defaultIfEmpty("", "default")    = "default"
     * StringUtil.defaultIfEmpty("  ", "default")  = "  "
     * StringUtil.defaultIfEmpty("bat", "default") = "bat"
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     *
     * @return 字符串本身或指定的默认字符串
     */
    public static String defaultIfEmpty(String str, String defaultStr) {
        return str == null || str.length() == 0 ? defaultStr : str;
    }

    /**
     * 如果字符串是<code>null</code>或空字符串<code>""</code>，则返回指定默认字符串，否则返回字符串本身。
     * <p/>
     * <p/>
     * <pre>
     * StringUtil.defaultIfBlank(null, "default")  = "default"
     * StringUtil.defaultIfBlank("", "default")    = "default"
     * StringUtil.defaultIfBlank("  ", "default")  = "default"
     * StringUtil.defaultIfBlank("bat", "default") = "bat"
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     *
     * @return 字符串本身或指定的默认字符串
     */
    public static String defaultIfBlank(String str, String defaultStr) {
        return StringUtils.isBlank(str) ? defaultStr : str;
    }

    /**
     * 将字符串转换成camel case。
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p/>
     * <p/>
     * <pre>
     * StringUtil.toCamelCase(null)  = null
     * StringUtil.toCamelCase("")    = ""
     * StringUtil.toCamelCase("aBc") = "aBc"
     * StringUtil.toCamelCase("aBc def") = "aBcDef"
     * StringUtil.toCamelCase("aBc def_ghi") = "aBcDefGhi"
     * StringUtil.toCamelCase("aBc def_ghi 123") = "aBcDefGhi123"
     * </pre>
     * <p/>
     * </p>
     * <p>
     * 此方法会保留除了下划线和空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     *
     * @return camel case字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String toCamelCase(String str) {
        return new WordTokenizer() {
            @Override
            protected void startSentence(StringBuilder buffer, char ch) {
                buffer.append(Character.toLowerCase(ch));
            }

            @Override
            protected void startWord(StringBuilder buffer, char ch) {
                if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                    buffer.append(Character.toUpperCase(ch));
                } else {
                    buffer.append(Character.toLowerCase(ch));
                }
            }

            @Override
            protected void inWord(StringBuilder buffer, char ch) {
                buffer.append(Character.toLowerCase(ch));
            }

            @Override
            protected void startDigitSentence(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }

            @Override
            protected void startDigitWord(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }

            @Override
            protected void inDigitWord(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }

            @Override
            protected void inDelimiter(StringBuilder buffer, char ch) {
                if (ch != UNDERSCORE) {
                    buffer.append(ch);
                }
            }
        }.parse(str);
    }

    /**
     * 将字符串转换成pascal case。
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p/>
     * <p/>
     * <pre>
     * StringUtil.toPascalCase(null)  = null
     * StringUtil.toPascalCase("")    = ""
     * StringUtil.toPascalCase("aBc") = "ABc"
     * StringUtil.toPascalCase("aBc def") = "ABcDef"
     * StringUtil.toPascalCase("aBc def_ghi") = "ABcDefGhi"
     * StringUtil.toPascalCase("aBc def_ghi 123") = "aBcDefGhi123"
     * </pre>
     * <p/>
     * </p>
     * <p>
     * 此方法会保留除了下划线和空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     *
     * @return pascal case字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String toPascalCase(String str) {
        return new WordTokenizer() {
            @Override
            protected void startSentence(StringBuilder buffer, char ch) {
                buffer.append(Character.toUpperCase(ch));
            }

            @Override
            protected void startWord(StringBuilder buffer, char ch) {
                buffer.append(Character.toUpperCase(ch));
            }

            @Override
            protected void inWord(StringBuilder buffer, char ch) {
                buffer.append(Character.toLowerCase(ch));
            }

            @Override
            protected void startDigitSentence(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }

            @Override
            protected void startDigitWord(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }

            @Override
            protected void inDigitWord(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }

            @Override
            protected void inDelimiter(StringBuilder buffer, char ch) {
                if (ch != UNDERSCORE) {
                    buffer.append(ch);
                }
            }
        }.parse(str);
    }

    /**
     * 将字符串转换成下划线分隔的大写字符串。
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p/>
     * <p/>
     * <pre>
     * StringUtil.toUpperCaseWithUnderscores(null)  = null
     * StringUtil.toUpperCaseWithUnderscores("")    = ""
     * StringUtil.toUpperCaseWithUnderscores("aBc") = "A_BC"
     * StringUtil.toUpperCaseWithUnderscores("aBc def") = "A_BC_DEF"
     * StringUtil.toUpperCaseWithUnderscores("aBc def_ghi") = "A_BC_DEF_GHI"
     * StringUtil.toUpperCaseWithUnderscores("aBc def_ghi 123") = "A_BC_DEF_GHI_123"
     * StringUtil.toUpperCaseWithUnderscores("__a__Bc__") = "__A__BC__"
     * </pre>
     * <p/>
     * </p>
     * <p>
     * 此方法会保留除了空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     *
     * @return 下划线分隔的大写字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String toUpperCaseWithUnderscores(String str) {
        return new WordTokenizer() {
            @Override
            protected void startSentence(StringBuilder buffer, char ch) {
                buffer.append(Character.toUpperCase(ch));
            }

            @Override
            protected void startWord(StringBuilder buffer, char ch) {
                if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                    buffer.append(UNDERSCORE);
                }

                buffer.append(Character.toUpperCase(ch));
            }

            @Override
            protected void inWord(StringBuilder buffer, char ch) {
                buffer.append(Character.toUpperCase(ch));
            }

            @Override
            protected void startDigitSentence(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }

            @Override
            protected void startDigitWord(StringBuilder buffer, char ch) {
                if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                    buffer.append(UNDERSCORE);
                }

                buffer.append(ch);
            }

            @Override
            protected void inDigitWord(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }

            @Override
            protected void inDelimiter(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }
        }.parse(str);
    }

    // ==========================================================================
    // 将数字或字节转换成ASCII字符串的函数。
    // ==========================================================================

    /**
     * 将字符串转换成下划线分隔的小写字符串。
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p/>
     * <p/>
     * <pre>
     * StringUtil.toLowerCaseWithUnderscores(null)  = null
     * StringUtil.toLowerCaseWithUnderscores("")    = ""
     * StringUtil.toLowerCaseWithUnderscores("aBc") = "a_bc"
     * StringUtil.toLowerCaseWithUnderscores("aBc def") = "a_bc_def"
     * StringUtil.toLowerCaseWithUnderscores("aBc def_ghi") = "a_bc_def_ghi"
     * StringUtil.toLowerCaseWithUnderscores("aBc def_ghi 123") = "a_bc_def_ghi_123"
     * StringUtil.toLowerCaseWithUnderscores("__a__Bc__") = "__a__bc__"
     * </pre>
     * <p/>
     * </p>
     * <p>
     * 此方法会保留除了空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     *
     * @return 下划线分隔的小写字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String toLowerCaseWithUnderscores(String str) {
        return new WordTokenizer() {
            @Override
            protected void startSentence(StringBuilder buffer, char ch) {
                buffer.append(Character.toLowerCase(ch));
            }

            @Override
            protected void startWord(StringBuilder buffer, char ch) {
                if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                    buffer.append(UNDERSCORE);
                }

                buffer.append(Character.toLowerCase(ch));
            }

            @Override
            protected void inWord(StringBuilder buffer, char ch) {
                buffer.append(Character.toLowerCase(ch));
            }

            @Override
            protected void startDigitSentence(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }

            @Override
            protected void startDigitWord(StringBuilder buffer, char ch) {
                if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                    buffer.append(UNDERSCORE);
                }

                buffer.append(ch);
            }

            @Override
            protected void inDigitWord(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }

            @Override
            protected void inDelimiter(StringBuilder buffer, char ch) {
                buffer.append(ch);
            }
        }.parse(str);
    }

    /**
     * 将一个长整形转换成62进制的字符串。
     *
     * @param longValue 64位数字
     *
     * @return 62进制的字符串
     */
    public static String longToString(long longValue) {
        return longToString(longValue, false);
    }

    /**
     * 将一个长整形转换成62进制的字符串。
     *
     * @param longValue 64位数字
     * @param noCase    区分大小写
     *
     * @return 62进制的字符串
     */
    public static String longToString(long longValue, boolean noCase) {
        char[] digits = noCase ? DIGITS_NOCASE : DIGITS;
        int digitsLength = digits.length;

        if (longValue == 0) {
            return String.valueOf(digits[0]);
        }

        if (longValue < 0) {
            longValue = -longValue;
        }

        StringBuilder strValue = new StringBuilder();

        while (longValue != 0) {
            int digit = (int) (longValue % digitsLength);
            longValue = longValue / digitsLength;

            strValue.append(digits[digit]);
        }

        return strValue.toString();
    }

    /**
     * 将一个byte数组转换成62进制的字符串。
     *
     * @param bytes 二进制数组
     *
     * @return 62进制的字符串
     */
    public static String bytesToString(byte[] bytes) {
        return bytesToString(bytes, false);
    }

    /**
     * 将一个byte数组转换成62进制的字符串。
     *
     * @param bytes  二进制数组
     * @param noCase 区分大小写
     *
     * @return 62进制的字符串
     */
    public static String bytesToString(byte[] bytes, boolean noCase) {
        char[] digits = noCase ? DIGITS_NOCASE : DIGITS;
        int digitsLength = digits.length;

        if (ArrayUtils.isEmpty(bytes)) {
            return String.valueOf(digits[0]);
        }

        StringBuilder strValue = new StringBuilder();
        int value = 0;
        int limit = Integer.MAX_VALUE >>> 8;
        int i = 0;

        do {
            while (i < bytes.length && value < limit) {
                value = (value << 8) + (0xFF & bytes[i++]);
            }

            while (value >= digitsLength) {
                strValue.append(digits[value % digitsLength]);
                value = value / digitsLength;
            }
        } while (i < bytes.length);

        if (value != 0 || strValue.length() == 0) {
            strValue.append(digits[value]);
        }

        return strValue.toString();
    }

    /**
     * 判断字符串<code>str</code>是否以字符<code>ch</code>结尾
     *
     * @param str 要比较的字符串
     * @param ch  结尾字符
     *
     * @return 如果字符串<code>str</code>是否以字符<code>ch</code>结尾，则返回<code>true</code>
     */
    public static boolean endsWithChar(String str, char ch) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }

        return str.charAt(str.length() - 1) == ch;
    }

    // ==========================================================================
    // 将字符串转化成集合
    // ==========================================================================

    // public static <T> List<T> toArrayList(String value) {
    // if (value == null) {
    // return null;
    // }
    // // FIXME "[ ...N个空格... ]"暂不考虑
    // if (StringUtils.isBlank(value) || value.equals("[]")) {
    // return Collections.emptyList();
    // }
    //
    // // ", "
    // value = StringUtils.substringBetween(value, "[", "]");
    // String[] array = StringUtils.split(value, ", ");
    // List<T>
    //
    // }

    // ==========================================================================
    // 字符串比较相关。
    // ==========================================================================

    /**
     * 判断字符串<code>str</code>是否以字符<code>ch</code>开头
     *
     * @param str 要比较的字符串
     * @param ch  开头字符
     *
     * @return 如果字符串<code>str</code>是否以字符<code>ch</code> 开头，则返回<code>true</code>
     */
    public static boolean startsWithChar(String str, char ch) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }

        return str.charAt(0) == ch;
    }

    public static int indexOfChars(String string, String chars) {
        return indexOfChars(string, chars, 0);
    }

    // ==========================================================================
    // 字符串索引相关。
    // ==========================================================================

    public static int indexOfChars(String string, String chars, int startindex) {
        if (string == null || chars == null) {
            return -1;
        }

        int stringLen = string.length();
        int charsLen = chars.length();

        if (startindex < 0) {
            startindex = 0;
        }

        for (int i = startindex; i < stringLen; i++) {
            char c = string.charAt(i);
            for (int j = 0; j < charsLen; j++) {
                if (c == chars.charAt(j)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static int indexOfChars(String string, char[] chars) {
        return indexOfChars(string, chars, 0);
    }

    public static int indexOfChars(String string, char[] chars, int startindex) {
        if (string == null || chars == null) {
            return -1;
        }

        int stringLen = string.length();
        int charsLen = chars.length;

        for (int i = startindex; i < stringLen; i++) {
            char c = string.charAt(i);
            for (int j = 0; j < charsLen; j++) {
                if (c == chars[j]) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * 解析出下列语法所构成的<code>SENTENCE</code>。
     * <p/>
     * <p/>
     * <pre>
     *  SENTENCE = WORD (DELIMITER* WORD)*
     *
     *  WORD = UPPER_CASE_WORD | LOWER_CASE_WORD | TITLE_CASE_WORD | DIGIT_WORD
     *
     *  UPPER_CASE_WORD = UPPER_CASE_LETTER+
     *  LOWER_CASE_WORD = LOWER_CASE_LETTER+
     *  TITLE_CASE_WORD = UPPER_CASE_LETTER LOWER_CASE_LETTER+
     *  DIGIT_WORD      = DIGIT+
     *
     *  UPPER_CASE_LETTER = Character.isUpperCase()
     *  LOWER_CASE_LETTER = Character.isLowerCase()
     *  DIGIT             = Character.isDigit()
     *  NON_LETTER_DIGIT  = !Character.isUpperCase() && !Character.isLowerCase() && !Character.isDigit()
     *
     *  DELIMITER = WHITESPACE | NON_LETTER_DIGIT
     * </pre>
     */
    private abstract static class WordTokenizer {
        protected static final char UNDERSCORE = '_';

        /**
         * Parse sentence。
         */
        public String parse(String str) {
            if (StringUtils.isEmpty(str)) {
                return str;
            }

            int length = str.length();
            StringBuilder buffer = new StringBuilder(length);

            for (int index = 0; index < length; index++) {
                char ch = str.charAt(index);

                // 忽略空白。
                if (Character.isWhitespace(ch)) {
                    continue;
                }

                // 大写字母开始：UpperCaseWord或是TitleCaseWord。
                if (Character.isUpperCase(ch)) {
                    int wordIndex = index + 1;

                    while (wordIndex < length) {
                        char wordChar = str.charAt(wordIndex);

                        if (Character.isUpperCase(wordChar)) {
                            wordIndex++;
                        } else if (Character.isLowerCase(wordChar)) {
                            wordIndex--;
                            break;
                        } else {
                            break;
                        }
                    }

                    // 1. wordIndex == length，说明最后一个字母为大写，以upperCaseWord处理之。
                    // 2. wordIndex == index，说明index处为一个titleCaseWord。
                    // 3. wordIndex > index，说明index到wordIndex -
                    // 1处全部是大写，以upperCaseWord处理。
                    if (wordIndex == length || wordIndex > index) {
                        index = parseUpperCaseWord(buffer, str, index,
                                wordIndex);
                    } else {
                        index = parseTitleCaseWord(buffer, str, index);
                    }

                    continue;
                }

                // 小写字母开始：LowerCaseWord。
                if (Character.isLowerCase(ch)) {
                    index = parseLowerCaseWord(buffer, str, index);
                    continue;
                }

                // 数字开始：DigitWord。
                if (Character.isDigit(ch)) {
                    index = parseDigitWord(buffer, str, index);
                    continue;
                }

                // 非字母数字开始：Delimiter。
                inDelimiter(buffer, ch);
            }

            return buffer.toString();
        }

        private int parseUpperCaseWord(StringBuilder buffer, String str,
                                       int index, int length) {
            char ch = str.charAt(index++);

            // 首字母，必然存在且为大写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

            // 后续字母，必为小写。
            for (; index < length; index++) {
                ch = str.charAt(index);
                inWord(buffer, ch);
            }

            return index - 1;
        }

        private int parseLowerCaseWord(StringBuilder buffer, String str,
                                       int index) {
            char ch = str.charAt(index++);

            // 首字母，必然存在且为小写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

            // 后续字母，必为小写。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isLowerCase(ch)) {
                    inWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        private int parseTitleCaseWord(StringBuilder buffer, String str,
                                       int index) {
            char ch = str.charAt(index++);

            // 首字母，必然存在且为大写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

            // 后续字母，必为小写。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isLowerCase(ch)) {
                    inWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        private int parseDigitWord(StringBuilder buffer, String str, int index) {
            char ch = str.charAt(index++);

            // 首字符，必然存在且为数字。
            if (buffer.length() == 0) {
                startDigitSentence(buffer, ch);
            } else {
                startDigitWord(buffer, ch);
            }

            // 后续字符，必为数字。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isDigit(ch)) {
                    inDigitWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        protected boolean isDelimiter(char ch) {
            return !Character.isUpperCase(ch) && !Character.isLowerCase(ch)
                    && !Character.isDigit(ch);
        }

        protected abstract void startSentence(StringBuilder buffer, char ch);

        protected abstract void startWord(StringBuilder buffer, char ch);

        protected abstract void inWord(StringBuilder buffer, char ch);

        protected abstract void startDigitSentence(StringBuilder buffer, char ch);

        protected abstract void startDigitWord(StringBuilder buffer, char ch);

        protected abstract void inDigitWord(StringBuilder buffer, char ch);

        protected abstract void inDelimiter(StringBuilder buffer, char ch);
    }

}
