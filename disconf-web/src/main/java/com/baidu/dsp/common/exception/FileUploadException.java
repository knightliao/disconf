package com.baidu.dsp.common.exception;

import com.baidu.dsp.common.constant.ErrorCode;
import com.baidu.dsp.common.constant.ModuleCode;
import com.baidu.dsp.common.exception.base.RuntimeGlobalException;

/**
 * 文件上传错误
 *
 * @author liaoqiqi
 * @version 2014-2-20
 */
public class FileUploadException extends RuntimeGlobalException {

    private static final long serialVersionUID = 1L;

    public FileUploadException(String exceptionMessage) {
        super(ErrorCode.FILEUPLOAD_ERROR, exceptionMessage);
    }

    @Override
    public ModuleCode getModuleCode() {
        return ModuleCode.FILE;
    }

    public FileUploadException(String exceptionMessage, Throwable throwable) {

        super(ErrorCode.FILEUPLOAD_ERROR, exceptionMessage, throwable);
    }

}
