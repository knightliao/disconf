package com.example.disconf.demo.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * Created by knightliao on 15/1/7.
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "code.properties")
public class CodeConfig {

    private String codeError = "";

    @DisconfFileItem(name = "syserror.paramtype", associateField = "codeError")
    public String getCodeError() {
        return codeError;
    }

    public void setCodeError(String codeError) {
        this.codeError = codeError;
    }
}
