package com.baidu.dsp.common.exception.base;

import com.baidu.dsp.common.constant.ErrorCode;

/**
 * 所有Exception异常类的基类
 *
 * @author liaoqiqi
 * @version 2013-12-2
 */
public abstract class DspException extends Exception implements GlobalExceptionAware {

    /**
     *
     */
    private static final long serialVersionUID = 3700791594685854374L;
    protected String exceptionMessage;
    protected ErrorCode errorCode;

    public DspException() {
        super();
    }

    public DspException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * 获取异常编码
     *
     * @return
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * 获取异常消息
     *
     * @return
     */
    public String getErrorMessage() {

        return exceptionMessage;
    }

}
