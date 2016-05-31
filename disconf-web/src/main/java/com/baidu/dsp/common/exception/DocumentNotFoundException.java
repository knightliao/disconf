package com.baidu.dsp.common.exception;

/**
 * 文档不存在错误
 *
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class DocumentNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 545280194497047918L;

    public DocumentNotFoundException(String fileName) {
        super(fileName + " file does not exist!");
    }

}
