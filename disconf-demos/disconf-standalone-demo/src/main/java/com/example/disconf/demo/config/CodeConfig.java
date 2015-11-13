package com.example.disconf.demo.config;

import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * Created by knightliao on 15/1/7.
 */
@Service
@DisconfFile(filename = "code.properties", copy2TargetDirPath = "disconf")
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
