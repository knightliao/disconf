package com.baidu.dsp.common.constraint.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.baidu.dsp.common.constraint.ChineseLengthConstrant;

/**
 * 中文长度校验实现
 *
 * @author zhangbi
 * @date 2014年1月13日下午9:26:39
 */
public class ChineseLengthValidator implements ConstraintValidator<ChineseLengthConstrant, String> {

    private long maxLength;
    private long minLength;
    private int chineseHoldLength;

    @Override
    public void initialize(ChineseLengthConstrant constraintAnnotation) {
        this.maxLength = constraintAnnotation.maxLength();
        this.minLength = constraintAnnotation.minLength();
        this.chineseHoldLength = constraintAnnotation.cnHoldLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.equals("")) {
            return true;
        }

        // 如果字符串总长度大于最大长度直接返回false,避免下面轮个判断处理
        if (value.length() * this.chineseHoldLength > maxLength) {
            return false;
        }

        long chineseLength = getChineseLength(value);
        if (chineseLength >= minLength && chineseLength <= maxLength) {
            return true;
        }
        return false;
    }

    /**
     * 获取中文字符串长度 中文算N个字符(N由{@link ChineseLengthConstrant#cnHoldLength()}指定,
     * 默认为2),英文算一个
     *
     * @param value
     *
     * @return
     */
    private long getChineseLength(String value) {
        long valueLength = 0;
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            char temp = value.charAt(i);
            /* 判断是否为中文字符 */
            if ((temp >= '\u4e00' && temp <= '\u9fa5') || (temp >= '\ufe30' && temp <= '\uffa0')) {
                /* 中文长度倍数 */
                valueLength += this.chineseHoldLength;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
