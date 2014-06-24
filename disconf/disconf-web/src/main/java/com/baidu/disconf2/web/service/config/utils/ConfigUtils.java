package com.baidu.disconf2.web.service.config.utils;

import com.baidu.disconf2.core.common.constants.Constants;
import com.baidu.disconf2.core.common.json.ValueVo;

public class ConfigUtils {

    /**
     * 
     * @param errorMsg
     * @return
     */
    public static ValueVo getErrorVo(String errorMsg) {

        ValueVo confItemVo = new ValueVo();
        confItemVo.setStatus(Constants.NOTOK);
        confItemVo.setValue("");
        confItemVo.setMessage(errorMsg);

        return confItemVo;
    }
}
