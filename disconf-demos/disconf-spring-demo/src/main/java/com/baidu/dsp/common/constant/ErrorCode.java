package com.baidu.dsp.common.constant;

/**
 * 错误代码定义
 *
 * @author liaoqiqi
 * @version 2013-12-2
 */
public enum ErrorCode {

    /**
     * 正常的
     */
    DEFAULT_ERROR(100),

    /**
     * 框架带的
     */
    // 请求方法不支持
    HttpRequestMethodNotSupportedException(1000),
    // 参数不匹配
    TYPE_MIS_MATCH(1001),
    // 缺少参数
    MissingServletRequestParameterException(1002),

    /**
     * 自定义的
     */
    // 域错误
    FIELD_ERROR(2000),
    // 全局错误，严重的错误,一般要退出的
    GLOBAL_ERROR(2002),
    // 只读错误
    READ_ONLY_ERROR(2004),
    // 登录出错
    LOGIN_ERROR(2006),
    // 远程的错误
    REMOTE_ERROR(2008),
    // 数据库保存的错误
    DAO_ERROR(2010),
    // 没有权限
    ACCESS_NOAUTH_ERROR(2012),
    // 文件上传错误
    FILEUPLOAD_ERROR(2014);

    private int code = 0;

    private ErrorCode(int code) {
        this.code = code;

    }

    public int getCode() {
        return code;
    }

}
