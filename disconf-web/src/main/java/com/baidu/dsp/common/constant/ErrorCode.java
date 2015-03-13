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
    HttpRequestMethodNotSupportedException(1000), TYPE_MIS_MATCH(1001), MissingServletRequestParameterException(1002),

    /**
     * 自定义的
     */

    // 域错误
    FIELD_ERROR(2000),

    // 全局式的，一般不会抛出
    GLOBAL_ERROR(2002), READ_ONLY_ERROR(2004),

    // 远程访问错误
    REMOTE_ERROR(2008),

    //
    // 权限问题
    //
    ACCESS_NOAUTH_ERROR(2010),

    // Mcpack Login error
    LOGIN_ERROR_MCPACK(21005), LOGIN_ERROR(2006),

    // 数据库保存的错误
    DAO_ERROR(2012),

    // 非预期错误
    UN_EXPECTED(2014),

    // 资源更新冲突错误
    // 由同时修改同一资源引起
    CONCURRENT_MODIFY_ERROR(2046),

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
